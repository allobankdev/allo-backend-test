package com.allo.test.configs.exceptions;

import com.allo.test.shared.response.ResponseEnum;
import lombok.Getter;

/**
 * Base exception for all Frankfurter API-related errors.
 * Contains the API endpoint that failed for better error tracking.
 * <p>
 * Extends RuntimeException directly since it has a dedicated handler in ControllerAdvisor.
 */
@Getter
public class ExternalApiException extends RuntimeException {

    private final ResponseEnum responseEnum;
    private final String apiEndpoint;

    public ExternalApiException(ResponseEnum responseEnum, String apiEndpoint) {
        super(responseEnum.getResponseCode());
        this.responseEnum = responseEnum;
        this.apiEndpoint = apiEndpoint;
    }
}
