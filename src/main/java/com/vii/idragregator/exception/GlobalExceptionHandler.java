package com.vii.idragregator.exception;

import com.vii.idragregator.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 11:00 p.m
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error("Endpoint not found: " + ex.getRequestURL()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<BaseResponse<Object>> handleImmutableError(UnsupportedOperationException ex) {
        log.warn("Attempt to modify immutable storage: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.error("Storage is immutable and cannot be modified after startup."));
    }
}