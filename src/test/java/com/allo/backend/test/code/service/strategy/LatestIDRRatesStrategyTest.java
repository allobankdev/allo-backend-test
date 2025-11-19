package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.LatestRatesData;
import com.allo.backend.test.code.model.dto.LatestRatesResponse;
import com.allo.backend.test.code.util.SpreadFactorCalculator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LatestIDRRatesStrategyTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private LatestIDRRatesStrategy strategy;
    private SpreadFactorCalculator spreadFactorCalculator;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        spreadFactorCalculator = new SpreadFactorCalculator("RadityaDito");
        strategy = new LatestIDRRatesStrategy(spreadFactorCalculator);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }

    @Test
    void testFetchData_Success() {
        String mockResponse = """
                {
                    "amount": 1.0,
                    "base": "IDR",
                    "date": "2025-01-15",
                    "rates": {
                        "USD": 0.00006,
                        "EUR": 0.000055
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Object result = strategy.fetchData(webClient);

        assertInstanceOf(LatestRatesData.class, result);
        LatestRatesData data = (LatestRatesData) result;

        assertEquals(1.0, data.getAmount());
        assertEquals("IDR", data.getBase());
        assertEquals("2025-01-15", data.getDate());
        assertEquals(0.00006, data.getRates().get("USD"));
        assertNotNull(data.getUsdBuySpreadIDR());
        assertEquals(16696.97, data.getUsdBuySpreadIDR(), 1.0);
        assertNotNull(data.getSpreadFactorNote());
    }

    @Test
    void testFetchData_MissingUSDRate() {
        String mockResponse = """
                {
                    "amount": 1.0,
                    "base": "IDR",
                    "date": "2025-01-15",
                    "rates": {
                        "EUR": 0.000055
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        assertThrows(RuntimeException.class, () -> strategy.fetchData(webClient));
    }
}
