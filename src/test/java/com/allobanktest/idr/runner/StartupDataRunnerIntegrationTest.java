package com.allobanktest.idr.runner;

import com.allobanktest.idr.store.DataStore;
import okhttp3.mockwebserver.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StartupDataRunnerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(StartupDataRunnerIntegrationTest.class);

    static MockWebServer mockWebServer;

    @Autowired
    DataStore dataStore;

    // Responses used by dispatcher
    static final MockResponse LATEST = new MockResponse().setBody("""
            {
              "amount": 1,
              "base": "IDR",
              "date": "2025-12-01",
              "rates": { "USD": 0.00006 }
            }
            """).addHeader("Content-Type", "application/json");

    static final MockResponse CURRENCIES = new MockResponse().setBody("""
            {
              "USD": "United States Dollar",
              "IDR": "Indonesian Rupiah"
            }
            """).addHeader("Content-Type", "application/json");

    static final MockResponse HISTORY = new MockResponse().setBody("""
            {
              "amount": 1,
              "base": "IDR",
              "start_date": "2023-12-29",
              "end_date": "2024-01-05",
              "rates": {
                "2023-12-29": { "USD": 0.000065 },
                "2024-01-02": { "USD": 0.000064 }
              }
            }
            """).addHeader("Content-Type", "application/json");

    @BeforeAll
    static void startServer() throws IOException {
        mockWebServer = new MockWebServer();

        // Deterministic dispatcher responding based on path and query
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request == null) return new MockResponse().setResponseCode(404);
                String path = request.getRequestUrl() == null ? request.getPath() : request.getRequestUrl().encodedPath();
                String query = request.getRequestUrl() == null ? "" : request.getRequestUrl().encodedQuery();
                log.info("MockWebServer received request: path='{}' query='{}'", path, query);

                try {
                    // Latest endpoint: /latest?base=IDR
                    if (path.startsWith("/latest")) return LATEST;

                    // Currencies endpoint: /currencies
                    if (path.startsWith("/currencies")) return CURRENCIES;

                    // Historical endpoint path (may include full path like /2024-01-01..2024-01-05)
                    if (path.contains("2024-01-01..2024-01-05")) return HISTORY;

                    // Also allow query-style historical (defensive)
                    if (path.startsWith("/")) {
                        // fallback: if query contains "from=IDR" and "to=USD"
                        if (query != null && query.contains("from=IDR") && query.contains("to=USD")) return HISTORY;
                    }
                } catch (Exception ex) {
                    log.error("Dispatcher error", ex);
                }
                return new MockResponse().setResponseCode(404);
            }
        });

        mockWebServer.start();
        log.info("MockWebServer started at {}", mockWebServer.url("/"));
    }

    @AfterAll
    static void stopServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
            log.info("MockWebServer shut down");
        }
    }

    // Make Spring use the mock server base url before context initialization
    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        String base = mockWebServer.url("/").toString();
        log.info("Setting app.external.frankfurter-base-url -> {}", base);
        registry.add("app.external.frankfurter-base-url", () -> base);
        registry.add("app.github.username", () -> "brnhrdwnnr");
        // optionally make the startup runner timeout longer for CI environments:
        registry.add("app.startup.timeout-seconds", () -> "60");
    }

    @Test
    void startupLoadsAllResourcesIntoDataStore() throws Exception {
        // Wait briefly for startup runner to finish (DataStore throws if not initialized)
        Map<String, Map<String, Object>> all = dataStore.getAll();

        assertNotNull(all, "DataStore should be initialized by StartupDataRunner");
        assertTrue(all.containsKey("latest_idr_rates"), "latest_idr_rates should exist");
        assertTrue(all.containsKey("historical_idr_usd"), "historical_idr_usd should exist");
        assertTrue(all.containsKey("supported_currencies"), "supported_currencies should exist");

        @SuppressWarnings("unchecked")
        Map<String, Object> latest = all.get("latest_idr_rates");
        assertNotNull(latest, "latest payload should not be null");
        assertNotNull(latest.get("USD_BuySpread_IDR"), "USD_BuySpread_IDR should be present in latest payload");

        // Optional: validate supported_currencies structure
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) all.get("supported_currencies").get("currencies");
        assertNotNull(currencies);
        assertEquals("Indonesian Rupiah", currencies.get("IDR"));
    }
}
