package id.tisnanda.allobank.allo_bank_backend_test.exception;

public class BadRequestException extends AlloBankException {
    public BadRequestException(String message) {
        super(message , ErrorCodes.INVALID_REQUEST);
    }
}
