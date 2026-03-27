package com.allobank.allo_backend_test.finance.advice;

import com.allobank.allo_backend_test.finance.exception.AppException;
import com.allobank.allo_backend_test.finance.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleCustom(AppException ex, HttpServletRequest request) {
        log.warn("method={}, path={}, error={}, status={}, message={}",
                request.getMethod(), request.getRequestURI(),
                ex.getError(), ex.getStatus().value(), ex.getMessage());

        return new ErrorResponse(ex.getStatus().value(), ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handle(Exception ex, HttpServletRequest request) {
        log.error("method={}, path={}, error={}, status={}, message={}",
                request.getMethod(), request.getRequestURI(),
                "internal server error", 500, ex.getMessage(), ex);

        return new ErrorResponse(500, "Internal server error", request.getRequestURI(), LocalDateTime.now());
    }
}