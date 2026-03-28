package com.allobank.allo_backend_test.finance.exception;

import org.springframework.http.HttpStatus;

public class DataSourceException extends AppException {
    public DataSourceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY, "data source error");
    }
}