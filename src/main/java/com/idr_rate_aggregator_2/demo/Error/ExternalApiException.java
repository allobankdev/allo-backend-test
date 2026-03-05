package com.idr_rate_aggregator_2.demo.Error;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final int statusCode;
    private final String resourceType;
    private final boolean retryable;

    public ExternalApiException(String message) {
        super(message);
        this.statusCode = 0;
        this.resourceType = "unknown";
        this.retryable = false;
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.resourceType = "unknown";
        this.retryable = determineRetryable(cause);
    }

    public ExternalApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.resourceType = "unknown";
        this.retryable = statusCode >= 500; // 5xx errors are retryable
    }

    public ExternalApiException(String message, int statusCode, String resourceType) {
        super(message);
        this.statusCode = statusCode;
        this.resourceType = resourceType;
        this.retryable = statusCode >= 500;
    }

    public ExternalApiException(String message, Throwable cause, String resourceType) {
        super(message, cause);
        this.statusCode = 0;
        this.resourceType = resourceType;
        this.retryable = determineRetryable(cause);
    }

    private boolean determineRetryable(Throwable cause) {
        return cause instanceof java.net.ConnectException ||
                cause instanceof java.net.SocketTimeoutException ||
                cause instanceof java.net.UnknownHostException;
    }
}