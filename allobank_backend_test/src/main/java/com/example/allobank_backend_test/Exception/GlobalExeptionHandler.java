package com.example.allobank_backend_test.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExeptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception exception) {
        return ResponseEntity.status(500).body(exception.getMessage());
    }
}
