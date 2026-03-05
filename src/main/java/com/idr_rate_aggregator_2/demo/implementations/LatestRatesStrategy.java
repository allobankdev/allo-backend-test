package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.Error.ExternalApiException;
import com.idr_rate_aggregator_2.demo.dto.LatestRatesResponse;
import com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class LatestRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;

    @Autowired
    public LatestRatesStrategy(WebClient webClient,
                               @Value("${app.github.username}") String githubUsername) {
        this.webClient = webClient;
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Mono<LatestRatesResponse> fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API for username: {}", githubUsername);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        handle4xxError(clientResponse, "latest_idr_rates"))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        handle5xxError(clientResponse, "latest_idr_rates"))
                .bodyToMono(LatestRatesResponse.class)
                .map(this::enrichWithSpreadCalculation)
                .timeout(Duration.ofSeconds(10))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(5))
                        .filter(this::isRetryable)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalApiException(
                                    "Failed to fetch latest rates after retries: " + retrySignal.failure().getMessage(),
                                    retrySignal.failure()
                            );
                        }))
                .doOnSuccess(response -> log.info("✅ Successfully fetched latest IDR rates with spread: {}",
                        response != null ? response.getUSD_BuySpread_IDR() : "N/A"))
                .doOnError(error -> {
                    if (error instanceof ExternalApiException) {
                        log.error("❌ External API error: {}", error.getMessage());
                    } else {
                        log.error("❌ Unexpected error fetching latest rates: {}", error.getMessage(), error);
                    }
                })
                .onErrorResume(this::handleFallback);
    }

    private Mono<? extends Throwable> handle4xxError(org.springframework.web.reactive.function.client.ClientResponse response, String resourceType) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("❌ Client error (4xx) for {}: Status={}, Body={}",
                            resourceType, response.statusCode(), errorBody);
                    return Mono.error(new ExternalApiException(
                            String.format("Client error %s: %s", response.statusCode(), errorBody),
                            response.statusCode().value()
                    ));
                });
    }

    private Mono<? extends Throwable> handle5xxError(org.springframework.web.reactive.function.client.ClientResponse response, String resourceType) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("❌ Server error (5xx) for {}: Status={}, Body={}",
                            resourceType, response.statusCode(), errorBody);
                    return Mono.error(new ExternalApiException(
                            String.format("Server error %s: %s", response.statusCode(), errorBody),
                            response.statusCode().value()
                    ));
                });
    }

    private boolean isRetryable(Throwable throwable) {
        // Retry on network issues and 5xx errors, but not on 4xx
        return throwable instanceof WebClientRequestException ||
                throwable instanceof java.net.ConnectException ||
                throwable instanceof java.net.SocketTimeoutException ||
                (throwable instanceof WebClientResponseException &&
                        ((WebClientResponseException) throwable).getStatusCode().is5xxServerError());
    }

    private Mono<LatestRatesResponse> handleFallback(Throwable error) {
        log.warn("⚠️ Using fallback for latest rates due to: {}", error.getMessage());

        // Fallback response dengan data default atau kosong
        LatestRatesResponse fallback = LatestRatesResponse.builder()
                .base("IDR")
                .resourceType(getResourceType())
                .rates(Map.of("USD", BigDecimal.ZERO))
                .USD_BuySpread_IDR(BigDecimal.ZERO)
                .build();

        return Mono.just(fallback);
    }

    private LatestRatesResponse enrichWithSpreadCalculation(LatestRatesResponse response) {
        if (response != null && response.getRates() != null && response.getRates().containsKey("USD")) {
            try {
                BigDecimal usdRate = response.getRates().get("USD");
                BigDecimal spreadFactor = calculateSpreadFactor();

                BigDecimal buySpreadRate = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.ONE.add(spreadFactor))
                        .setScale(5, RoundingMode.HALF_UP);

                response.setUSD_BuySpread_IDR(buySpreadRate);
                log.debug("USD Rate: {}, Spread Factor: {}, Buy Spread Rate: {}",
                        usdRate, spreadFactor, buySpreadRate);
            } catch (Exception e) {
                log.error("Error calculating spread: {}", e.getMessage());
                response.setUSD_BuySpread_IDR(BigDecimal.ZERO);
            }
        }

        if (response != null) {
            response.setResourceType(getResourceType());
        }
        return response;
    }

    private BigDecimal calculateSpreadFactor() {
        try {
            String username = githubUsername.toLowerCase();
            int sum = username.chars().sum();
            double factor = (sum % 1000) / 100000.0;
            return BigDecimal.valueOf(factor).setScale(5, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Error calculating spread factor, using default: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Class<?> getResponseType() {
        return LatestRatesResponse.class;
    }
}