package com.allobank.exceptions;

import com.allobank.enums.RESPONSE;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private String code;
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(RESPONSE RESPONSE) {
        super(RESPONSE.getMessage());
        this.code = RESPONSE.getCode();
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
