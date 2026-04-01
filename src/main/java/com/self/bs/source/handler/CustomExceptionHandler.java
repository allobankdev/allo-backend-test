package com.self.bs.source.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.self.bs.source.dto.response.ResponseDto;
import com.self.bs.source.exception.ExchangeRateException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ExchangeRateException.class)
    public ResponseEntity<Object> businessException(ExchangeRateException ex){
        return ResponseEntity.badRequest().body(new ResponseDto<>(ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = String.format("field '%s' is mandatory", ex.getParameterName());

        return ResponseEntity.badRequest().body(new ResponseDto<>(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generalException(Exception ex){
        return ResponseEntity.internalServerError().body(new ResponseDto<>("Internal server error"));
    }
}
