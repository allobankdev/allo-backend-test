package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when external API returns 5xx server errors.
 * Maps to HTTP 502 Bad Gateway.
 */
public class ServerException extends ExternalApiException {

    public ServerException(String apiEndpoint) {
        super(ResponseEnum.EXTERNAL_API_ERROR, apiEndpoint);
    }
}
