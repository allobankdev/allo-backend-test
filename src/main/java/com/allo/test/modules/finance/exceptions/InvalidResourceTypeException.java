package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.GlobalException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when user requests an invalid or unsupported resource type.
 * Returns HTTP 400 Bad Request.
 */
public class InvalidResourceTypeException extends GlobalException {

    public InvalidResourceTypeException() {
        super(ResponseEnum.INVALID_RESOURCE_TYPE);
    }
}
