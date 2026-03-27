package com.allobank.allo_backend_test.finance.advice;

import com.allobank.allo_backend_test.finance.exception.BadRequestException;
import com.allobank.allo_backend_test.finance.exception.ResourceNotSupportedException;
import com.allobank.allo_backend_test.finance.exception.ServiceUnavailableException;
import com.allobank.allo_backend_test.finance.exception.ResourceNotFoundException;

import com.allobank.allo_backend_test.finance.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return new ErrorResponse(400, ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotSupported(ResourceNotSupportedException ex, HttpServletRequest request) {
        return new ErrorResponse(404, ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler({ResourceNotFoundException.class, ServiceUnavailableException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleUnavailable(RuntimeException ex, HttpServletRequest request) {
        return new ErrorResponse(503, ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex, HttpServletRequest request) {
        return new ErrorResponse(500, "Internal server error", request.getRequestURI(), LocalDateTime.now());
    }
}