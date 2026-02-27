package com.example.finance.config;

import com.example.finance.service.FinanceDataService;
import com.example.finance.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DataLoaderRunnerIntegrationTest {

    @Autowired
    private FinanceDataService service;

    @TestConfiguration
    static class MockRestTemplateConfig {
        @Bean
        @Primary
        public RestTemplate restTemplate() {
            RestTemplate rt = mock(RestTemplate.class);
            // responses for each endpoint
            when(rt.getForObject(contains("latest"), eq(Map.class)))
                    .thenReturn(Collections.singletonMap("rates", Collections.singletonMap("USD", 15000.0)));
            when(rt.getForObject(contains("2024-01-01"), eq(Map.class)))
                    .thenReturn(Collections.singletonMap("rates", Collections.singletonMap("2024-01-01", Collections.singletonMap("USD", 0.00007))));
            when(rt.getForObject(contains("/currencies"), eq(Map.class)))
                    .thenReturn(Collections.singletonMap("USD", "United States Dollar"));
            return rt;
        }
    }

    @Test
    void contextLoads_andDataServiceIsPopulated() {
        // runner should have executed already and loaded the mocked data
        assertThat(service.isInitialized()).isTrue();
        assertThat(service.getData("latest_idr_rates")).isNotNull();
        assertThat(service.getData("historical_idr_usd")).isNotNull();
        assertThat(service.getData("supported_currencies")).isNotNull();
    }
}