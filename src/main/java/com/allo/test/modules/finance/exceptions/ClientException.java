package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when external API returns 4xx client errors (bad request, not found).
 * Maps to HTTP 502 Bad Gateway (since it's an external API issue).
 */
public class ClientException extends ExternalApiException {

    public ClientException(String apiEndpoint) {
        super(ResponseEnum.EXTERNAL_API_ERROR, apiEndpoint);
    }
}
