package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.LatestResponse;
import com.chikohakles.allobank.agregator.helper.CalculationUtil;
import org.mockito.MockedStatic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class LatestRateStrategyTests {
    @Mock
    RestClient restClient;

    @Mock
    RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    RestClient.ResponseSpec responseSpec;

    @InjectMocks
    LatestRateStrategy strategy;

    @Test
    void getResourceType_ShouldReturnLatestIdrRates() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.LATEST_IDR_RATES);
    }

    @Test
    void getData_ShouldCallRestClientAndReturnBody() {
        log.debug("getData_ShouldCallRestClientAndReturnBody Latest START");
        var endpoint = "/latest?base={base}";
        var mockResponse = new LatestResponse();
        mockResponse.setAmount(BigDecimal.ONE);
        mockResponse.setBase("IDR");
        mockResponse.setDate(new Date());
        mockResponse.setUSD_BuySpread_IDR(BigDecimal.valueOf(0.00456));
        mockResponse.setRates(new HashMap<>());
        mockResponse.getRates().put("USD", BigDecimal.valueOf(0.00110));
        try (MockedStatic<CalculationUtil> mockedCalc = mockStatic(CalculationUtil.class)) {
            mockedCalc.when(() -> CalculationUtil.calculateSpreadFactor(any()))
                    .thenReturn(BigDecimal.valueOf(0.00123));
            mockedCalc.when(() -> CalculationUtil.calculateRate(any(BigDecimal.class), any(BigDecimal.class)))
                    .thenReturn(BigDecimal.valueOf(0.00123));

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString(), anyString()))
                    .thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve())
                    .thenReturn(responseSpec);
            when(responseSpec.body(any(Class.class)))
                    .thenReturn(mockResponse);

            Object result = strategy.getData();

            assertThat(result).isEqualTo(mockResponse);
            verify(restClient).get();
            verify(requestHeadersUriSpec).uri(endpoint, "IDR");
            verify(requestHeadersSpec).retrieve();
            verify(responseSpec).body(LatestResponse.class);

            //to check were the mocked calculation already executed
            mockedCalc.verify(() -> CalculationUtil.calculateSpreadFactor(any()));
            mockedCalc.verify(() -> CalculationUtil.calculateRate(any(BigDecimal.class), any(BigDecimal.class)));
        }

        log.debug("getData_ShouldCallRestClientAndReturnBody Latest END");
    }
}

