package com.allobank.finance.idrrateaggregator.service;

import com.allobank.finance.idrrateaggregator.exception.ResourceNotFoundException;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FinanceDataStoreTest {

    @Test
    void shouldReturnDataForExistingResourceType() {
        // given
        FinanceDataStore store = new FinanceDataStore();

        Map<String, List<FinanceResponse>> data = Map.of(
                "supported_currencies",
                List.of(new FinanceResponse("USD", "United States Dollar"))
        );

        store.initialize(data);

        // when
        List<FinanceResponse> result =
                store.get("supported_currencies");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getKey()).isEqualTo("USD");
    }

    @Test
    void shouldThrowExceptionWhenResourceTypeNotFound() {
        // given
        FinanceDataStore store = new FinanceDataStore();
        store.initialize(Map.of());

        // then
        assertThatThrownBy(() ->
                store.get("unknown_resource")
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("unknown_resource");
    }

    @Test
    void storedDataShouldBeImmutableAfterInitialization() {
        FinanceDataStore store = new FinanceDataStore();

        Map<String, List<FinanceResponse>> mutableMap =
                new HashMap<>();

        mutableMap.put(
                "latest_idr_rates",
                new ArrayList<>(
                        List.of(new FinanceResponse("USD", 0.00006))
                )
        );

        store.initialize(mutableMap);

        // when: modify original map AFTER initialization
        mutableMap.put(
                "latest_idr_rates",
                List.of(new FinanceResponse("EUR", 0.00005))
        );

        // then: store data remains unchanged
        List<FinanceResponse> stored =
                store.get("latest_idr_rates");

        assertThat(stored).hasSize(1);
        assertThat(stored.get(0).getKey()).isEqualTo("USD");
    }
}
