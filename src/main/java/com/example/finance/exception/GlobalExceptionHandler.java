package com.example.finance.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApi(ExternalApiException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGeneral(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}