package com.allo.test.modules.finance.exceptions;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.shared.response.ResponseEnum;

/**
 * Thrown when API response cannot be parsed/deserialized.
 * Maps to HTTP 502 Bad Gateway (invalid response from external API).
 */
public class ResponseParsingException extends ExternalApiException {

    public ResponseParsingException(String apiEndpoint) {
        super(ResponseEnum.INVALID_EXTERNAL_API_RESPONSE, apiEndpoint);
    }
}
