package com.athallah.finance.strategy;


import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.dto.LatestRatesRawDto;
import com.athallah.finance.dto.LatestRatesResponseDto;
import com.athallah.finance.service.strategy.LatestIdrRatesStrategy;
import com.athallah.finance.util.constant.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesStrategyTest {
    @Mock
    private FinanceFrankfurterWebClient webClient;

    @InjectMocks
    private LatestIdrRatesStrategy strategy;

    private LatestRatesRawDto mockRawData;

    @BeforeEach
    void setUp() {
        Map<String, BigDecimal> rates = new LinkedHashMap<>();
        rates.put("AUD", new BigDecimal("0.000092"));
        rates.put("USD", new BigDecimal("0.000060"));
        rates.put("EUR", new BigDecimal("0.000052"));
        rates.put("GBP", new BigDecimal("0.000046"));
        rates.put("JPY", new BigDecimal("0.00934"));

        mockRawData = LatestRatesRawDto.builder()
                .amount(1)
                .base("IDR")
                .date("2025-11-19")
                .rates(rates)
                .build();
    }

    @Test
    void fetchData_shouldTransformDataWithSpreadFactor() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
        strategy.init();

        when(webClient.getLatestIdrRates()).thenReturn(mockRawData);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(1);
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getDate()).isEqualTo("2025-11-19");
        assertThat(result.getRates()).hasSize(5);
        assertThat(result.getRates()).containsKeys("USD", "EUR", "GBP", "JPY", "AUD");
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr().scale()).isEqualTo(8);

        verify(webClient, times(1)).getLatestIdrRates();
    }

    @Test
    void fetchData_shouldCalculateUsdBuySpreadCorrectly() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
        strategy.init();

        when(webClient.getLatestIdrRates()).thenReturn(mockRawData);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        // USD rate = 0.000060
        // Base conversion: 1 / 0.000060 = 16666.666...
        // Spread factor for "testuser" should be calculated
        // Final = 16666.666... * (1 + spreadFactor)
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isGreaterThan(new BigDecimal("16666"));
        assertThat(result.getUsdBuySpreadIdr()).isLessThan(new BigDecimal("17000"));
    }

    @Test
    void fetchData_shouldHandleNullRates() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
        strategy.init();

        LatestRatesRawDto nullRatesData = LatestRatesRawDto.builder()
                .amount(1)
                .base("IDR")
                .date("2025-11-19")
                .rates(null)
                .build();

        when(webClient.getLatestIdrRates()).thenReturn(nullRatesData);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isNull();
        assertThat(result.getRates()).isNull();
    }

    @Test
    void fetchData_shouldHandleMissingUsdRate() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
        strategy.init();

        Map<String, BigDecimal> ratesWithoutUsd = new LinkedHashMap<>();
        ratesWithoutUsd.put("EUR", new BigDecimal("0.000052"));
        ratesWithoutUsd.put("GBP", new BigDecimal("0.000046"));

        LatestRatesRawDto dataWithoutUsd = LatestRatesRawDto.builder()
                .amount(1)
                .base("IDR")
                .date("2025-11-19")
                .rates(ratesWithoutUsd)
                .build();

        when(webClient.getLatestIdrRates()).thenReturn(dataWithoutUsd);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isNull();
        assertThat(result.getRates()).containsKeys("EUR", "GBP");
    }

    @Test
    void getResourceType_shouldReturnLatestIdrRates() {
        // When
        ResourceType result = strategy.getResourceType();

        // Then
        assertThat(result).isEqualTo(ResourceType.latest_idr_rates);
    }

    @Test
    void init_shouldCalculateSpreadFactorForValidUsername() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "john");

        // When
        strategy.init();
        BigDecimal spreadFactor = (BigDecimal) ReflectionTestUtils.getField(strategy, "spreadFactor");

        // Then
        // "john" unicode sum: j(106) + o(111) + h(104) + n(110) = 431
        // 431 % 1000 = 431
        // 431 / 100000 = 0.00431
        assertThat(spreadFactor).isNotNull();
        assertThat(spreadFactor.scale()).isEqualTo(5);
        assertThat(spreadFactor).isEqualByComparingTo(new BigDecimal("0.00431"));
    }

    @Test
    void init_shouldUseDefaultSpreadFactorForEmptyUsername() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "");

        // When
        strategy.init();
        BigDecimal spreadFactor = (BigDecimal) ReflectionTestUtils.getField(strategy, "spreadFactor");

        // Then
        assertThat(spreadFactor).isEqualByComparingTo(new BigDecimal("0.00500"));
    }

    @Test
    void init_shouldUseDefaultSpreadFactorForNullUsername() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", null);

        // When
        strategy.init();
        BigDecimal spreadFactor = (BigDecimal) ReflectionTestUtils.getField(strategy, "spreadFactor");

        // Then
        assertThat(spreadFactor).isEqualByComparingTo(new BigDecimal("0.00500"));
    }

    @Test
    void fetchData_shouldPreserveAllRatesFromRawData() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "test");
        strategy.init();

        when(webClient.getLatestIdrRates()).thenReturn(mockRawData);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        assertThat(result.getRates()).isEqualTo(mockRawData.getRates());
        assertThat(result.getRates().get("AUD")).isEqualByComparingTo(new BigDecimal("0.000092"));
        assertThat(result.getRates().get("JPY")).isEqualByComparingTo(new BigDecimal("0.00934"));
    }

    @Test
    void fetchData_shouldRoundUsdBuySpreadToEightDecimals() {
        // Given
        ReflectionTestUtils.setField(strategy, "githubUsername", "alice");
        strategy.init();

        when(webClient.getLatestIdrRates()).thenReturn(mockRawData);

        // When
        LatestRatesResponseDto result = strategy.fetchData();

        // Then
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr().scale()).isEqualTo(8);
        // Verify it's properly rounded, not truncated
        String resultString = result.getUsdBuySpreadIdr().toPlainString();
        int decimalIndex = resultString.indexOf('.');
        if (decimalIndex >= 0) {
            assertThat(resultString.substring(decimalIndex + 1).length()).isLessThanOrEqualTo(8);
        }
    }
}
