package com.idr_rate_aggregator_2.demo.Error;

import com.idr_rate_aggregator_2.demo.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiErrorResponse> handleExternalApiException(ExternalApiException ex) {
        log.error("External API error: {}", ex.getMessage(), ex);

        HttpStatus status = determineHttpStatus(ex.getStatusCode());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .path("/api/finance/data")
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .path("/api/finance/data")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .path("/api/finance/data")
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred. Please try again later.")
                .timestamp(System.currentTimeMillis())
                .path("/api/finance/data")
                .build();

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    private HttpStatus determineHttpStatus(int statusCode) {
        if (statusCode >= 400 && statusCode < 500) {
            return HttpStatus.valueOf(statusCode);
        } else if (statusCode >= 500) {
            return HttpStatus.BAD_GATEWAY;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}