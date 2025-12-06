package id.tisnanda.allobank.allo_bank_backend_test.exception;

import lombok.Getter;

@Getter
public class AlloBankException extends RuntimeException {

    private final ErrorCodes errorCodes;

    public AlloBankException(String message, ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public AlloBankException(String message, Throwable cause, ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }
}
