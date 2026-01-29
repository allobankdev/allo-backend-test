package com.allobank.financeaggregator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allobank.financeaggregator.exception.DataNotLoadedException;
import com.allobank.financeaggregator.exception.ResourceNotFoundException;
import com.allobank.financeaggregator.model.FinanceDataItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FinanceDataStoreTest {

    @Test
    void getBeforeLoadThrows() {
        FinanceDataStore store = new FinanceDataStore();

        assertThatThrownBy(() -> store.get("latest_idr_rates"))
                .isInstanceOf(DataNotLoadedException.class);
    }

    @Test
    void loadAndGetReturnsImmutableCopy() {
        FinanceDataStore store = new FinanceDataStore();

        List<FinanceDataItem<?>> items = new ArrayList<>();
        items.add(new FinanceDataItem<>("latest_idr_rates", Map.of("USD", 1)));

        Map<String, List<FinanceDataItem<?>>> data = new HashMap<>();
        data.put("latest_idr_rates", items);

        store.load(data);

        List<FinanceDataItem<?>> stored = store.get("latest_idr_rates");
        assertThat(stored).hasSize(1);

        items.add(new FinanceDataItem<>("latest_idr_rates", Map.of("EUR", 2)));
        assertThat(store.get("latest_idr_rates")).hasSize(1);

        assertThatThrownBy(() -> stored.add(new FinanceDataItem<>("latest_idr_rates", Map.of("GBP", 3))))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void loadTwiceThrows() {
        FinanceDataStore store = new FinanceDataStore();

        Map<String, List<FinanceDataItem<?>>> data = Map.of(
                "latest_idr_rates",
                List.of(new FinanceDataItem<>("latest_idr_rates", Map.of("USD", 1)))
        );

        store.load(data);

        assertThatThrownBy(() -> store.load(data))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already loaded");
    }

    @Test
    void unknownResourceThrows() {
        FinanceDataStore store = new FinanceDataStore();

        Map<String, List<FinanceDataItem<?>>> data = Map.of(
                "latest_idr_rates",
                List.of(new FinanceDataItem<>("latest_idr_rates", Map.of("USD", 1)))
        );

        store.load(data);

        assertThatThrownBy(() -> store.get("unknown"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
