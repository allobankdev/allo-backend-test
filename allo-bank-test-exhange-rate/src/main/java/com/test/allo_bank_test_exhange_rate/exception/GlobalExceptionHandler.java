package com.test.allo_bank_test_exhange_rate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleException(Exception ex) {
        log.error("ResponseStatusException: ", ex);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseStatusException handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: ", ex);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
