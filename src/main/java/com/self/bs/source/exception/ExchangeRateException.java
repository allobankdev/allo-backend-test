package com.self.bs.source.exception;

public class ExchangeRateException extends RuntimeException{
    
    public static final String DATE_FROM_CANNOT_BE_AFTER_DATE_TO = "bs.exchange-rate.exception.001";

    public ExchangeRateException (String message) {
        super(message);
    }
}
