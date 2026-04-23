package com.allobank.finnance.allobankfinance.service;

import com.allobank.finnance.allobankfinance.config.OtherProperties;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.dto.frankfurter.LatestRatesResponse;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.impl.LatestIdrRatesServiceImpl;
import com.allobank.finnance.allobankfinance.util.SpreadFactorCalculatorUtil;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class LatestIdrRatesServiceImplTest {

    @Mock
    private FrankfurterIntegrationService frankfurterService;

    @Mock
    private OtherProperties otherProperties;

    private LatestIdrRatesServiceImpl service;

    @BeforeEach
    void setUp() {
        otherProperties = new OtherProperties();
        otherProperties.setGithubUsername("herdiansyah5197");
        service = new LatestIdrRatesServiceImpl(frankfurterService,otherProperties);

    }

    @Test
    void shouldCalculateUsdBuySpreadIdrCorrectly() {

        // given
        BigDecimal usdRate = new BigDecimal("1.1617");

        LatestRatesResponse mockResponse =
                LatestRatesResponse.builder()
                        .base("IDR")
                        .rates(Map.of("USD", usdRate))
                        .build();

        Mockito.when(frankfurterService.getLatestUsdRates("IDR"))
                .thenReturn(mockResponse);

        // when
        BigDecimal result = (BigDecimal) service.fetchData(new FinanceRequestDto());

        // then
        BigDecimal spreadFactor =
                SpreadFactorCalculatorUtil.calculateSpreadFactor("herdiansyah5197");

        BigDecimal expected =
                BigDecimal.ONE
                        .divide(usdRate, 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.ONE.add(spreadFactor));

        AssertionsForClassTypes.assertThat(result)
                .isEqualByComparingTo(expected);

        Mockito.verify(frankfurterService)
                .getLatestUsdRates("IDR");
    }
}
