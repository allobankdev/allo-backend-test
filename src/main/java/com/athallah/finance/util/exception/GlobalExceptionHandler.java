package com.athallah.finance.util.exception;

import com.athallah.finance.config.message.EnumMessagesKey;
import com.athallah.finance.util.response.GlobalRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle 4xx Client Errors from External API
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<GlobalRespDto> handleClientError(
            HttpClientErrorException ex, WebRequest request) {

        log.error("Client error from external API: Status={}, URI={}, Body={}",
                ex.getStatusCode(), request.getDescription(false),
                ex.getResponseBodyAsString(), ex);

        EnumMessagesKey messageKey;
        String errorCode;

        switch (ex.getStatusCode().value()) {
            case 400:
                messageKey = EnumMessagesKey.EXT_SERVICE_BAD_REQUEST;
                errorCode = "EXT_BAD_REQUEST";
                break;
            case 401:
                messageKey = EnumMessagesKey.EXT_SERVICE_UNAUTHORIZED;
                errorCode = "EXT_UNAUTHORIZED";
                break;
            case 403:
                messageKey = EnumMessagesKey.EXT_SERVICE_FORBIDDEN;
                errorCode = "EXT_FORBIDDEN";
                break;
            case 404:
                messageKey = EnumMessagesKey.EXT_SERVICE_NOT_FOUND;
                errorCode = "EXT_NOT_FOUND";
                break;
            case 429:
                messageKey = EnumMessagesKey.EXT_SERVICE_RATE_LIMIT;
                errorCode = "EXT_RATE_LIMIT";
                break;
            default:
                messageKey = EnumMessagesKey.EXT_SERVICE_BAD_REQUEST;
                errorCode = "EXT_CLIENT_ERROR";
        }

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(messageKey.getMessageKey())
                .errorCode(errorCode)
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    // Handle 5xx Server Errors from External API
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<GlobalRespDto> handleServerError(
            HttpServerErrorException ex, WebRequest request) {

        log.error("Server error from external API: Status={}, URI={}, Body={}",
                ex.getStatusCode(), request.getDescription(false),
                ex.getResponseBodyAsString(), ex);

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(EnumMessagesKey.EXT_SERVICE_SERVER_ERROR.getMessageKey())
                .errorCode("EXT_SERVER_ERROR")
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Handle Network/Connection Failures
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<GlobalRespDto> handleNetworkError(
            ResourceAccessException ex, WebRequest request) {

        log.error("Network error while connecting to external API: URI={}",
                request.getDescription(false), ex);

        EnumMessagesKey messageKey;
        String errorCode;

        if (ex.getCause() instanceof ConnectException) {
            messageKey = EnumMessagesKey.NETWORK_CONNECTION_FAILED;
            errorCode = "NETWORK_CONNECTION_REFUSED";
        } else if (ex.getCause() instanceof SocketTimeoutException) {
            messageKey = EnumMessagesKey.NETWORK_TIMEOUT;
            errorCode = "NETWORK_TIMEOUT";
        } else if (ex.getCause() instanceof UnknownHostException) {
            messageKey = EnumMessagesKey.NETWORK_HOST_UNREACHABLE;
            errorCode = "NETWORK_HOST_UNKNOWN";
        } else {
            messageKey = EnumMessagesKey.EXT_SERVICE_UNAVAILABLE;
            errorCode = "NETWORK_ERROR";
        }

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(messageKey.getMessageKey())
                .errorCode(errorCode)
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Handle Generic REST Client Exceptions
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<GlobalRespDto> handleRestClientException(
            RestClientException ex, WebRequest request) {

        log.error("REST client error occurred: URI={}",
                request.getDescription(false), ex);

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(EnumMessagesKey.EXT_SERVICE_UNAVAILABLE.getMessageKey())
                .errorCode("REST_CLIENT_ERROR")
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    // Handle Illegal Arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalRespDto> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        log.error("Invalid argument provided: URI={}",
                request.getDescription(false), ex);

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(EnumMessagesKey.INVALID_ARGUMENT.getMessageKey())
                .errorCode("INVALID_ARGUMENT")
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle All Other Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRespDto> handleGenericException(
            Exception ex, WebRequest request) {

        log.error("Unexpected error occurred: URI={}",
                request.getDescription(false), ex);

        GlobalRespDto response = GlobalRespDto.errorResponseBuilder()
                .message(EnumMessagesKey.UNEXPECTED_ERROR.getMessageKey())
                .errorCode("INTERNAL_ERROR")
                .errorDetails(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}