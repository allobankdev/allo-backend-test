package com.allobank.financeaggregator.runner;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.financeaggregator.dto.SupportedCurrenciesDto;
import com.allobank.financeaggregator.model.FinanceDataItem;
import com.allobank.financeaggregator.service.FinanceDataStore;
import com.allobank.financeaggregator.strategy.IDRDataFetcher;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

class FinanceDataLoaderTest {

    @Test
    void runLoadsAllStrategiesIntoStore() {
        FinanceDataStore store = new FinanceDataStore();
        SupportedCurrenciesDto currencies = new SupportedCurrenciesDto(Map.of("USD", "United States Dollar"));

        Map<String, IDRDataFetcher> strategies = Map.of(
                "supported_currencies",
                () -> currencies
        );

        FinanceDataLoader loader = new FinanceDataLoader(strategies, store);
        loader.run(new DefaultApplicationArguments(new String[0]));

        List<FinanceDataItem<?>> items = store.get("supported_currencies");
        assertThat(items).hasSize(1);
        assertThat(items.get(0).data()).isInstanceOf(SupportedCurrenciesDto.class);
    }
}
