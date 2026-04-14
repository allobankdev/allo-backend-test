package com.example.frankfurter.runner;

import com.example.frankfurter.client.FrankfurterClient;
import com.example.frankfurter.config.GithubProperties;
import com.example.frankfurter.dto.FrankfurterLatestResponse;
import com.example.frankfurter.dto.FrankfurterTimeseriesResponse;
import com.example.frankfurter.store.FinanceDataStore;
import com.example.frankfurter.strategy.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration-style test:
 * - Memverifikasi bahwa FinanceDataInitializer (ApplicationRunner)
 *   menginisialisasi FinanceDataStore dengan semua resourceType
 *   menggunakan FrankfurterClient yang sudah di-mock.
 */
class FinanceDataInitializerIT {

    @Test
    void shouldInitializeStoreWithAllResourceTypes() throws Exception {
        // Arrange: mock external client
        FrankfurterClient client = mock(FrankfurterClient.class);

        // 1) Stub latest_idr_rates
        FrankfurterLatestResponse latest = new FrankfurterLatestResponse();
        latest.setBase("IDR");
        latest.setDate("2024-01-01");
        latest.setRates(Map.of("USD", 0.00006));
        when(client.getLatestIdrRates()).thenReturn(latest);

        // 2) Stub historical_idr_usd
        FrankfurterTimeseriesResponse timeseries = new FrankfurterTimeseriesResponse();
        timeseries.setBase("IDR");
        timeseries.setStart_date("2024-01-01");
        timeseries.setEnd_date("2024-01-05");
        timeseries.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.00006),
                "2024-01-02", Map.of("USD", 0.00007)
        ));
        when(client.getHistoricalIdrUsd(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(timeseries);

        // 3) Stub supported_currencies
        when(client.getCurrencies()).thenReturn(Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro"
        ));

        // GithubProperties dummy untuk LatestIdrRatesFetcher
        GithubProperties githubProps = new GithubProperties();
        githubProps.setUsername("thasyalarasuci"); // ganti kalau username kamu beda

        IDRDataFetcher latestFetcher = new LatestIdrRatesFetcher(client, githubProps);
        IDRDataFetcher histFetcher = new HistoricalIdrUsdFetcher(client);
        IDRDataFetcher currFetcher = new SupportedCurrenciesFetcher(client);

        IDRDataFetcherRegistry registry = new IDRDataFetcherRegistry(
                List.of(latestFetcher, histFetcher, currFetcher)
        );

        FinanceDataStore store = new FinanceDataStore();
        FinanceDataInitializer initializer = new FinanceDataInitializer(registry, store);

        initializer.run(new DefaultApplicationArguments(new String[]{}));


        List<?> latestData = store.getData(LatestIdrRatesFetcher.RESOURCE_TYPE);
        List<?> historicalData = store.getData(HistoricalIdrUsdFetcher.RESOURCE_TYPE);
        List<?> currencyData = store.getData(SupportedCurrenciesFetcher.RESOURCE_TYPE);

        assertThat(latestData).isNotEmpty();
        assertThat(historicalData).isNotEmpty();
        assertThat(currencyData).isNotEmpty();
    }
}
