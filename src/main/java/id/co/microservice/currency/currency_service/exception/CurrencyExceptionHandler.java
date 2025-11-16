package id.co.microservice.currency.currency_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CurrencyExceptionHandler {

    @ExceptionHandler(CurrencyException.class)
    public ResponseEntity<ErrorResponse> handleFinanceException(CurrencyException ex, HttpServletRequest req) {
        var status = ex.getStatus();
        var body = new ErrorResponse(status.value(), status.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(status).body(body);
    }

}
