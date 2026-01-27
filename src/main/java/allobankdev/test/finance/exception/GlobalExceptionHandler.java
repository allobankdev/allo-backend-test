package allobankdev.test.finance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidResourceTypeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidResource(
            InvalidResourceTypeException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "INVALID_RESOURCE_TYPE",
                        "message", ex.getMessage()
                ));
    }
}