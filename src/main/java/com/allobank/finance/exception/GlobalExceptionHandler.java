package com.allobank.finance.exception;

import com.allobank.finance.dto.ErrorResponse;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalException(ExternalApiException e) {

        return buildResponse(e.getStatusCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {

        return buildResponse(500, e.getMessage());
    }

    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResourceException(InvalidResourceException e) {
        return buildResponse(400, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(int status, String message) {
        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                MDC.get("traceId"),
                status,
                message
        );
        return ResponseEntity.status(status).body(error);
    }
}
