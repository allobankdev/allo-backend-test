package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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
public class CurrencyStrategy implements IDRDataFetcher {

    private static final String ENDPOINT = "/currencies";

    private final WebClient webClient;

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        Map<String, String> currencies;

        try {
            currencies = webClient.get()
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
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();
        } catch (ApiException e) {
            throw e;
        } catch (WebClientRequestException e) {
            throw mapClientException(e, ENDPOINT);
        } catch (Exception e) {
            throw new ApiException("Unexpected error while calling endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY, e);
        }

        validateResponse(currencies);

        return Map.of(
                "resourceType", getType(),
                "data", currencies,
                "fetchedAt", Instant.now().toString()
        );
    }

    private void validateResponse(Map<String, String> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            throw new ApiException("Response body is null/empty for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
        }

        if (!currencies.containsKey("USD") || currencies.get("USD") == null || currencies.get("USD").isBlank()) {
            throw new ApiException("Field USD is required for endpoint " + ENDPOINT, HttpStatus.BAD_GATEWAY);
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
