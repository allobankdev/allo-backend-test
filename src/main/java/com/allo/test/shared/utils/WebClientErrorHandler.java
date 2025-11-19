package com.allo.test.shared.utils;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.modules.finance.exceptions.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Utility class for mapping WebClient errors to custom domain exceptions.
 * Used with .onErrorMap() in reactive chains for clean error handling.
 * <p>
 * This eliminates code duplication across all strategy implementations.
 */
@Slf4j
@UtilityClass
public class WebClientErrorHandler {

    /**
     * Maps WebClient exceptions to custom domain exceptions.
     *
     * @param error the original error from WebClient
     * @param endpoint the API endpoint that was called
     * @return mapped custom exception
     */
    public static Throwable mapException(Throwable error, String endpoint) {
        // Already mapped - pass through
        if (error instanceof ExternalApiException) {
            return error;
        }

        // HTTP 4xx - Client errors (bad request, not found, etc.)
        if (error instanceof WebClientResponseException.BadRequest ||
            error instanceof WebClientResponseException.NotFound) {
            log.error("Client error from API [{}]: {}",
                ((WebClientResponseException) error).getStatusCode(),
                error.getMessage());
            return new ClientException(endpoint);
        }

        // HTTP 5xx - Server errors
        if (error instanceof WebClientResponseException ex) {
            log.error("Server error from API [{}]: {}", ex.getStatusCode(), error.getMessage());
            return new ServerException(endpoint);
        }

        // Network/connectivity errors (timeout, connection refused, etc.)
        if (error instanceof WebClientRequestException) {
            log.error("Network connectivity error calling {}: {}", endpoint, error.getMessage());
            return new ConnectivityException(endpoint);
        }

        // Unexpected errors (parsing, runtime, etc.)
        log.error("Unexpected error calling {}: {}", endpoint, error.getMessage(), error);
        return new ResponseParsingException(endpoint);
    }
}
