package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.CurrenciesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private CurrencyRatesGateway currencyRatesGateway;

    @Test
    void fetch_shouldReturnSupportedCurrencies() {
        CurrenciesDto currenciesDto = new CurrenciesDto(Map.of("USD", "United States Dollar"));
        when(currencyRatesGateway.getCurrencies()).thenReturn(currenciesDto);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(currencyRatesGateway);

        var result = fetcher.fetch();

        assertEquals("supported_currencies", result.resourceType());
        assertEquals(currenciesDto, result.data());
        verify(currencyRatesGateway).getCurrencies();
    }
}
