package com.allo.backendtest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex) {
        return ResponseEntity.internalServerError()
                .body("Internal error: " + ex.getMessage());
    }

    @ExceptionHandler(UnknownResourceException.class)
    public ResponseEntity<String> handleUnknown(UnknownResourceException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}