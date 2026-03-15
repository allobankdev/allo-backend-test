package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import com.allobank.allobackendtest.strategy.impl.SupportedCurrenciesStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesStrategyTest {

    private MockWebServer mockWebServer;
    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new SupportedCurrenciesStrategy(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldReturnCurrencyList_whenApiSuccess() {

        String body = """
        {
          "USD": "United States Dollar",
          "EUR": "Euro",
          "JPY": "Japanese Yen"
        }
        """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(body)
                        .addHeader("Content-Type","application/json")
        );

        StepVerifier.create(strategy.fetchData())
                .assertNext(response -> {

                    List<Map<String,String>> list =
                            (List<Map<String,String>>) response;

                    assertEquals(3, list.size());

                    Map<String,String> first = list.get(0);

                    assertTrue(first.containsKey("code"));
                    assertTrue(first.containsKey("name"));
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
    void supports_shouldReturnTrue() {
        assertTrue(strategy.supports("supported_currencies"));
    }

    @Test
    void supports_shouldReturnFalse() {
        assertFalse(strategy.supports("latest_idr_rates"));
    }
}
