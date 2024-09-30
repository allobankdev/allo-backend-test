package com.allobank.allobackendtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class PartaiNotFoundException extends RuntimeException {
    public PartaiNotFoundException(Long id) {
        super("Partai not found: " + id);
    }

}
