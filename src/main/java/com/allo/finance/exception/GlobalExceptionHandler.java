package com.allo.finance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        return ResponseEntity.internalServerError().body(
                Map.of(
                        "error", "Internal Server Error",
                        "message", ex.getMessage()
                )
        );
    }
}