package com.allo.integration;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.allo.dto.FinanceResourceResponse;
import com.allo.store.FinanceDataStore;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Data Load Integration Tests")
class DataLoadIntegrationTest {

    @Autowired
    private FinanceDataStore dataStore;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Data store is initialized after application startup")
    void dataStoreIsInitializedOnStartup() {
        assertThat(dataStore.isInitialized()).isTrue();
    }

    @Test
    @DisplayName("latest_idr_rates data is present in the store")
    void latestIdrRatesLoaded() {
        List<FinanceResourceResponse> data = dataStore.getData("latest_idr_rates");
        assertThat(data).isNotEmpty();
        assertThat(data.get(0).resourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    @DisplayName("historical_idr_usd data is present in the store")
    void historicalIdrUsdLoaded() {
        List<FinanceResourceResponse> data = dataStore.getData("historical_idr_usd");
        assertThat(data).isNotEmpty();
        assertThat(data.get(0).resourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    @DisplayName("supported_currencies data is present in the store")
    void supportedCurrenciesLoaded() {
        List<FinanceResourceResponse> data = dataStore.getData("supported_currencies");
        assertThat(data).isNotEmpty();
        assertThat(data.get(0).resourceType()).isEqualTo("supported_currencies");
    }

    @Test
    @DisplayName("GET /api/finance/data/latest_idr_rates returns 200")
    void endpointLatestReturns200() {
        ResponseEntity<List<FinanceResourceResponse>> response = testRestTemplate.exchange(
                "/api/finance/data/latest_idr_rates",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/finance/data/historical_idr_usd returns 200")
    void endpointHistoricalReturns200() {
        ResponseEntity<List<FinanceResourceResponse>> response = testRestTemplate.exchange(
                "/api/finance/data/historical_idr_usd",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/finance/data/supported_currencies returns 200")
    void endpointCurrenciesReturns200() {
        ResponseEntity<List<FinanceResourceResponse>> response = testRestTemplate.exchange(
                "/api/finance/data/supported_currencies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/finance/data/unknown returns 404")
    void endpointUnknownReturns404() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/finance/data/unknown_type",
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
