package com.allobank.allobackendtest.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.allobank.allobackendtest.exception.*;
import com.allobank.allobackendtest.config.RCProp;
import com.allobank.allobackendtest.dto.Response;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @Autowired
    private RCProp rcProp;

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Response> exception(RuntimeException e) {
        log.error("RuntimeException: ", e);
        String code = "500";
        String message = rcProp.message(code);
        Response response = new Response(code, message, e.getLocalizedMessage(), "APP", null);
        log.info("[END] ExceptionController - RuntimteException \n");
        return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<Response> exception(GlobalException e) {
        log.error("GlobalException: ", e);
        log.info("Code: " + e.getCode() + "; StackTrace: " + e.getLocalizedMessage());
        String message = rcProp.message(e.getCode());
        String code = e.getCode();
        HttpStatus httpStatus = e.getHttpStatus();
        if (message == null) {
            code = "500";
            message = rcProp.message(code);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Response response = new Response(code, message, null, "APP", null);
        log.info("[END] ExceptionController - GlobalException \n");
        return new ResponseEntity<Response>(response, httpStatus);
    }

}
