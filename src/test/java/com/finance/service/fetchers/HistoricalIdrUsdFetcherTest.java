package com.finance.service.fetchers;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.external.HistoricalRateResponse;
import com.finance.dto.internal.HistoricalRateInfoResponse;
import com.finance.exception.ExternalServiceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    private HistoricalRateResponse buildMockResponse() {
        Map<String, Map<String, BigDecimal>> rates = new HashMap<>();
        rates.put("2025-11-20", Map.of("USD", new BigDecimal("0.000062")));
        rates.put("2025-11-19", Map.of("USD", new BigDecimal("0.000063")));

        HistoricalRateResponse response = new HistoricalRateResponse();
        response.setRates(rates);
        return response;
    }

    @Test
    void fetch_shouldReturnHistoricalRateInfoList_whenClientReturnsData() {
        // Arrange
        HistoricalRateResponse mockResponse = buildMockResponse();
        when(client.getHistoricalIdrUsd()).thenReturn(Mono.just(mockResponse));

        // Act
        List<HistoricalRateInfoResponse> result = fetcher.fetch();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        HistoricalRateInfoResponse first = result.get(0);
        assertEquals("2025-11-20", first.getDate());
        assertEquals(new BigDecimal("0.000062"), first.getRate());

        HistoricalRateInfoResponse second = result.get(1);
        assertEquals("2025-11-19", second.getDate());
        assertEquals(new BigDecimal("0.000063"), second.getRate());

        verify(client).getHistoricalIdrUsd();
    }

    @Test
    void fetch_shouldThrowExternalServiceException_whenClientReturnsEmpty() {
        // Arrange
        when(client.getHistoricalIdrUsd()).thenReturn(Mono.empty());

        // Act & Assert
        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> fetcher.fetch());
        assertEquals(AppConstant.NO_RESPONSE_FROM_API_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());

        verify(client).getHistoricalIdrUsd();
    }

}
