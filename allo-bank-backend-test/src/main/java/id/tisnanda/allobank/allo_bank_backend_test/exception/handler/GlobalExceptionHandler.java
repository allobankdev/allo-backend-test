package id.tisnanda.allobank.allo_bank_backend_test.exception.handler;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
import id.tisnanda.allobank.allo_bank_backend_test.dto.ErrorResponse;
import id.tisnanda.allobank.allo_bank_backend_test.exception.AlloBankException;
import id.tisnanda.allobank.allo_bank_backend_test.exception.ErrorCodes;
import id.tisnanda.allobank.allo_bank_backend_test.util.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jboss.logging.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);
    private static final Logger alloBankLog = Logger.getLogger(AlloBankException.class);

    @ExceptionHandler(AlloBankException.class)
    public ResponseEntity<ErrorResponse> handleAlloBankException(AlloBankException ex, HttpServletRequest request) {
        ErrorCodes code = ex.getErrorCodes();

        alloBankLog.errorf("Handled exception occurred: %s - %s, path=%s",

                code.name(),
                ex.getMessage(),
                request.getRequestURI()
        );

        ErrorResponse response = new ErrorResponse(
                String.valueOf(code.getHttpStatus()),
                ex.getMessage(),
                code.name(),
                request.getRequestURI(),
                DateUtils.formatLocalDateTime(LocalDateTime.now())
        );

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {

        StackTraceElement element = ex.getStackTrace()[0];
        log.errorf(ex,
                "Unhandled exception: class=%s, method=%s, exception=%s, message=%s, path=%s",
                element.getClassName(),
                element.getMethodName(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        );

        ErrorResponse response = new ErrorResponse(
                Constant.INTERNAL_SERVER_ERROR_CODE,
                ex.getMessage() != null ? ex.getMessage() : Constant.UNEXPECTED_ERROR,
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                DateUtils.formatLocalDateTime(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
