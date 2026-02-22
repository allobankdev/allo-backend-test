package io.aditsukoco.allobank_test.exceptions;

public class InternalServerErrorRestException extends BaseRestException {

    public InternalServerErrorRestException() {
        this.httpCode = 500;
        this.message = "Bad Request";
    }

    public InternalServerErrorRestException(String message) {
        this.httpCode = 500;
        this.message = message;
    }
}
