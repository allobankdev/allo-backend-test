package io.aditsukoco.allobank_test.exceptions;

public class BadRequestRestException extends BaseRestException {

    public BadRequestRestException() {
        this.httpCode = 400;
        this.message = "Bad Request";
    }

    public BadRequestRestException(String message) {
        this.httpCode = 400;
        this.message = message;
    }
}
