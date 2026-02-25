package com.allobank.finance.service;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FinanceDataServiceTest {

    private FinanceDataStore financeDataStore;
    private FinanceDataService financeDataService;

    @BeforeEach
    void setUp() {
        financeDataStore = new FinanceDataStore();
        financeDataService = new FinanceDataService(financeDataStore);
    }

    @Test
    void getByResourceType_shouldReturnStoredData() {
        FinanceDataResponse response = FinanceDataResponse.builder()
                .resourceType("latest_idr_rates")
                .fetchedAt("2024-01-05T08:00:00Z")
                .build();

        financeDataStore.put("latest_idr_rates", response);

        FinanceDataResponse result = financeDataService.getByResourceType("latest_idr_rates");

        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    void getByResourceType_unknownKey_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> financeDataService.getByResourceType("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("unknown");
    }
}
