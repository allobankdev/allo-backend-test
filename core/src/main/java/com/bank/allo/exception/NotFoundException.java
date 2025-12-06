package com.bank.allo.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }

    public static NotFoundException of(String value) {
        return new NotFoundException(value);
    }

    public static NotFoundException of(String messageFormat, Object... args) {
        return new NotFoundException(messageFormat, args);
    }
}
