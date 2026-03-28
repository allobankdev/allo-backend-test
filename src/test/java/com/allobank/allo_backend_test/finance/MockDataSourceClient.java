package com.allobank.allo_backend_test.finance;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.dto.CurrenciesDto;
import com.allobank.allo_backend_test.finance.model.dto.HistoricalRatesDto;
import com.allobank.allo_backend_test.finance.model.dto.LatestRateDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.*;

@TestConfiguration
public class MockDataSourceClient {

    @Bean
    @Primary
    public DataSourceClient mockClient() {
        DataSourceClient mock = mock(DataSourceClient.class);

        when(mock.getWithParams("/latest", Map.of("base", "IDR"), LatestRateDto.class)).thenReturn(
                new LatestRateDto(1.0, "IDR", LocalDate.now(), Map.of("USD", 0.000064)));

        when(mock.getWithParams("/2024-01-01..2024-01-05", Map.of("from", "IDR", "to", "USD"), HistoricalRatesDto.class)).thenReturn(
                new HistoricalRatesDto(1.0, "IDR",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 1, 5),
                        Map.of("2024-01-01", Map.of("USD", 0.000064))));

        CurrenciesDto currencies = new CurrenciesDto();
        currencies.put("USD", "United States Dollar");
        when(mock.get("/currencies", CurrenciesDto.class)).thenReturn(currencies);

        return mock;
    }
}