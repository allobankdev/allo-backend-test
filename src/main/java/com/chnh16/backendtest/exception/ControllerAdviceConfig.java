package com.chnh16.backendtest.exception;

import com.chnh16.backendtest.model.response.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceConfig {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonErrorResponse> commonException(CommonException ex) {
        return new ResponseEntity<>(new CommonErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponse> commonException(Exception ex) {
        return new ResponseEntity<>(new CommonErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
