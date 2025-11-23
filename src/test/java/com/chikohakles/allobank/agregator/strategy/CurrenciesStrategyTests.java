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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CurrenciesStrategyTests {
    @Mock
    RestClient restClient;

    @Mock
    RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    RestClient.ResponseSpec responseSpec;

    @InjectMocks
    CurrenciesStrategy strategy;

    @Test
    void getResourceType_ShouldReturnLatestIdrRates() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.SUPPORTED_CURRENCIES);
    }

    @Test
    void getData_ShouldCallRestClientAndReturnBody() {
        log.debug("getData_ShouldCallRestClientAndReturnBody Currencies START");
        var endpoint = "/currencies";
        var mockResponse = new HashMap<>();
        mockResponse.put("USD", "U.S. Dollar");
        mockResponse.put("IDR", "Indonesia Rupiah");

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString()))
                    .thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve())
                    .thenReturn(responseSpec);
            when(responseSpec.body(any(Class.class)))
                    .thenReturn(mockResponse);

            Object result = strategy.getData();

            assertThat(result).isEqualTo(mockResponse);
            verify(restClient).get();
            verify(requestHeadersUriSpec).uri(endpoint);
            verify(requestHeadersSpec).retrieve();
            verify(responseSpec).body(Map.class);

        log.debug("getData_ShouldCallRestClientAndReturnBody Currencies END");
    }
}

