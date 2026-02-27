package com.allobank.finance.exception.handler;

import com.allobank.finance.dto.error.ErrorResponseDto;
import com.allobank.finance.exception.ResourceTypeNotFoundException;
import com.allobank.finance.exception.WebClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceTypeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceTypeNotFoundException(
            ResourceTypeNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<ErrorResponseDto> handleWebClientException(
            WebClientException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(ex.getStatus())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}