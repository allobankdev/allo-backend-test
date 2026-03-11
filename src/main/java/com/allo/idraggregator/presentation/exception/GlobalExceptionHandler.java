package com.allo.idraggregator.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.allo.idraggregator.presentation.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceTypeNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<ApiResponse<String>> handleResourceNotFound(
                        ResourceTypeNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.response(
                                                HttpStatus.NOT_FOUND.value(),
                                                ex.getMessage(),
                                                null));
        }

        @ExceptionHandler(ExternalApiException.class)
        @ResponseStatus(HttpStatus.BAD_GATEWAY)
        public ResponseEntity<ApiResponse<String>> handleExternalApi(
                        ExternalApiException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body(ApiResponse.response(
                                                HttpStatus.BAD_GATEWAY.value(),
                                                ex.getMessage(),
                                                null));
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<ApiResponse<String>> handleGeneralException(
                        Exception ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.response(
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ex.getMessage(),
                                                null));
        }
}
