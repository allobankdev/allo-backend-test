package com.bank.allo.rest.controller;

import com.bank.allo.exception.BadRequestException;
import com.bank.allo.exception.NotFoundException;
import com.bank.allo.rest.entity.ApiResponse;
import com.bank.allo.rest.entity.ApiResponse.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // =====================================================
    //               CUSTOM APPLICATION ERRORS
    // =====================================================

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        log.warn("BadRequestException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), Source.APPLICATION);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        log.warn("NotFoundException: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), Source.APPLICATION);
    }


    // =====================================================
    //               VALIDATION / INPUT ERRORS
    // =====================================================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Invalid value for '%s': expected %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );

        log.warn("TypeMismatch: {}", msg);
        return build(HttpStatus.BAD_REQUEST, msg, Source.APPLICATION);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        log.warn("MissingRequestParameter: {}", msg);
        return build(HttpStatus.BAD_REQUEST, msg, Source.APPLICATION);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingPath(MissingPathVariableException ex) {
        String msg = "Missing path variable: " + ex.getVariableName();
        log.warn("MissingPathVariable: {}", msg);
        return build(HttpStatus.BAD_REQUEST, msg, Source.APPLICATION);
    }


    // =====================================================
    //               FALLBACK UNHANDLED ERROR
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error occurred",
                Source.UNKNOWN
        );
    }


    // =====================================================
    //               RESPONSE WRAPPER
    // =====================================================

    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String msg, Source source) {
        ApiResponse<Void> resp = ApiResponse.defaultBuilder(
                status.value(),
                msg,
                source,
                null
        );
        return new ResponseEntity<>(resp, status);
    }
}
