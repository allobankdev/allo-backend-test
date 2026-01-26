package com.example.AlloBank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Kalau resourceType tidak dikenal
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Error 4xx dari Frankfurter API
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleClientError(HttpClientErrorException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY,
                "External API client error: " + ex.getStatusCode());
    }

    // Error 5xx dari Frankfurter API
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleServerError(HttpServerErrorException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY,
                "External API server error: " + ex.getStatusCode());
    }

    // Network error / timeout
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleConnectionError(ResourceAccessException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "Failed to access external API");
    }

    // Fallback (error lain-lain)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error");
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternalService(ExternalServiceException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

}
