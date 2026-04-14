package com.allobank.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.allobank.finance.service.FinanceDataService;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final FinanceDataService financeDataService;

    public GlobalExceptionHandler(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @ExceptionHandler(ResourceTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleResourceTypeNotSupported(ResourceTypeNotSupportedException exception) {
        return Map.of(
                "error", "Bad Request",
                "message", exception.getMessage(),
                "supportedResourceTypes", financeDataService.supportedResourceTypes());
    }

    @ExceptionHandler(DataNotInitializedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> handleDataNotInitialized(DataNotInitializedException exception) {
        return Map.of(
                "error", "Service Unavailable",
                "message", exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException exception) {
        return Map.of(
                "error", "Bad Request",
                "message", exception.getMessage());
    }
}
