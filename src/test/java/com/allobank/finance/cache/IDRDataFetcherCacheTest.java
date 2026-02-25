package com.allobank.finance.cache;

import com.allobank.finance.exception.BaseException;
import com.allobank.finance.model.FinanceData;
import com.allobank.finance.model.LatestRateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IDRDataFetcherCacheTest {

    private IDRDataFetcherCache cache;

    @BeforeEach
    void setUp() {
        cache = new IDRDataFetcherCache();
    }

    @Test
    void shouldStoreAndRetrieveData() {
        // Arrange
        FinanceData testData = createTestData();
        cache.put("test_resource", testData);
        cache.markInitialized();

        // Act
        FinanceData retrieved = cache.get("test_resource");

        // Assert
        assertThat(retrieved).isEqualTo(testData);
    }

    @Test
    void shouldThrowExceptionWhenAccessingBeforeInitialization() {
        // Arrange
        cache.put("test", createTestData());

        // Act & Assert
        assertThatThrownBy(() -> cache.get("test"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("not yet initialized");
    }

    @Test
    void shouldThrowExceptionWhenStoringAfterInitialization() {
        // Arrange
        cache.put("test", createTestData());
        cache.markInitialized();

        // Act & Assert
        assertThatThrownBy(() -> cache.put("another", createTestData()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("immutable");
    }

    @Test
    void shouldThrowExceptionForUnknownResourceType() {
        // Arrange
        cache.put("known", createTestData());
        cache.markInitialized();

        // Act & Assert
        assertThatThrownBy(() -> cache.get("unknown"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldThrowExceptionWhenMarkingInitializedTwice() {
        // Arrange
        cache.markInitialized();

        // Act & Assert
        assertThatThrownBy(() -> cache.markInitialized())
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("already initialized");
    }

    private FinanceData createTestData() {
        return LatestRateData.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date("2024-01-15")
                .rates(Map.of("USD", new BigDecimal("0.000063")))
                .build();
    }
}
