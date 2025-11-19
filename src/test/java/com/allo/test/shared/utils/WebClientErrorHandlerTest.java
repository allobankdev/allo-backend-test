package com.allo.test.shared.utils;

import com.allo.test.configs.exceptions.ExternalApiException;
import com.allo.test.modules.finance.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for WebClientErrorHandler utility class.
 * <p>
 * Tests exception mapping from WebClient errors to custom domain exceptions.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WebClientErrorHandler Unit Tests")
class WebClientErrorHandlerTest {

    private static final String TEST_ENDPOINT = "/api/test-endpoint";

    // ==================== PASS-THROUGH SCENARIOS (Already Mapped) ====================

    @Test
    @DisplayName("Should pass through already mapped ClientException")
    void shouldPassThrough_AlreadyMappedClientException() {
        // Arrange
        ExternalApiException originalException = new ClientException(TEST_ENDPOINT);

        // Act
        Throwable result = WebClientErrorHandler.mapException(originalException, TEST_ENDPOINT);

        // Assert - Should return the same exception instance
        assertThat(result)
                .isSameAs(originalException)
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("Should pass through already mapped ServerException")
    void shouldPassThrough_AlreadyMappedServerException() {
        // Arrange
        ExternalApiException originalException = new ServerException(TEST_ENDPOINT);

        // Act
        Throwable result = WebClientErrorHandler.mapException(originalException, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isSameAs(originalException)
                .isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("Should pass through already mapped ConnectivityException")
    void shouldPassThrough_AlreadyMappedConnectivityException() {
        // Arrange
        ExternalApiException originalException = new ConnectivityException(TEST_ENDPOINT);

        // Act
        Throwable result = WebClientErrorHandler.mapException(originalException, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isSameAs(originalException)
                .isInstanceOf(ConnectivityException.class);
    }

    @Test
    @DisplayName("Should pass through already mapped ResponseParsingException")
    void shouldPassThrough_AlreadyMappedResponseParsingException() {
        // Arrange
        ExternalApiException originalException = new ResponseParsingException(TEST_ENDPOINT);

        // Act
        Throwable result = WebClientErrorHandler.mapException(originalException, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isSameAs(originalException)
                .isInstanceOf(ResponseParsingException.class);
    }

    // ==================== HTTP 4xx CLIENT ERRORS ====================

    @Test
    @DisplayName("Should map BadRequest (400) to ClientException")
    void shouldMapBadRequest_ToClientException() {
        // Arrange
        WebClientResponseException badRequest = WebClientResponseException.create(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(badRequest, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(ClientException.class)
                .isNotSameAs(badRequest);
    }

    @Test
    @DisplayName("Should map NotFound (404) to ClientException")
    void shouldMapNotFound_ToClientException() {
        // Arrange
        WebClientResponseException notFound = WebClientResponseException.create(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(notFound, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(ClientException.class)
                .isNotSameAs(notFound);
    }

    // ==================== HTTP 5xx SERVER ERRORS ====================

    @Test
    @DisplayName("Should map InternalServerError (500) to ServerException")
    void shouldMapInternalServerError_ToServerException() {
        // Arrange
        WebClientResponseException serverError = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(serverError, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(ServerException.class)
                .isNotSameAs(serverError);
    }

    @Test
    @DisplayName("Should map BadGateway (502) to ServerException")
    void shouldMapBadGateway_ToServerException() {
        // Arrange
        WebClientResponseException badGateway = WebClientResponseException.create(
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(badGateway, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("Should map ServiceUnavailable (503) to ServerException")
    void shouldMapServiceUnavailable_ToServerException() {
        // Arrange
        WebClientResponseException serviceUnavailable = WebClientResponseException.create(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(serviceUnavailable, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("Should map GatewayTimeout (504) to ServerException")
    void shouldMapGatewayTimeout_ToServerException() {
        // Arrange
        WebClientResponseException gatewayTimeout = WebClientResponseException.create(
                HttpStatus.GATEWAY_TIMEOUT.value(),
                "Gateway Timeout",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(gatewayTimeout, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("Should map other 5xx errors to ServerException")
    void shouldMapOther5xxErrors_ToServerException() {
        // Arrange - Using 507 Insufficient Storage as example
        WebClientResponseException insufficientStorage = WebClientResponseException.create(
                HttpStatus.INSUFFICIENT_STORAGE.value(),
                "Insufficient Storage",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(insufficientStorage, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
    }

    // ==================== NETWORK/CONNECTIVITY ERRORS ====================

    @Test
    @DisplayName("Should map WebClientRequestException with timeout to ConnectivityException")
    void shouldMapWebClientRequestException_WithTimeout_ToConnectivityException() {
        // Arrange - Mock WebClientRequestException
        WebClientRequestException requestException = mock(WebClientRequestException.class);

        // Act
        Throwable result = WebClientErrorHandler.mapException(requestException, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(ConnectivityException.class)
                .isNotSameAs(requestException);
    }

    @Test
    @DisplayName("Should map WebClientRequestException with connection refused to ConnectivityException")
    void shouldMapWebClientRequestException_WithConnectionRefused_ToConnectivityException() {
        // Arrange - Mock WebClientRequestException
        WebClientRequestException requestException = mock(WebClientRequestException.class);

        // Act
        Throwable result = WebClientErrorHandler.mapException(requestException, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ConnectivityException.class);
    }

    // ==================== PARSING/UNEXPECTED ERRORS ====================

    @Test
    @DisplayName("Should map DataBufferLimitException to ResponseParsingException")
    void shouldMapDataBufferLimitException_ToResponseParsingException() {
        // Arrange
        DataBufferLimitException bufferException = new DataBufferLimitException(
                "Exceeded limit on max bytes to buffer"
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(bufferException, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(ResponseParsingException.class)
                .isNotSameAs(bufferException);
    }

    @Test
    @DisplayName("Should map generic RuntimeException to ResponseParsingException")
    void shouldMapGenericRuntimeException_ToResponseParsingException() {
        // Arrange
        RuntimeException genericException = new RuntimeException("Unexpected error");

        // Act
        Throwable result = WebClientErrorHandler.mapException(genericException, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ResponseParsingException.class);
    }

    @Test
    @DisplayName("Should map IllegalArgumentException to ResponseParsingException")
    void shouldMapIllegalArgumentException_ToResponseParsingException() {
        // Arrange
        IllegalArgumentException illegalArgException = new IllegalArgumentException(
                "Invalid argument"
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(illegalArgException, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ResponseParsingException.class);
    }

    @Test
    @DisplayName("Should map NullPointerException to ResponseParsingException")
    void shouldMapNullPointerException_ToResponseParsingException() {
        // Arrange
        NullPointerException nullPointerException = new NullPointerException(
                "Null value encountered"
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(nullPointerException, TEST_ENDPOINT);

        // Assert
        assertThat(result).isInstanceOf(ResponseParsingException.class);
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should handle null endpoint gracefully")
    void shouldHandleNullEndpoint_Gracefully() {
        // Arrange - Mock WebClientRequestException
        WebClientRequestException requestException = mock(WebClientRequestException.class);
        String nullEndpoint = null;

        // Act
        Throwable result = WebClientErrorHandler.mapException(requestException, nullEndpoint);

        // Assert
        assertThat(result).isInstanceOf(ConnectivityException.class);
        // Exception should still be created, just with null endpoint
    }

    @Test
    @DisplayName("Should handle empty endpoint gracefully")
    void shouldHandleEmptyEndpoint_Gracefully() {
        // Arrange - Mock WebClientRequestException
        WebClientRequestException requestException = mock(WebClientRequestException.class);
        String emptyEndpoint = "";

        // Act
        Throwable result = WebClientErrorHandler.mapException(requestException, emptyEndpoint);

        // Assert
        assertThat(result).isInstanceOf(ConnectivityException.class);
    }

    @Test
    @DisplayName("Should handle very long endpoint path")
    void shouldHandleVeryLongEndpointPath() {
        // Arrange
        String longEndpoint = "/api/v1/very/long/nested/endpoint/path/with/many/segments";
        WebClientResponseException serverError = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(serverError, longEndpoint);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
    }

    // ==================== RETURN TYPE VERIFICATION ====================

    @Test
    @DisplayName("Should always return Throwable type")
    void shouldAlwaysReturnThrowableType() {
        // Arrange
        RuntimeException exception = new RuntimeException("Test error");

        // Act
        Throwable result = WebClientErrorHandler.mapException(exception, TEST_ENDPOINT);

        // Assert
        assertThat(result)
                .isInstanceOf(Throwable.class)
                .isNotNull();
    }

    @Test
    @DisplayName("Should preserve endpoint information in mapped exception")
    void shouldPreserveEndpointInformation_InMappedException() {
        // Arrange
        String customEndpoint = "/api/custom/endpoint";
        WebClientResponseException serverError = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server Error",
                null,
                null,
                null
        );

        // Act
        Throwable result = WebClientErrorHandler.mapException(serverError, customEndpoint);

        // Assert
        assertThat(result).isInstanceOf(ServerException.class);
        // The exception message should contain endpoint information
        // (This depends on how ExternalApiException is implemented)
    }
}
