package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.dto.HistoricalRate;
import com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.core.Exceptions;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HistoricalRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Autowired
    public HistoricalRatesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Mono<List<HistoricalRate>> fetchData() {
        log.info("Fetching historical IDR-USD rates from Frankfurter API");

        // Periode yang diminta soal: 1-5 Januari 2024
        String dateRange = "2024-01-01..2024-01-05";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + dateRange)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(this::transformToHistoricalRates)
                .doOnSuccess(response -> log.info("Successfully fetched {} historical rates for period {}",
                        response != null ? response.size() : 0, dateRange))
                .onErrorResume(this::handleNetworkError);
    }

    /**
     * Handle network-related errors only
     * Business errors (4xx, 5xx) akan tetap throw exception
     */
    private Mono<List<HistoricalRate>> handleNetworkError(Throwable error) {
        // Cek apakah ini network error
        if (isNetworkError(error)) {
            log.error("Network error occurred while fetching historical rates: {}", error.getMessage());

            // Return empty list untuk network error
            return Mono.just(Collections.emptyList());
        }

        // Jika bukan network error, lanjutkan error propagation
        log.error("Non-network error occurred: {}", error.getMessage());
        return Mono.error(error);
    }

    /**
     * Deteksi apakah error termasuk network error
     */
    private boolean isNetworkError(Throwable error) {
        // Unwrap penyebab error jika perlu
        Throwable cause = Exceptions.unwrap(error);

        // Cek berbagai tipe network error
        return cause instanceof WebClientRequestException ||
                cause instanceof ConnectException ||
                cause instanceof UnknownHostException ||
                cause instanceof SocketTimeoutException ||
                isConnectionRefused(cause) ||
                isNetworkUnreachable(cause);
    }

    private boolean isConnectionRefused(Throwable error) {
        return error.getMessage() != null &&
                error.getMessage().toLowerCase().contains("connection refused");
    }

    private boolean isNetworkUnreachable(Throwable error) {
        return error.getMessage() != null &&
                error.getMessage().toLowerCase().contains("network is unreachable");
    }

    /**
     * Transform response dari API Frankfurter ke List<HistoricalRate>
     *
     * Response API format:
     * {
     *   "rates": {
     *     "2024-01-01": {"USD": 0.000064},
     *     "2024-01-02": {"USD": 0.000065},
     *     "2024-01-03": {"USD": 0.000063},
     *     "2024-01-04": {"USD": 0.000064},
     *     "2024-01-05": {"USD": 0.000066}
     *   }
     * }
     */
    @SuppressWarnings("unchecked")
    private List<HistoricalRate> transformToHistoricalRates(Map<String, Object> response) {
        if (response == null || response.isEmpty()) {
            log.warn("Empty response from API");
            return Collections.emptyList();
        }

        // Extract rates object dari response dengan type safety
        Object ratesObj = response.get("rates");
        if (ratesObj == null) {
            log.warn("No rates data found in response");
            return Collections.emptyList();
        }

        try {
            Map<String, Map<String, Double>> rates = (Map<String, Map<String, Double>>) ratesObj;

            if (rates.isEmpty()) {
                log.warn("Rates object is empty");
                return Collections.emptyList();
            }

            log.debug("Processing {} days of historical data", rates.size());

            return rates.entrySet().stream()
                    .map(entry -> {
                        String dateStr = entry.getKey();
                        Map<String, Double> dailyRates = entry.getValue();

                        if (dailyRates == null || dailyRates.isEmpty()) {
                            log.warn("No rates for date: {}", dateStr);
                            return null;
                        }

                        Double usdRate = dailyRates.get("USD");
                        if (usdRate == null) {
                            log.warn("USD rate not found for date: {}", dateStr);
                            return null;
                        }

                        try {
                            return HistoricalRate.builder()
                                    .date(LocalDate.parse(dateStr))
                                    .usdRate(BigDecimal.valueOf(usdRate))
                                    .resourceType(getResourceType())
                                    .build();
                        } catch (Exception e) {
                            log.warn("Error parsing date {} or rate {}: {}", dateStr, usdRate, e.getMessage());
                            return null;
                        }
                    })
                    .filter(rate -> rate != null)
                    .collect(Collectors.toList());

        } catch (ClassCastException e) {
            log.error("Error casting response data: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Fetch historical rates dengan parameter dinamis
     */
    public Mono<List<HistoricalRate>> fetchHistoricalRates(String startDate, String endDate) {
        log.info("Fetching historical IDR-USD rates from {} to {}", startDate, endDate);

        String dateRange = startDate + ".." + endDate;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + dateRange)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(this::transformToHistoricalRates)
                .doOnSuccess(response -> log.info("Successfully fetched {} historical rates for period {}",
                        response != null ? response.size() : 0, dateRange))
                .onErrorResume(this::handleNetworkError);
    }

    /**
     * Fetch historical rates dengan timeout handling
     */
    public Mono<List<HistoricalRate>> fetchDataWithTimeout() {
        return fetchData()
                .timeout(java.time.Duration.ofSeconds(10))
                .onErrorResume(error -> {
                    if (error instanceof java.util.concurrent.TimeoutException) {
                        log.error("Request timeout after 10 seconds");
                        return Mono.just(Collections.emptyList());
                    }
                    return handleNetworkError(error);
                });
    }

    /**
     * Fetch historical rates dengan fallback data (contoh data dummy)
     */
    public Mono<List<HistoricalRate>> fetchDataWithFallback() {
        return fetchData()
                .onErrorResume(error -> {
                    if (isNetworkError(error)) {
                        log.warn("Using fallback historical data due to network error: {}", error.getMessage());
                        return Mono.just(getFallbackHistoricalRates());
                    }
                    return Mono.error(error);
                });
    }

    /**
     * Fallback historical rates jika API tidak tersedia
     */
    private List<HistoricalRate> getFallbackHistoricalRates() {
        return List.of(
                createHistoricalRate("2024-01-01", 0.000064),
                createHistoricalRate("2024-01-02", 0.000065),
                createHistoricalRate("2024-01-03", 0.000063),
                createHistoricalRate("2024-01-04", 0.000064),
                createHistoricalRate("2024-01-05", 0.000066)
        );
    }

    private HistoricalRate createHistoricalRate(String date, double rate) {
        return HistoricalRate.builder()
                .date(LocalDate.parse(date))
                .usdRate(BigDecimal.valueOf(rate))
                .resourceType(getResourceType())
                .build();
    }

    @Override
    public Class<?> getResponseType() {
        return List.class;
    }

    /**
     * Custom exception untuk network error (opsional)
     */
    public static class NetworkException extends RuntimeException {
        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}