package com.allo.bank.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.allo.bank.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceTypeNotFoundException exception,
                                                        ServletWebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequest().getRequestURI());
    }

    @ExceptionHandler({ExternalApiException.class, DataNotInitializedException.class})
    public ResponseEntity<ErrorResponse> handleServiceErrors(RuntimeException exception,
                                                             ServletWebRequest request) {
        return buildResponse(HttpStatus.BAD_GATEWAY, exception.getMessage(), request.getRequest().getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status)
            .body(new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path));
    }
}
