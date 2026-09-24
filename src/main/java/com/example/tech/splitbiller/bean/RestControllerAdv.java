package com.example.tech.splitbiller.bean;

import com.example.tech.splitbiller.exception.CaughtException;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerAdv {

    @ExceptionHandler(CaughtException.class)
    public ResponseEntity<BaseResponse<String>> handleCaughtException(CaughtException e) {
        return ResponseEntity.status(HttpStatus.valueOf(e.getCode()))
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(e.getMessage()).data(null).build());
    }

    /* Handling Mismatch Request Parameter */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String parameterType = ex.getParameter().getParameterType().getSimpleName();
        String type = "";
        if(parameterType.equalsIgnoreCase("Long") ||
                parameterType.equals("Integer") ||
                parameterType.equalsIgnoreCase("Double") ||
                parameterType.equals("int")) {
            type =  "number";
        }else if(parameterType.equals("String")) {
            type = "string";
        }
        String message = String.format("Request parameter %s should be %s", ex.getParameter().getParameterName(), type);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Missing Request Parameter */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingRequestParameterException(MissingServletRequestParameterException ex) {
        String message = String.format("Request parameter %s is required", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Missing Request Header Parameter */
    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        String message = String.format("Request header %s is required", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Missing Path Variable Parameter */
    @ExceptionHandler(value = MissingPathVariableException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingPathVariableException(MissingPathVariableException ex) {
        String message = String.format("Path variable %s is required", ex.getVariableName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Validation JSON */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleJSONValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ", "", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Validation Request Parameter */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleRequestParameterValidationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ", "", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling JSON Mapping Error */
    @ExceptionHandler(value = JsonParseException.class)
    public ResponseEntity<BaseResponse<String>> handleJSONParseException(JsonParseException ex) {
        String message = "JSON body is invalid, error at converting JSON";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Http Method Error */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        if(!Objects.isNull(ex.getSupportedHttpMethods())) {
            ex.getSupportedHttpMethods().forEach(httpMethod -> joiner.add(httpMethod.name()));
        }
        String message = String.format("This end point only supported http method %s", joiner);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    /* Handling Resource Not Found */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleNoResourceException(NoHandlerFoundException ex) {
        String message = String.format("Path '%s' with '%s' method is not found", ex.getRequestURL(), ex.getHttpMethod());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(message).data(null).build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(ex.getMessage()).data(null).build());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse<String>> handleUnCaughtException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.<String>builder().success(Boolean.FALSE).message(ex.getMessage()).data(null).build());
    }
}
