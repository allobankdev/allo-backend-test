package com.allobank.exceptions;

import com.allobank.enums.RESPONSE;
import lombok.Getter;

@Getter
public class ExternalException extends RuntimeException {

    public ExternalException(String message) {
        super(message);
    }

    public ExternalException(RESPONSE response) {
        super(response.getMessage());
        this.code = response.getCode();
    }

    public ExternalException(String code, String message) {
        super(message);
        this.code = code;
    }

    private String code;
}
