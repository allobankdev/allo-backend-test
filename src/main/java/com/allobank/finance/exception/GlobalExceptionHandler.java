package com.allobank.finance.exception;

import com.allobank.finance.model.response.ErrorResponse;
import com.allobank.finance.util.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleService(ServiceException ex) {
        ErrorCode error = ex.getErrorCode();

        ErrorResponse body = new ErrorResponse(
                error.getCode(),
                error.getMessage(),
                ZonedDateTime.now()
        );

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInput(IllegalArgumentException ex) {
        return build(ErrorCode.INVALID_INPUT, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
        return build(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return build(ErrorCode.GENERAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> build(ErrorCode error, HttpStatus status) {
        ErrorResponse body = new ErrorResponse(
                error.getCode(),
                error.getMessage(),
                ZonedDateTime.now()
        );

        return ResponseEntity.status(status).body(body);
    }
}
