package com.allobank.allobackendtest.common.exception;

import com.allobank.allobackendtest.common.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * GlobalExceptionHandler class for handling exceptions globally.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //handle bad request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    //handle not found resource
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .message(getMessageError(ex))
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    //Handle Unique Constraint Violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = extractMessageUniqueViolation(ex);

        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .status(HttpStatus.CONFLICT.value()) // 409
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // fallback general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Get root cause message from throwable
    private String getRootCauseMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage();
    }

    // Get message error from MethodArgumentNotValidException
    private static String getMessageError(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .toList();

        if (errors.size() == 1) {
            return errors.get(0);
        } else {
            return String.join("; ", errors);
        }
    }

    // Get message error from DataIntegrityViolationException
    private static String extractMessageUniqueViolation(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();

        // cari kata "already exists"
        if (message != null && message.contains("already exists")) {
            message = message.substring(message.indexOf("already exists"));
        } else {
            message = "Duplicate entry already exists";
        }
        return message;
    }

}
