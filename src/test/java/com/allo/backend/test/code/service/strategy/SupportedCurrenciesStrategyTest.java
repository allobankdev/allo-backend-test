package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.CurrenciesData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesStrategyTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new SupportedCurrenciesStrategy();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetResourceType() {
        assertEquals("supported_currencies", strategy.getResourceType());
    }

    @Test
    void testFetchData_Success() {
        String mockResponse = """
                {
                    "USD": "United States Dollar",
                    "EUR": "Euro",
                    "GBP": "British Pound"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Object result = strategy.fetchData(webClient);

        assertInstanceOf(CurrenciesData.class, result);
        CurrenciesData data = (CurrenciesData) result;

        assertNotNull(data.getCurrencies());
        assertEquals(3, data.getCount());
        assertEquals(3, data.getCurrencies().size());
        assertEquals("United States Dollar", data.getCurrencies().get("USD"));
        assertEquals("Euro", data.getCurrencies().get("EUR"));
        assertEquals("British Pound", data.getCurrencies().get("GBP"));
    }
}
