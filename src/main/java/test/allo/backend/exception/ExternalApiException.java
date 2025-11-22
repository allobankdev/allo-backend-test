package test.allo.backend.exception;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final int status;
    private final String error;

    public ExternalApiException(int status, String error, String message) {
        super(message);
        this.error = error;
        this.status = status;
    }
}
