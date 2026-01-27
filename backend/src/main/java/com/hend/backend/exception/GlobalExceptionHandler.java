package com.hend.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

/**
 * @author : hend wunga
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 404,
                        "error", "NOT_FOUND",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApi(ExternalApiException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 502,
                        "error", "EXTERNAL_API_ERROR",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 500,
                        "error", "INTERNAL_SERVER_ERROR",
                        "message", ex.getMessage()
                )
        );
    }
}



