package com.amri.apiintegration.exception;

import com.amri.apiintegration.util.IResultDTO;
import com.amri.apiintegration.util.ResponseBuilderAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExternalApiException.class)
    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    public IResultDTO<Object> externalApi(ExternalApiException e) {
        return ResponseBuilderAPI.error(HttpStatus.BAD_GATEWAY.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public IResultDTO<Object> handle(HttpServletRequest req, Exception e) {
        return ResponseBuilderAPI.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public IResultDTO<Object> resourceNotFound(ResourceNotFoundException e, WebRequest request) {
        return ResponseBuilderAPI.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
