package com.allobank.finance.exception;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalException(ExternalApiException e) {

        String traceId = MDC.get("traceId");

        return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.of(
                "timestamp", Instant.now(),
                "traceId", traceId,
                "error", e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral() {

        String traceId = MDC.get("traceId");

        return ResponseEntity
                .internalServerError()
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "traceId", traceId,
                        "error", "Unexpected error"
                ));
    }
}
