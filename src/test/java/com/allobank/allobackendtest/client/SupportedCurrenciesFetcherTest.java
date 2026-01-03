package com.allobank.allobackendtest.client;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.allobank.allobackendtest.strategy.SupportedCurrenciesFetcher;

@SpringBootTest
class SupportedCurrenciesFetcherTest {

    @Autowired
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldFetchSupportedCurrenciesSuccessfully() {
        try {
            Map<String, String> result = (Map<String, String>) fetcher.fetchData();

            assertThat(result).containsKeys("IDR", "USD", "EUR");

        } catch (IllegalStateException ex) {
            assumeTrue(false, "Skipping test due to network/API issue");
        }
    }

}
