package id.co.microservice.currency.currency_service.exception;

import org.springframework.http.HttpStatus;

public class CurrencyException extends RuntimeException {
    private final HttpStatus status;

    public CurrencyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CurrencyException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public CurrencyException(HttpStatus status) {
        this(status.getReasonPhrase(), status);
    }

    public HttpStatus getStatus() {
        return status;
    }

}
