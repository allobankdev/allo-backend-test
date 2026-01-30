package com.example.allobank.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Tangkap semua error HTTP dari API luar (404, 405, 500, dll)
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException ex) {

        HttpStatus status = (HttpStatus) ex.getStatusCode(); // status asli dari API luar
        String responseBody = ex.getResponseBodyAsString();

        return ResponseEntity
                .status(status)
                .body(responseBody);
    }

    // 🔹 Timeout / koneksi gagal
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleConnectionError(ResourceAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body("External API tidak bisa diakses: " + ex.getMessage());
    }

    // 🔹 Error lain (500 internal kita sendiri)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Terjadi kesalahan pada server: " + ex.getMessage());
    }
}
