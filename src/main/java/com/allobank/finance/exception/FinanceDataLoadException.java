package com.allobank.finance.exception;

public class FinanceDataLoadException extends RuntimeException {
    public FinanceDataLoadException(String message) {
        super(message);
    }

    public FinanceDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
