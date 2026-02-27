package com.allobank.finance.service.strategy;

import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.integration.frankfurter.dto.SupportedCurrenciesRawResponseBuilder;
import com.allobank.finance.model.response.SupportedCurrenciesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class SupportedCurrenciesFetcherTest {

    @Mock
    FrankfurterClient client;

    @InjectMocks
    SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldMapAndSortSupportedCurrencies() {
        var rawMap = new LinkedHashMap<String, String>();
        rawMap.put("USD", "United States Dollar");
        rawMap.put("AUD", "Australian Dollar");
        rawMap.put("JPY", "Japanese Yen");

        var raw = SupportedCurrenciesRawResponseBuilder.builder()
                .currencies(rawMap)
                .build();

        Mockito.when(client.fetchSupportedCurrencies()).thenReturn(raw);

        var result = (List<SupportedCurrenciesResponse>) fetcher.fetchData();

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("AUD", result.get(0).code());
        Assertions.assertEquals("Australian Dollar", result.get(0).name());

        Assertions.assertEquals("JPY", result.get(1).code());
        Assertions.assertEquals("Japanese Yen", result.get(1).name());

        Assertions.assertEquals("USD", result.get(2).code());
        Assertions.assertEquals("United States Dollar", result.get(2).name());

        Mockito.verify(client).fetchSupportedCurrencies();
    }
}
