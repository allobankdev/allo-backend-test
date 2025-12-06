package id.tisnanda.allobank.allo_bank_backend_test.exception;

public class ResourceNotFoundException extends AlloBankException {
    public ResourceNotFoundException(String message) {
        super(message , ErrorCodes.RESOURCE_NOT_FOUND);
    }
}
