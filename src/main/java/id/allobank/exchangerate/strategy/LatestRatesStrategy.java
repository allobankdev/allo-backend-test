package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.model.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatestRatesStrategy implements IDRDataFetcher {

    private static final String ENDPOINT = "/latest?base=IDR";

    private final WebClient webClient;

    @Value("${app.github-username}")
    private String username;

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        LatestRatesResponse response;
        try {
            response = webClient.get()
                    .uri(ENDPOINT)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                            Mono.error(new ApiException(
                                    String.format("External API returned 4xx for endpoint %s", ENDPOINT),
                                    HttpStatus.BAD_GATEWAY)))
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                            Mono.error(new ApiException(
                                    String.format("External API returned 5xx for endpoint %s", ENDPOINT),
                                    HttpStatus.BAD_GATEWAY)))
                    .bodyToMono(LatestRatesResponse.class)
                    .block();
        } catch (ApiException e) {
            throw e;
        } catch (WebClientRequestException e) {
            throw mapClientException(e, ENDPOINT);
        } catch (Exception e) {
            throw new ApiException("Unexpected error while calling endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY, e);
        }

        validateResponse(response);

        Double usdRate = response.getRates().get("USD");
        log.info("USD Rate: {}", usdRate);

        if (usdRate == null) {
            throw new ApiException("Field rates.USD is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        double spread = calculateSpread(username);
        log.info("Spread: {}", spread);

        double result = (1 / usdRate) * (1 + spread);

        response.setUSD_BuySpread_IDR(result);

        return Map.of(
                "resourceType", getType(),
                "data", response,
                "fetchedAt", Instant.now().toString()
        );
    }

    private void validateResponse(LatestRatesResponse response) {
        if (response == null) {
            throw new ApiException("Response body is null for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        if (response.getBase() == null || response.getBase().isBlank()) {
            throw new ApiException("Field base is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        if (response.getDate() == null || response.getDate().isBlank()) {
            throw new ApiException("Field date is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        if (response.getRates() == null || response.getRates().isEmpty()) {
            throw new ApiException("Field rates is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }
    }

    private ApiException mapClientException(WebClientRequestException ex, String endpoint) {
        Throwable cause = ex.getCause();

        if (cause instanceof TimeoutException) {
            return new ApiException("Timeout while calling endpoint " + endpoint, HttpStatus.GATEWAY_TIMEOUT, ex);
        }

        return new ApiException("Failed to connect to external API endpoint " + endpoint, HttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    private double calculateSpread(String username) {
        if (username == null || username.isBlank()) {
            throw new ApiException("Invalid GitHub username: must not be null or blank");
        }

        int sum = username.toLowerCase(Locale.ROOT).chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
