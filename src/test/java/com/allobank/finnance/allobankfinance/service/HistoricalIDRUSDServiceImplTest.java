package com.allobank.finnance.allobankfinance.service;

import com.allobank.finnance.allobankfinance.constant.ResourceTypeConstant;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.dto.frankfurter.HistoricalRatesResponse;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.impl.HistoricalIDRUSDServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class HistoricalIDRUSDServiceImplTest {

    @Mock
    private FrankfurterIntegrationService frankfurterService;

    private HistoricalIDRUSDServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new HistoricalIDRUSDServiceImpl(frankfurterService);
    }

    @Test
    void getResourceType_shouldReturnHistoricalIdrUsd() {
        String resourceType = service.getResourceType();

        Assertions.assertEquals(
                ResourceTypeConstant.HISTORICAL_IDR_USD,
                resourceType
        );
    }

    @Test
    void fetchData_shouldThrowException_whenStartDateIsNull() {
        FinanceRequestDto requestDto = FinanceRequestDto.builder()
                .endDate("2024-01-31")
                .build();

        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.fetchData(requestDto)
        );

        Assertions.assertEquals(
                "startDate and endDate are required for historical data",
                ex.getMessage()
        );
    }

    @Test
    void fetchData_shouldThrowException_whenEndDateIsNull() {
        FinanceRequestDto requestDto = FinanceRequestDto.builder()
                .startDate("2024-01-01")
                .build();

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.fetchData(requestDto)
        );
    }

    @Test
    void fetchData_shouldCallFrankfurterService_whenDatesAreValid() {
        // given
        FinanceRequestDto requestDto = FinanceRequestDto.builder()
                .startDate("2024-01-01")
                .endDate("2024-01-31")
                .build();

        //mock
        Map<String, BigDecimal> ratesDay1 = Map.of(
                "AUD", new BigDecimal("1.5422"),
                "CAD", new BigDecimal("1.4608"),
                "CHF", new BigDecimal("1.6051"),
                "USD", new BigDecimal("1.0046")
        );

        Map<String, BigDecimal> ratesDay2 = Map.of(
                "AUD", new BigDecimal("1.5346"),
                "CAD", new BigDecimal("1.4577"),
                "CHF", new BigDecimal("1.6043"),
                "USD", new BigDecimal("1.009")
        );

        Map<String, Map<String, BigDecimal>> rates = Map.of(
                "1999-12-30", ratesDay1,
                "2000-01-03", ratesDay2
        );

        HistoricalRatesResponse mockResponse =
                HistoricalRatesResponse.builder()
                        .base("EUR")
                        .startDate("1999-12-30")
                        .endDate("2000-12-29")
                        .rates(rates)
                        .build();

        Mockito.when(frankfurterService.getHistoricalRates(
                "2024-01-01",
                "2024-01-31",
                "IDR",
                "USD"
        )).thenReturn(mockResponse);

        // when
        Object result = service.fetchData(requestDto);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockResponse, result);

        Mockito.verify(frankfurterService, Mockito.times(1))
                .getHistoricalRates(
                        "2024-01-01",
                        "2024-01-31",
                        "IDR",
                        "USD"
                );
    }
}
