package allobankdev.test.finance.startegy;

import allobankdev.test.finance.client.FrankfurterClient;
import allobankdev.test.finance.strategy.LatestIdrRatesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    FrankfurterClient client;

    LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        fetcher = new LatestIdrRatesFetcher(client);
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        // given
        Map<String, Object> apiResponse = new HashMap<>();
        Map<String, Object> rates = Map.of("USD", 0.000064);
        apiResponse.put("rates", rates);

        when(client.getLatestIdrRates()).thenReturn(apiResponse);

        // when
        Map<String, Object> result =
                (Map<String, Object>) fetcher.fetch();

        // then
        assertTrue(result.containsKey("USD_BuySpread_IDR"));

        double spread = (double) result.get("USD_BuySpread_IDR");
        assertTrue(spread > 0);
    }
}

