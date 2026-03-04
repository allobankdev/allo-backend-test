package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.dto.CurrencyResponse;
import com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.core.Exceptions;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Autowired
    public CurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Mono<List<CurrencyResponse>> fetchData() {
        log.info("Fetching supported currencies from Frankfurter API");

        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .map(currencies -> {
                    // Cast explicit di sini
                    Map<String, String> currencyMap = (Map<String, String>) currencies;
                    return transformToCurrencyResponses(currencyMap);
                })
                .doOnSuccess(response -> log.info("Successfully fetched {} currencies", response.size()))
                .onErrorResume(this::handleNetworkError);
    }

    /**
     * Handle network-related errors only
     * Business errors (4xx, 5xx) akan tetap throw exception
     */
    private Mono<List<CurrencyResponse>> handleNetworkError(Throwable error) {
        // Cek apakah ini network error
        if (isNetworkError(error)) {
            log.error("Network error occurred: {}", error.getMessage());

            // Bisa return empty list atau error message yang friendly
            return Mono.just(Collections.emptyList());

            // Atau bisa juga return error dengan pesan yang lebih friendly
            // return Mono.error(new NetworkException("Gagal terhubung ke server currency", error));
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

    @SuppressWarnings("unchecked")
    private List<CurrencyResponse> transformToCurrencyResponses(Map<String, String> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            log.warn("Received empty currency data from API");
            return Collections.emptyList();
        }

        return currencies.entrySet().stream()
                .map(entry -> CurrencyResponse.builder()
                        .code(entry.getKey())
                        .name(entry.getValue())
                        .resourceType(getResourceType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Class getResponseType() {
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