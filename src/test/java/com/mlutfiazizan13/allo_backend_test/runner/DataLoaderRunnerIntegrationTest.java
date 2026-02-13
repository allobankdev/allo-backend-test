package com.mlutfiazizan13.allo_backend_test.runner;

import com.mlutfiazizan13.allo_backend_test.dto.CurrencyMapResponse;
import com.mlutfiazizan13.allo_backend_test.dto.HistoricalRatesResponse;
import com.mlutfiazizan13.allo_backend_test.dto.LatestRatesResponse;
import com.mlutfiazizan13.allo_backend_test.service.IDRDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class DataLoaderRunnerIntegrationTest {

    @MockitoBean
    private RestTemplate restTemplate;

    @Autowired
    private IDRDataStore dataStore;

    @Autowired
    private DataLoaderRunner runner;

    @Test
    void contextLoads_andDataStoreIsInitialized() {
        assertThat(dataStore).isNotNull();
        assertThat(runner).isNotNull();
    }

    @Test
    void runner_shouldPopulateDataStoreWithAllResources() throws Exception {
        // Setup mock responses
        LatestRatesResponse latestResponse = new LatestRatesResponse();
        latestResponse.setAmount(BigDecimal.ONE);
        latestResponse.setBase("IDR");
        latestResponse.setDate("2025-02-11");
        Map<String, BigDecimal> latestRates = new HashMap<>();
        latestRates.put("USD", new BigDecimal("0.0000636998"));
        latestResponse.setRates(latestRates);

        when(restTemplate.getForObject("/latest?base=IDR", LatestRatesResponse.class))
                .thenReturn(latestResponse);

        HistoricalRatesResponse historicalResponse = new HistoricalRatesResponse();
        historicalResponse.setAmount(BigDecimal.ONE);
        historicalResponse.setBase("IDR");
        historicalResponse.setStartDate("2024-01-01");
        historicalResponse.setEndDate("2024-01-05");
        Map<String, Map<String, BigDecimal>> historicalRates = new HashMap<>();
        Map<String, BigDecimal> dayRate = new HashMap<>();
        dayRate.put("USD", new BigDecimal("0.000064"));
        historicalRates.put("2024-01-02", dayRate);
        historicalResponse.setRates(historicalRates);

        when(restTemplate.getForObject(
                "/2024-01-01..2024-01-05?from=IDR&to=USD",
                HistoricalRatesResponse.class))
                .thenReturn(historicalResponse);

        Map<String, String> currenciesMap = new LinkedHashMap<>();
        currenciesMap.put("IDR", "Indonesian Rupiah");
        currenciesMap.put("USD", "United States Dollar");
        ResponseEntity<Map<String, String>> currenciesResponse =
                new ResponseEntity<>(currenciesMap, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(currenciesResponse);

        // Re-execute the runner with mocked responses
        runner.run(null);

        // Verify all 3 resources are loaded
        assertThat(dataStore.getAvailableResources())
                .containsExactlyInAnyOrder(
                        "latest_idr_rates",
                        "historical_idr_usd",
                        "supported_currencies"
                );

        // Verify data types and content
        Object latestData = dataStore.getData("latest_idr_rates");
        assertThat(latestData).isInstanceOf(LatestRatesResponse.class);
        LatestRatesResponse latest = (LatestRatesResponse) latestData;
        assertThat(latest.getBase()).isEqualTo("IDR");
        assertThat(latest.getUsdBuySpreadIdr()).isNotNull();

        Object historicalData = dataStore.getData("historical_idr_usd");
        assertThat(historicalData).isInstanceOf(HistoricalRatesResponse.class);

        Object currenciesData = dataStore.getData("supported_currencies");
        assertThat(currenciesData).isInstanceOf(CurrencyMapResponse.class);
        CurrencyMapResponse currencies = (CurrencyMapResponse) currenciesData;
        assertThat(currencies.getCurrencies()).containsEntry("IDR", "Indonesian Rupiah");
    }
}
