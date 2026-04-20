package id.allobank.exchangerate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApi(ApiException ex) {
        log.error("API Exception: {}", ex.getMessage());

        return ResponseEntity
                .status(ex.getStatus())
                .body(buildError(ex.getMessage(), ex.getStatus().value()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientResponse(WebClientResponseException ex) {
        log.error("External API response exception: status={}, message={}", ex.getStatusCode(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(buildError("External API returned invalid response", HttpStatus.BAD_GATEWAY.value()));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<?> handleWebClientRequest(WebClientRequestException ex) {
        log.error("External API request exception: {}", ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof TimeoutException) {
            return ResponseEntity
                    .status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(buildError("Timeout when connecting to external API", HttpStatus.GATEWAY_TIMEOUT.value()));
        }

        if (cause instanceof ConnectException || cause instanceof UnknownHostException) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(buildError("Failed to connect to external API", HttpStatus.SERVICE_UNAVAILABLE.value()));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(buildError("External API request failed", HttpStatus.BAD_GATEWAY.value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private Map<String, Object> buildError(String message, int status) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status,
                "error", message
        );
    }
}
