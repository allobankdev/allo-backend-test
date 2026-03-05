package com.allobank.finance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientError(
            WebClientResponseException ex) {
        log.severe("[GlobalExceptionHandler] External API error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "error", "External API error",
                "status", ex.getStatusCode().value(),
                "message", ex.getMessage(),
                "timestamp", Instant.now().toString()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "Bad request",
                "message", ex.getMessage(),
                "timestamp", Instant.now().toString()
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {
        log.severe("[GlobalExceptionHandler] Runtime error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Internal server error",
                "message", ex.getMessage(),
                "timestamp", Instant.now().toString()
        ));
    }
}