package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.model.dto.HistoricalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class HistoricalStrategy implements IDRDataFetcher {

    private static final String ENDPOINT = "/2024-01-01..2024-01-05?from=IDR&to=USD";

    private final WebClient webClient;

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        HistoricalResponse response;

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
                    .bodyToMono(HistoricalResponse.class)
                    .block();
        } catch (ApiException e) {
            throw e;
        } catch (WebClientRequestException e) {
            throw mapClientException(e, ENDPOINT);
        } catch (Exception e) {
            throw new ApiException("Unexpected error while calling endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY, e);
        }

        validateResponse(response);

        return Map.of(
                "resourceType", getType(),
                "data", response,
                "fetchedAt", Instant.now().toString()
        );
    }

    private void validateResponse(HistoricalResponse response) {
        if (response == null) {
            throw new ApiException("Response body is null for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        if (response.getBase() == null || response.getBase().isBlank()) {
            throw new ApiException("Field base is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
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
}
