package co.id.allobank.finance.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorHandlingProperties props;

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(ServiceException ex) {
        String errorCode = buildServiceErrorCode(ex.getErrorCode().getCode());

        ErrorResponse body = new ErrorResponse(
                errorCode,
                ex.getErrorCode().getMessage(),
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        var mapping = resolveMapping(ex);

        String errorCode = buildCommonErrorCode(mapping.errorCode());

        ErrorResponse body = new ErrorResponse(
                errorCode,
                mapping.errorDesc(),
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        return ResponseEntity
                .status(HttpStatus.valueOf(mapping.status()))
                .body(body);
    }

    private ErrorHandlingProperties.Mapping resolveMapping(Exception ex) {
        var mappings = props.common().mappings();

        return mappings.getOrDefault(
                ex.getClass().getName(),
                mappings.get("java.lang.Exception")
        );
    }

    private String buildServiceErrorCode(int errorCode) {
        return String.format(
                "%s-%03d-%03d",
                props.prefix(),
                props.serviceCode(),
                errorCode
        );
    }

    private String buildCommonErrorCode(int errorCode) {
        return String.format(
                "%s-%03d-%03d",
                props.common().prefix(),
                props.common().serviceCode(),
                errorCode
        );
    }
}
