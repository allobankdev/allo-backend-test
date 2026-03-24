package com.allobank.test.exception;

import com.allobank.test.service.FinanceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final FinanceDataService financeDataService;

    public GlobalExceptionHandler(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException exception) {
        return Map.of(
                "error", "Bad Request",
                "message", exception.getMessage(),
                "supportedResourceTypes", financeDataService.supportedResourceTypes()
        );
    }
}
