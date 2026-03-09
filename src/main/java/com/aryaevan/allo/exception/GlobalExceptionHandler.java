package com.aryaevan.allo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API.
 * Provides graceful error handling for network failures and API errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handles WebClient response exceptions (4xx, 5xx errors from external API).
     * 
     * @param ex The WebClientResponseException
     * @param request The WebRequest
     * @return Error response with details
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientResponseException(
            WebClientResponseException ex, 
            WebRequest request) {
        
        logger.error("External API error: {} - {}", ex.getStatusCode(), ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                "External API Error",
                "Failed to fetch data from Frankfurter API: " + ex.getStatusCode(),
                ex.getStatusCode().value()
        );
        
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }
    
    /**
     * Handles illegal argument exceptions (invalid resource type).
     * 
     * @param ex The IllegalArgumentException
     * @param request The WebRequest
     * @return Error response with details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex, 
            WebRequest request) {
        
        logger.warn("Invalid argument: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                "Invalid Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * Handles illegal state exceptions (data store not initialized).
     * 
     * @param ex The IllegalStateException
     * @param request The WebRequest
     * @return Error response with details
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(
            IllegalStateException ex, 
            WebRequest request) {
        
        logger.error("Illegal state: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                "Service Unavailable",
                "Application is not ready to serve requests. " + ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }
    
    /**
     * Handles all other exceptions.
     * 
     * @param ex The Exception
     * @param request The WebRequest
     * @return Error response with details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
            Exception ex, 
            WebRequest request) {
        
        logger.error("Unexpected error: ", ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    
    /**
     * Creates a standardized error response.
     * 
     * @param title The error title
     * @param message The error message
     * @param status The HTTP status code
     * @return Map containing error details
     */
    private Map<String, Object> createErrorResponse(String title, String message, int status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", title);
        response.put("message", message);
        response.put("status", status);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
