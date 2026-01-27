package allobankdev.test.finance.startegy;

import allobankdev.test.finance.client.FrankfurterClient;
import allobankdev.test.finance.strategy.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    FrankfurterClient client;

    @Test
    void shouldReturnCurrencies() {
        Map<String, Object> mock = Map.of("USD", "US Dollar");

        when(client.getCurrencies()).thenReturn(mock);

        SupportedCurrenciesFetcher fetcher =
                new SupportedCurrenciesFetcher(client);

        Object result = fetcher.fetch();

        assertEquals(mock, result);
    }
}

