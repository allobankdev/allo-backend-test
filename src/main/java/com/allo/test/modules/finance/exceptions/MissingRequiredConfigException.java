package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.GlobalException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when required configuration is missing (e.g., github-username).
 * This causes application startup to fail during bean construction.
 */
public class MissingRequiredConfigException extends GlobalException {

    public MissingRequiredConfigException() {
        super(ResponseEnum.MISSING_REQUIRED_CONFIG);
    }
}
