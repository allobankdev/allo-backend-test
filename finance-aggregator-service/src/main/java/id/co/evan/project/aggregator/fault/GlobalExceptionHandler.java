package id.co.evan.project.aggregator.fault;

import id.co.evan.project.aggregator.model.response.ErrorResponse;
import id.co.evan.project.aggregator.util.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, ServerWebExchange exchange) {
        log.error("Exception caught: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

        var errorCode = ErrorCode.GENERAL_ERROR;

        if (ex instanceof ResourceNotFoundException) {
            errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        } else if (ex instanceof ConstraintViolationException) {
            errorCode = ErrorCode.INVALID_INPUT;
        }

        return buildResponse(errorCode, exchange);
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode, ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSXXX"));

        var errorBody = new ErrorResponse(
            errorCode.getCode(),
            errorCode.getDefaultMessage(),
            timestamp,
            path
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }
}