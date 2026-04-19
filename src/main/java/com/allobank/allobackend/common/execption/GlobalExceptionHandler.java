package com.allobank.allobackend.common.execption;

import com.allobank.allobackend.core.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDto> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                ResponseDto.builder()
                        .message("System Error: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDto.builder()
                        .message("An unexpected error occurred: " + ex.getMessage())
                        .build()
        );
    }

}
