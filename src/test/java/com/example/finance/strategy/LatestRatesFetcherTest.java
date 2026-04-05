package com.example.finance.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.finance.client.FrankfurterClient;

@SpringBootTest
@ActiveProfiles("test")
public class LatestRatesFetcherTest {

    @Test
    void testFetchData() {

        FrankfurterClient mockClient = mock(FrankfurterClient.class);

        when(mockClient.getLatestRates()).thenReturn(
            "{\"rates\":{\"USD\":0.000064}}"
        );

        LatestRatesFetcher fetcher = new LatestRatesFetcher(mockClient);

        Object result = fetcher.fetchData();

        assertNotNull(result);
    }
}