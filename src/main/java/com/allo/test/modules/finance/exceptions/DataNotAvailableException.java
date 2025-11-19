package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.GlobalException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when requested data is not available (failed to load at startup).
 * Maps to HTTP 503 Service Unavailable.
 */
public class DataNotAvailableException extends GlobalException {

    public DataNotAvailableException() {
        super(ResponseEnum.DATA_NOT_AVAILABLE);
    }
}
