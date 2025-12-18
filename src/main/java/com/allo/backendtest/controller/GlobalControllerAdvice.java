package com.allo.backendtest.controller;

import com.allo.backendtest.dto.ErrorResponse;
import com.allo.backendtest.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(HttpException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getErrorCode()));
    }

}
