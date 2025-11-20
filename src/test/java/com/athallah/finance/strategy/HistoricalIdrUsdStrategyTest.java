package com.athallah.finance.strategy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.dto.HistoricalRatesRawDto;
import com.athallah.finance.service.strategy.HistoricalIdrUsdStrategy;
import com.athallah.finance.util.constant.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdStrategyTest {
    @Mock
    private FinanceFrankfurterWebClient webClient;

    @InjectMocks
    private HistoricalIdrUsdStrategy strategy;

    private HistoricalRatesRawDto mockHistoricalData;

    @BeforeEach
    void setUp() {
        // Create nested rates structure
        Map<String, Map<String, BigDecimal>> rates = new LinkedHashMap<>();

        Map<String, BigDecimal> rate1 = new LinkedHashMap<>();
        rate1.put("USD", new BigDecimal("0.000065"));
        rates.put("2023-12-29", rate1);

        Map<String, BigDecimal> rate2 = new LinkedHashMap<>();
        rate2.put("USD", new BigDecimal("0.000064"));
        rates.put("2024-01-02", rate2);

        Map<String, BigDecimal> rate3 = new LinkedHashMap<>();
        rate3.put("USD", new BigDecimal("0.000064"));
        rates.put("2024-01-03", rate3);

        mockHistoricalData = HistoricalRatesRawDto.builder()
                .amount(1)
                .base("IDR")
                .rates(rates)
                .startDate("2023-12-29")
                .endDate("2024-01-05")
                .build();
    }

    @Test
    void fetchData_shouldCallWebClientAndReturnData() {
        // Given
        when(webClient.getHistoricalIdrUsd()).thenReturn(mockHistoricalData);

        // When
        Object result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(HistoricalRatesRawDto.class);

        HistoricalRatesRawDto responseDto = (HistoricalRatesRawDto) result;
        assertThat(responseDto.getAmount()).isEqualTo(1);
        assertThat(responseDto.getBase()).isEqualTo("IDR");
        assertThat(responseDto.getStartDate()).isEqualTo("2023-12-29");
        assertThat(responseDto.getEndDate()).isEqualTo("2024-01-05");
        assertThat(responseDto.getRates()).hasSize(3);
        assertThat(responseDto.getRates().get("2023-12-29").get("USD"))
                .isEqualByComparingTo(new BigDecimal("0.000065"));

        verify(webClient, times(1)).getHistoricalIdrUsd();
    }

    @Test
    void getResourceType_shouldReturnHistoricalIdrUsd() {
        // When
        ResourceType result = strategy.getResourceType();

        // Then
        assertThat(result).isEqualTo(ResourceType.historical_idr_usd);
    }

    @Test
    void fetchData_shouldHandleEmptyRates() {
        // Given
        HistoricalRatesRawDto emptyData = HistoricalRatesRawDto.builder()
                .amount(1)
                .base("IDR")
                .rates(new LinkedHashMap<>())
                .startDate("2024-01-01")
                .endDate("2024-01-01")
                .build();

        when(webClient.getHistoricalIdrUsd()).thenReturn(emptyData);

        // When
        Object result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        HistoricalRatesRawDto responseDto = (HistoricalRatesRawDto) result;
        assertThat(responseDto.getRates()).isEmpty();
    }

    @Test
    void fetchData_shouldPreserveAllRateData() {
        // Given
        when(webClient.getHistoricalIdrUsd()).thenReturn(mockHistoricalData);

        // When
        Object result = strategy.fetchData();

        // Then
        HistoricalRatesRawDto responseDto = (HistoricalRatesRawDto) result;
        assertThat(responseDto.getRates()).containsKeys("2023-12-29", "2024-01-02", "2024-01-03");
        assertThat(responseDto.getRates().get("2024-01-02").get("USD"))
                .isEqualByComparingTo(new BigDecimal("0.000064"));
    }
}
