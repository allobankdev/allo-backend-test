package allobankdev.test.finance.startegy;

import allobankdev.test.finance.client.FrankfurterClient;
import allobankdev.test.finance.strategy.HistoricalIdrUsdFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    FrankfurterClient client;

    @Test
    void shouldReturnHistoricalData() {
        Map<String, Object> mock = Map.of("rates", Map.of());

        when(client.getHistoricalIdrUsd()).thenReturn(mock);

        HistoricalIdrUsdFetcher fetcher =
                new HistoricalIdrUsdFetcher(client);

        Object result = fetcher.fetch();

        assertEquals(mock, result);
    }
}

