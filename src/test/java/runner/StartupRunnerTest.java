package runner;
import com.allobank.idr_rate_aggregator.IdrRateAggregatorApplication;
import com.allobank.idr_rate_aggregator.strategy.DataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = IdrRateAggregatorApplication.class)
public class StartupRunnerTest {
    @Autowired
    private Map<String, DataFetcher> strategies;
    @TestConfiguration
    static class MockConfig {

        @Bean
        @Primary
        public WebClient webClient() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            lenient().when(mockClient.get()).thenReturn(uriSpec);
            lenient().when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
            lenient().when(uriSpec.uri(any(String.class))).thenReturn(headersSpec);
            lenient().when(headersSpec.retrieve()).thenReturn(responseSpec);

            Map<String, Object> dummyResponse = new HashMap<>();
            dummyResponse.put("date", "2024-01-01");

            Map<String, Object> innerRates = new HashMap<>();
            innerRates.put("USD", 0.000065);

            Map<String, Object> ratesMap = new HashMap<>();
            ratesMap.put("2024-01-01", innerRates);
            ratesMap.put("USD", 0.000065);

            dummyResponse.put("rates", ratesMap);
            dummyResponse.put("USD", "United States Dollar");

            lenient().when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(dummyResponse));

            return mockClient;
        }
    }

    @Test
    void verifyDataLoaded_BeforeContextReady() {
        DataFetcher latest = strategies.get("latest_idr_rates");
        assertFalse(latest.fetchData().isEmpty(),
                "GAGAL: Data Latest Rate kosong! Runner mungkin gagal jalan saat startup.");

        DataFetcher history = strategies.get("historical_idr_usd");
        assertFalse(history.fetchData().isEmpty(),
                "GAGAL: Data History kosong! Runner mungkin gagal jalan saat startup.");

        DataFetcher currency = strategies.get("supported_currencies");
    }
}
