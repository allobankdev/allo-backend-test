package id.co.allobank.exchangerate.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String responseCode;

    public CustomException(String message, HttpStatus httpStatus, String responseCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
    }

    public CustomException(String message, Throwable cause, HttpStatus httpStatus, String responseCode) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
    }
}