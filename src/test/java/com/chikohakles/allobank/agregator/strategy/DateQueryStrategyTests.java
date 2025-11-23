package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.DateQueryResponse;
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
public class DateQueryStrategyTests {
    @Mock
    RestClient restClient;

    @Mock
    RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    RestClient.ResponseSpec responseSpec;

    @InjectMocks
    DateQueryStrategy strategy;

    @Test
    void getResourceType_ShouldReturnLatestIdrRates() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.HISTORICAL_IDR_USD);
    }

    @Test
    void getData_ShouldCallRestClientAndReturnBody() {
        log.debug("getData_ShouldCallRestClientAndReturnBody DateQuery START");
        var endpoint = "/{from}..{to}?from={base}&to={target}";
        var mockResponse = new DateQueryResponse();
        mockResponse.setAmount(BigDecimal.ONE);
        mockResponse.setBase("IDR");
        mockResponse.setStart_date(new Date());
        mockResponse.setEnd_date(new Date());
        mockResponse.setRates(new HashMap<>());
        mockResponse.getRates().put(new Date(), new HashMap<>());
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.body(any(Class.class)))
                .thenReturn(mockResponse);

        Object result = strategy.getData();

        assertThat(result).isEqualTo(mockResponse);
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(endpoint, "2024-01-01", "2024-01-05", "IDR", "USD");
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(DateQueryResponse.class);

        log.debug("getData_ShouldCallRestClientAndReturnBody DateQuery END");
    }
}

