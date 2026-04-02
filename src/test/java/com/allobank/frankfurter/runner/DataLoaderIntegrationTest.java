package com.allobank.frankfurter.runner;

import com.allobank.frankfurter.model.DataResult;
import com.allobank.frankfurter.service.InMemoryDataStore;
import com.allobank.frankfurter.service.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class DataLoaderIntegrationTest {

    @Autowired
    private InMemoryDataStore dataStore;

    @MockBean(name = "latestRatesFetcher")
    private IDRDataFetcher latestRatesFetcher;

    @MockBean(name = "historicalRatesFetcher")
    private IDRDataFetcher historicalRatesFetcher;

    @MockBean(name = "currenciesFetcher")
    private IDRDataFetcher currenciesFetcher;

    @Test
    void dataLoader_shouldPopulateDataStore_whenFetchersReturnValidData() {
        when(latestRatesFetcher.getResourceType()).thenReturn("latest_idr_rates");
        when(latestRatesFetcher.fetchData()).thenReturn(new DataResult("latest_idr_rates", Map.of("base", "IDR")));

        when(historicalRatesFetcher.getResourceType()).thenReturn("historical_idr_usd");
        when(historicalRatesFetcher.fetchData()).thenReturn(new DataResult("historical_idr_usd", Map.of("base", "IDR")));

        when(currenciesFetcher.getResourceType()).thenReturn("supported_currencies");
        when(currenciesFetcher.fetchData()).thenReturn(new DataResult("supported_currencies", Map.of("IDR", "Indonesian Rupiah")));

        // DataLoader is ApplicationRunner and runs during startup; verify the store was populated.
        assertThat(dataStore.get("latest_idr_rates")).isNotNull();
        assertThat(dataStore.get("historical_idr_usd")).isNotNull();
        assertThat(dataStore.get("supported_currencies")).isNotNull();
    }
}
