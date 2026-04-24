package com.allobank.backend.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesStrategyTest {

    private static MockWebServer mockBackEnd;
    private LatestIdrRatesStrategy strategy;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testFetchAndTransformData_Success() {
 
        String baseUrl = mockBackEnd.url("/").toString();
        WebClient mockWebClient = WebClient.builder().baseUrl(baseUrl).build();
        
        strategy = new LatestIdrRatesStrategy(mockWebClient, objectMapper);

        String mockJsonResponse = "{\"amount\": 1.0, \"base\": \"IDR\", \"date\": \"2024-04-23\", \"rates\": {\"USD\": 0.000062}}";
        mockBackEnd.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(strategy.fetchAndTransformData())
                .assertNext(jsonNode -> {
                    assertTrue(jsonNode.has("rates"));
                    assertTrue(jsonNode.get("rates").has("USD_BuySpread_IDR"));
                    
                    double calculatedSpread = jsonNode.get("rates").get("USD_BuySpread_IDR").asDouble();
                    assertNotNull(calculatedSpread);
                })
                .verifyComplete();
    }
}