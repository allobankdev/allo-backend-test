package com.example.allotest.exception;

import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleException(Exception ex) {
        return Map.of("error", ex.getMessage());
    }
}
