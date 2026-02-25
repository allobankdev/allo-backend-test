package com.allobank.finance.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Todo : global exception handler
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
                log.warn("Resource tidak ditemukan: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                                "status", "error",
                                                "httpCode", 404,
                                                "message", ex.getMessage()));
        }

        @ExceptionHandler(ExternalApiException.class)
        public ResponseEntity<Map<String, Object>> handleExternalApiError(ExternalApiException ex) {
                log.error("Kesalahan komunikasi dengan API eksternal: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(Map.of(
                                                "status", "error",
                                                "httpCode", 503,
                                                "message", "Layanan eksternal tidak tersedia: " + ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
                log.error("Kesalahan tidak tertangani: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of(
                                                "status", "error",
                                                "httpCode", 500,
                                                "message", "Terjadi kesalahan internal server"));
        }
}
