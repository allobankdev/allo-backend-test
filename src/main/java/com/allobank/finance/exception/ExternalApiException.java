package com.allobank.finance.exception;

/**
 * Exception yang dilempar ketika terjadi kegagalan saat mengambil data
 * dari Frankfurter API (network error, 4xx, 5xx).
 */
public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
