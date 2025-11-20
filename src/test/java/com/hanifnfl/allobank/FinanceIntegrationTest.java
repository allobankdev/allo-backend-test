package com.hanifnfl.allobank;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinanceIntegrationTest {

    private static MockWebServer mockWebServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerMockFrankfurter(DynamicPropertyRegistry registry) {
        try {
            mockWebServer = new MockWebServer();

            Dispatcher dispatcher = new Dispatcher() {
                @NotNull
                @Override
                public MockResponse dispatch(RecordedRequest request) {
                    String path = request.getPath();
                    if (path != null && path.startsWith("/latest")) {
                        return new MockResponse()
                                .setBody("""
                                    {
                                      "amount": 1.0,
                                      "base": "IDR",
                                      "date": "2024-02-09",
                                      "rates": { "USD": 0.000064 }
                                    }
                                    """)
                                .addHeader("Content-Type", "application/json");
                    } else if (path != null && path.contains("..")) {
                        return new MockResponse()
                                .setBody("""
                                    {
                                      "base": "IDR",
                                      "start_date": "2024-01-01",
                                      "end_date": "2024-01-05",
                                      "rates": {
                                        "2024-01-01": { "USD": 0.000064 },
                                        "2024-01-02": { "USD": 0.000065 }
                                      }
                                    }
                                    """)
                                .addHeader("Content-Type", "application/json");
                    } else if (path != null && path.startsWith("/currencies")) {
                        return new MockResponse()
                                .setBody("""
                                    {
                                      "USD": "United States Dollar",
                                      "EUR": "Euro"
                                    }
                                    """)
                                .addHeader("Content-Type", "application/json");
                    }
                    return new MockResponse().setResponseCode(404);
                }
            };

            mockWebServer.setDispatcher(dispatcher);
            mockWebServer.start();

            String baseUrl = mockWebServer.url("/").toString();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            final String finalBaseUrl = baseUrl;

            registry.add("frankfurter.base-url", () -> finalBaseUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void shutdownMockServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    void applicationRunner_shouldLoadData_andEndpoint_shouldReturnFromCache() {
        // latest_idr_rates
        ResponseEntity<List<Map<String, Object>>> latestResponse =
                restTemplate.exchange(
                        "/api/finance/data/latest_idr_rates",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(latestResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(latestResponse.getBody()).isNotNull().isNotEmpty();

        Map<String, Object> latestFirst = latestResponse.getBody().get(0);
        assertThat(latestFirst).containsKeys(
                "base",
                "date",
                "payload",
                "USD_BuySpread_IDR",
                "spreadFactor"
        );

        // historical_idr_usd
        ResponseEntity<List<Map<String, Object>>> historicalResponse =
                restTemplate.exchange(
                        "/api/finance/data/historical_idr_usd",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(historicalResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(historicalResponse.getBody()).isNotNull().isNotEmpty();

        // supported_currencies
        ResponseEntity<List<Map<String, Object>>> currenciesResponse =
                restTemplate.exchange(
                        "/api/finance/data/supported_currencies",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(currenciesResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(currenciesResponse.getBody()).isNotNull().isNotEmpty();
    }
}
