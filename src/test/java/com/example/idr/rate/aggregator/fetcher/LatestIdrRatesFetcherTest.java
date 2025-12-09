package com.example.idr.rate.aggregator.fetcher;

import com.example.idr.rate.aggregator.dto.LatestIdrRatesDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesFetcherTest {

    static MockWebServer server;
    WebClient client;

    @BeforeAll
    static void startServer() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.shutdown();
    }

    @BeforeEach
    void setup() {
        String baseUrl = server.url("/").toString();
        client = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Test
    void computesUsdBuySpread() throws Exception {
        String body = """
                        {
                          "base": "IDR",
                          "date": "2025-12-01",
                          "rates": {
                            "USD": 0.000060
                          },
                          "usdRate": 0.00006,
                          "usdBuySpreadIdr": 16734.5,
                          "spreadFactor": 0.00407
                        }
                        """;
        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, "primaputraa");
        Object obj = fetcher.fetch().block();
        assertNotNull(obj);
        assertInstanceOf(LatestIdrRatesDto.class, obj);
        LatestIdrRatesDto dto = (LatestIdrRatesDto) obj;

        double expectedRateUsd = 0.000060;
        double expectedSpread = 0.00093;
        double expectedUsdBuy = (1.0 / expectedRateUsd) * (1.0 + expectedSpread);
        assertEquals(expectedUsdBuy, dto.getUsdBuySpreadIdr().doubleValue(), 1e-6);
    }
}
