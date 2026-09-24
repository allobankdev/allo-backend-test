package com.example.tech.splitbiller.exception;

public class TransactionConflictException extends CaughtException {
    public TransactionConflictException(String message) {
        super(message, 409);
    }
}
