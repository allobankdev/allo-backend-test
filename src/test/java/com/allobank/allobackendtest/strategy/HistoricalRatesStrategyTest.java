package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import com.allobank.allobackendtest.strategy.impl.HistoricalRatesStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HistoricalRatesStrategyTest {

    private MockWebServer mockWebServer;

    private HistoricalRatesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException{
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new HistoricalRatesStrategy(webClient);
    }

    @AfterEach
    void tearDown() throws IOException{
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldReturnHistoricalRates_whenApiSuccess(){
        String body = """
        {
          "rates": {
            "2024-01-01": { "USD": 0.000064 },
            "2024-01-02": { "USD": 0.000065 }
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

                    List<Map<String,Object>> list =
                            (List<Map<String,Object>>) response;

                    assertEquals(2, list.size());

                    Map<String,Object> first = list.get(0);

                    assertTrue(first.containsKey("date"));
                    assertTrue(first.containsKey("rate_USD"));
                })
                .verifyComplete();
    }

    @Test
    void fetchData_shouldThrowException_whenApi4xx() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("Bad Request")
        );

        StepVerifier.create(strategy.fetchData())
                .expectErrorMatches(e ->
                        e instanceof ExternalServiceException &&
                                e.getMessage().contains("API unavailable"))
                .verify();
    }

    @Test
    void fetchData_shouldThrowException_whenApi5xx() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal Server Error")
        );

        StepVerifier.create(strategy.fetchData())
                .expectErrorMatches(e ->
                        e instanceof ExternalServiceException &&
                                e.getMessage().contains("API unavailable"))
                .verify();
    }

    @Test
    void supports_shouldReturnTrue_forHistoricalType() {
        assertTrue(strategy.supports("historical_idr_usd"));
    }

    @Test
    void supports_shouldReturnFalse_forOtherType() {
        assertFalse(strategy.supports("latest_idr_rates"));
    }
}
