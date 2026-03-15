package com.allobank.allobackendtest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidResource(IllegalArgumentException ex) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", ex.getMessage(),
                        "status", 400
                ));
    }
}

