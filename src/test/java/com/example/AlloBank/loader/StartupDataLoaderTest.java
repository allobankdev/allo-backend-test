package com.example.AlloBank.loader;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.AlloBank.client.FrankfurterClient;
import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.response.LatestRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(StartupDataLoader.class)
public class StartupDataLoaderTest {

    @MockBean
    private FrankfurterClient frankfurterClient;

    @MockBean
    private FinanceStore financeStore;

    @Test
    void shouldLoadDataOnStartup() throws Exception{
        LatestRatesResponse latest = new LatestRatesResponse();
        HistoricalRatesResponse historical = new HistoricalRatesResponse();
        CurrenciesResponse currencies = new CurrenciesResponse();

        Mockito.when(frankfurterClient.getLatestRates()).thenReturn(latest);
        Mockito.when(frankfurterClient.getHistoricalUsd()).thenReturn(historical);
        Mockito.when(frankfurterClient.getCurrencies()).thenReturn(currencies);

        StartupDataLoader loader = new StartupDataLoader(frankfurterClient, financeStore);
        loader.run(null);
        Mockito.verify(financeStore)
                .initialize(latest, historical, currencies);
    }

}
