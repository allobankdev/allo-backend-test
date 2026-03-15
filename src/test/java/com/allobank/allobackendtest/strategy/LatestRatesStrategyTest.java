package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.strategy.impl.LatestRatesStrategy;
import com.allobank.allobackendtest.util.SpreadFactorCalculator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestRatesStrategyTest {
    private MockWebServer mockWebServer;

    @Mock
    private SpreadFactorCalculator spreadFactorCalculator;

    private LatestRatesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new LatestRatesStrategy(webClient, spreadFactorCalculator);
    }
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldCalculateSpread_whenUsdExists() {

        String body = """
                {
                  "rates": {
                    "USD": 0.000064
                  }
                }
                """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(body)
                        .addHeader("Content-Type", "application/json")
        );

        when(spreadFactorCalculator.calculate()).thenReturn(0.02);

        Mono<Object> result = strategy.fetchData();

        StepVerifier.create(result)
                .assertNext(response -> {

                    Map<String, Object> map = (Map<String, Object>) response;
                    Map<String, Object> rates = (Map<String, Object>) map.get("rates");

                    assertNotNull(rates.get("USD"));
                    assertTrue(map.containsKey("USD_BuySpread_IDR"));

                    Double usdSpread = (Double) map.get("USD_BuySpread_IDR");
                    assertTrue(usdSpread > 0);
                })
                .verifyComplete();
    }
    @Test
    void fetchData_shouldNotAddSpread_whenUsdMissing() {

        String body = """
                {
                  "rates": {
                    "EUR": 0.000055
                  }
                }
                """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(body)
                        .addHeader("Content-Type", "application/json")
        );

        Mono<Object> result = strategy.fetchData();

        StepVerifier.create(result)
                .assertNext(response -> {

                    Map<String, Object> map = (Map<String, Object>) response;

                    assertFalse(map.containsKey("USD_BuySpread_IDR"));
                })
                .verifyComplete();
    }

    @Test
    void supports_shouldReturnTrue_forLatestRates() {
        assertTrue(strategy.supports("latest_idr_rates"));
    }

    @Test
    void supports_shouldReturnFalse_forOtherResource() {
        assertFalse(strategy.supports("historical_rates"));
    }

}
