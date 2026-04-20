package com.allobank.backendtest;

import com.allobank.backendtest.service.FinanceDataService;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendtestApplicationTests {

    static MockWebServer mockWebServer;

    // --- Mock Data ---

    private static final String LATEST_JSON = """
            {"amount":1.0,"base":"IDR","date":"2026-04-17","rates":{"USD":0.000058,"EUR":0.000049}}
            """;

    private static final String HISTORICAL_JSON = """
            {"amount":1.0,"base":"IDR","start_date":"2023-12-29","end_date":"2024-01-05","rates":{"2023-12-29":{"USD":0.000065}}}
            """;

    private static final String CURRENCIES_JSON = """
            {"IDR":"Indonesian Rupiah","USD":"United States Dollar"}
            """;

    // --- Lifecycle Management ---

    @BeforeAll
    static void startMockServer() throws IOException {
        mockWebServer = new MockWebServer();
        
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();
                if (path != null) {
                    if (path.startsWith("/latest")) {
                        return jsonResponse(LATEST_JSON);
                    } else if (path.contains("2024-01-01")) {
                        return jsonResponse(HISTORICAL_JSON);
                    } else if (path.startsWith("/currencies")) {
                        return jsonResponse(CURRENCIES_JSON);
                    }
                }
                return new MockResponse().setResponseCode(404);
            }
        });
        
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownMockServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Point the Frankfurter API base URL to our local mock server
        registry.add("frankfurter.api.base-url", () -> "http://localhost:" + mockWebServer.getPort());
    }

    @Autowired
    private FinanceDataService financeDataService;

    // --- Tests ---

    @Test
    void contextLoads() {
        // Basic sanity check: did the data load correctly on startup?
        assertThat(financeDataService.isDataLoaded()).isTrue();
    }

    private static MockResponse jsonResponse(String body) {
        return new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(body);
    }
}
