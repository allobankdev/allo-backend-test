package com.allobank.finance.runner;

import com.allobank.finance.cache.IDRDataFetcherCache;
import com.allobank.finance.model.FinanceData;
import com.allobank.finance.model.LatestRateData;
import com.allobank.finance.registry.IDRDataFetcherRegistry;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializationRunnerTest {

    @Mock
    private IDRDataFetcherRegistry registry;

    @Mock
    private IDRDataFetcher fetcher1;

    @Mock
    private IDRDataFetcher fetcher2;

    @Mock
    private ApplicationArguments args;

    private IDRDataFetcherCache cache;
    private DataInitializationRunner runner;

    @BeforeEach
    void setUp() {
        cache = new IDRDataFetcherCache();
        runner = new DataInitializationRunner(registry, cache);
    }

    @Test
    void shouldInitializeAllDataSuccessfully() throws Exception {
        // Arrange
        FinanceData data1 = createTestData("IDR");
        FinanceData data2 = createTestData("USD");

        when(registry.getAll()).thenReturn(Map.of(
                "type1", fetcher1,
                "type2", fetcher2
        ));
        lenient().when(fetcher1.fetchData()).thenReturn(data1);
        lenient().when(fetcher2.fetchData()).thenReturn(data2);

        // Act
        runner.run(args);

        // Assert
        FinanceData retrieved1 = cache.get("type1");
        FinanceData retrieved2 = cache.get("type2");

        assertThat(retrieved1).isEqualTo(data1);
        assertThat(retrieved2).isEqualTo(data2);
    }

    @Test
    void shouldThrowExceptionWhenFetcherFails() {
        // Arrange
        when(registry.getAll()).thenReturn(Map.of("type1", fetcher1));
        lenient().when(fetcher1.fetchData()).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        assertThatThrownBy(() -> runner.run(args))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("API error");
    }

    private FinanceData createTestData(String base) {
        return LatestRateData.builder()
                .amount(BigDecimal.ONE)
                .base(base)
                .date("2024-01-15")
                .rates(Map.of("USD", new BigDecimal("0.000063")))
                .build();
    }
}
