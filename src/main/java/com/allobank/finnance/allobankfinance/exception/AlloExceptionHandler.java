package com.allobank.finnance.allobankfinance.exception;

import com.allobank.finnance.allobankfinance.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class AlloExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {FrankfurterException.class})
    public ResponseEntity<Object> dataNotFoundException(FrankfurterException e) {
        BaseResponse errorDetail = BaseResponse.builder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorDetail, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> notificationNotFoundException(DataNotFoundException e) {
        BaseResponse errorDetail = BaseResponse.builder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
}
