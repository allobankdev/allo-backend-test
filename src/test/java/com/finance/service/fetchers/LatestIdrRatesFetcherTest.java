package com.finance.service.fetchers;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.external.RateResponse;
import com.finance.dto.internal.LatestIdrRatesInfoResponse;
import com.finance.exception.ExternalServiceException;
import com.finance.service.util.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient client;

    private LatestIdrRatesFetcher fetcher;

    private String githubUserName;

    @BeforeEach
    void setup() {
        githubUserName = "xixixi";
        fetcher = new LatestIdrRatesFetcher(client, githubUserName);
    }

    private RateResponse buildMockResponse(double usdRate) {
        RateResponse dto = new RateResponse();
        dto.setBase(AppConstant.IDR_BASE);
        dto.setDate("2025-11-20");
        dto.setRates(Map.of(AppConstant.USD_BASE, usdRate));
        return dto;
    }

    @Test
    void fetch_shouldReturnLatestIdrRatesInfo_whenClientReturnsData() {
        // Arrange
        RateResponse mockResponse = buildMockResponse(16000.0);
        when(client.getLatestRates(AppConstant.IDR_BASE)).thenReturn(Mono.just(mockResponse));

        // Act
        List<LatestIdrRatesInfoResponse> result = fetcher.fetch();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        LatestIdrRatesInfoResponse info = result.get(0);
        assertEquals("IDR", info.getBase());
        assertEquals("2025-11-20", info.getDate());
        assertEquals(16000.0, info.getRate().get(AppConstant.USD_BASE));

        double expectedUsdBuySpread = (1.0 / 16000.0) * (1.0 + SpreadCalculator.computeSpread(githubUserName.toLowerCase()));
        assertEquals(expectedUsdBuySpread, info.getUSD_BuySpread_IDR(), 1e-9);

        verify(client).getLatestRates(AppConstant.IDR_BASE);
    }

    @Test
    void fetch_shouldThrowExternalServiceException_whenClientReturnsEmpty() {
        // Arrange
        when(client.getLatestRates(AppConstant.IDR_BASE)).thenReturn(Mono.empty());

        // Act & Assert
        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> fetcher.fetch());
        assertEquals(AppConstant.NO_RESPONSE_FROM_API_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());

        verify(client).getLatestRates(AppConstant.IDR_BASE);
    }

    @Test
    void fetch_shouldThrowExternalServiceException_whenRatesAreEmpty() {
        // Arrange
        RateResponse dto = new RateResponse();
        dto.setBase(AppConstant.IDR_BASE);
        dto.setDate("2025-11-20");
        dto.setRates(Map.of()); // empty
        when(client.getLatestRates(AppConstant.IDR_BASE)).thenReturn(Mono.just(dto));

        // Act & Assert
        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> fetcher.fetch());
        assertEquals(AppConstant.EMPTY_RATES_RESPONSE_FROM_API_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void fetch_shouldThrowExternalServiceException_whenUsdRateMissing() {
        // Arrange
        RateResponse dto = new RateResponse();
        dto.setBase(AppConstant.IDR_BASE);
        dto.setDate("2025-11-20");
        dto.setRates(Map.of("EUR", 0.001)); // USD missing
        when(client.getLatestRates(AppConstant.IDR_BASE)).thenReturn(Mono.just(dto));

        // Act & Assert
        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> fetcher.fetch());
        assertEquals(AppConstant.USD_RATE_RESPONSE_MISSING_FROM_API_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }
}

