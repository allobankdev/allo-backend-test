package com.allobank.backendtest.exception;

import com.allobank.backendtest.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handle(Exception ex) {
        // log.error("Unhandled", ex);
        return ResponseEntity.status(500).body(ApiResponse.failure("internal_error: " + ex.getMessage()));
    }
}
