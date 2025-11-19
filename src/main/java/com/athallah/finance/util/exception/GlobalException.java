package com.athallah.finance.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@AllArgsConstructor
public class GlobalException extends RuntimeException implements Serializable {

    final HttpStatus httpStatus;
    final String messageKey;
    final String[] messageArgs;
    transient final Object data;

    public GlobalException(HttpStatus httpStatus, String messageKey, String messageArgs) {
        this(httpStatus, messageKey, new String[]{messageArgs});
    }

    public GlobalException(HttpStatus httpStatus, String messageKey, String... messageArgs) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
        this.data = null;
    }

    public GlobalException(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
        this.messageArgs = null;
        this.data = null;
    }

    public GlobalException(HttpStatus httpStatus, String messageKey, Object data) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
        this.messageArgs = null;
        this.data = data;
    }

}

