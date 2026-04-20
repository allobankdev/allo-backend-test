package id.allobank.exchangerate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(ex.getMessage(), 500));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Unexpected error", 500));
    }

    private Map<String, Object> buildError(String message, int status) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status,
                "error", message
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApi(ApiException ex) {
        log.error("API Exception: {}", ex.getMessage());

        return ResponseEntity
                .status(ex.getStatus())
                .body(buildError(ex.getMessage(), ex.getStatus().value()));
    }
}