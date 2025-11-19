package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when network connectivity issues occur (timeout, connection refused, DNS failure).
 * Maps to HTTP 503 Service Unavailable.
 */
public class ConnectivityException extends ExternalApiException {

    public ConnectivityException(String apiEndpoint) {
        super(ResponseEnum.EXTERNAL_API_UNAVAILABLE, apiEndpoint);
    }
}
