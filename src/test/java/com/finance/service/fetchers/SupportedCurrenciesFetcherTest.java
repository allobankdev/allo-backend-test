package com.finance.service.fetchers;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.external.SupportedCurrenciesResponse;
import com.finance.dto.internal.CurrencyInfoResponse;
import com.finance.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient client;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setup() {
        fetcher = new SupportedCurrenciesFetcher(client);
    }

    private SupportedCurrenciesResponse buildMockResponse() {
        SupportedCurrenciesResponse dto = new SupportedCurrenciesResponse();
        dto.setSupportedCurrencies(Map.of(
                "USD", "US Dollar",
                "EUR", "Euro"
        ));
        return dto;
    }

    @Test
    void fetch_shouldReturnCurrencyInfoList_whenClientReturnsData() {
        // Arrange
        SupportedCurrenciesResponse mockResponse = buildMockResponse();
        when(client.getSupportedCurrencies()).thenReturn(Mono.just(mockResponse));

        // Act
        List<CurrencyInfoResponse> result = fetcher.fetch();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(c -> c.getCurrencyCode().equals("USD") && c.getCurrencyName().equals("US Dollar")));
        assertTrue(result.stream().anyMatch(c -> c.getCurrencyCode().equals("EUR") && c.getCurrencyName().equals("Euro")));

        verify(client).getSupportedCurrencies();
    }

    @Test
    void fetch_shouldThrowExternalServiceException_whenClientReturnsEmpty() {
        // Arrange
        when(client.getSupportedCurrencies()).thenReturn(Mono.empty());

        // Act & Assert
        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> fetcher.fetch());
        assertEquals(AppConstant.NO_RESPONSE_FROM_API_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());

        verify(client).getSupportedCurrencies();
    }
}
