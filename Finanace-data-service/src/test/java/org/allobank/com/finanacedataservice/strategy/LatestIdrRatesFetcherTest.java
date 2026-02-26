package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.clent.dto.FrankfurterLatestResponse;
import org.allobank.com.finanacedataservice.domain.LatestIdrRateResult;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.allobank.com.finanacedataservice.service.SpreadFactorCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LatestIdrRatesFetcherTest {
    @Test
    void LoadDataStoresInCache() {
        FrankfurterClient client = Mockito.mock((FrankfurterClient.class));
        SpreadFactorCalculator calc = Mockito.mock(SpreadFactorCalculator.class);
        FinanceDataCache cache = new FinanceDataCache();

        FrankfurterLatestResponse response = new FrankfurterLatestResponse();
        response.setBase("IDR");
        response.setDate(LocalDate.now());
        response.setRates(Map.of("USD", new BigDecimal("0.000064")));

        when(client.getLatestIdrRates()).thenReturn(response);
        when(calc.calculateSpreadFactor()).thenReturn(new BigDecimal("0.00765"));

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(calc);

        fetcher.loadData(client,cache);

        List<?> cached = cache.getLatestIdrRate();
        assertThat(cached).hasSize(1);
        Object element = cached.get(0);
        assertThat(element).isInstanceOf(LatestIdrRateResult.class);
        LatestIdrRateResult result = (LatestIdrRateResult) element;
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getRateUsd()).isEqualByComparingTo("0.000064");
        assertThat(result.getSpreadFactor()).isEqualByComparingTo("0.00765");
    }

    @Test
    void LoadDataNullResponse() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        SpreadFactorCalculator calculator = Mockito.mock(SpreadFactorCalculator.class);
        FinanceDataCache cache = new FinanceDataCache();

        when(client.getLatestIdrRates()).thenReturn(null);
        when(calculator.calculateSpreadFactor()).thenReturn(new BigDecimal("0.00765"));

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(calculator);
        fetcher.loadData(client, cache);

        assertThat(cache.getLatestIdrRate()).isEmpty();
    }

    @Test
    void loadDataNotUsingUsdRate() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        SpreadFactorCalculator calculator = Mockito.mock(SpreadFactorCalculator.class);
        FinanceDataCache cache = new FinanceDataCache();

        FrankfurterLatestResponse response = new FrankfurterLatestResponse();
        response.setBase("IDR");
        response.setDate(LocalDate.now());
        response.setRates(Map.of("EUR", new BigDecimal("0.00006")));

        when(client.getLatestIdrRates()).thenReturn(response);
        when(calculator.calculateSpreadFactor()).thenReturn(new BigDecimal("0.00765"));

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(calculator);

        fetcher.loadData(client,cache);

        assertThat(cache.getLatestIdrRate()).isEmpty();
    }
}