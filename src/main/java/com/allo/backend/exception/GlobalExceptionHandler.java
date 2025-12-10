package com.allo.backend.exception;

import com.allo.backend.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.error(
                "Resource not found",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleExternalApiException(
            ExternalApiException ex, WebRequest request) {
        log.error("External API error: {}", ex.getMessage(), ex);
        ApiResponse<Object> response = ApiResponse.error(
                "External service error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataInitializationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataInitializationException(
            DataInitializationException ex, WebRequest request) {
        log.error("Data initialization error: {}", ex.getMessage(), ex);
        ApiResponse<Object> response = ApiResponse.error(
                "Data initialization failed",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ApiResponse<Object> response = ApiResponse.error(
                "Internal server error",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
