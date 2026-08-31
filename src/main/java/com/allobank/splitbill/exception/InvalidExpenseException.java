package com.allobank.splitbill.exception;

public class InvalidExpenseException extends RuntimeException {
    public InvalidExpenseException(String message) {
        super(message);
    }
}
