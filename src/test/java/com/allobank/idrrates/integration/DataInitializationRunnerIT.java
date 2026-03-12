package com.allobank.idrrates.integration;

import com.allobank.idrrates.service.DataStoreService;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataInitializationRunnerIT {

    static MockWebServer mockWebServer;

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void startMockServer() throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();

                if (path.startsWith("/latest")) {
                    return new MockResponse()
                            .setBody("""
                                    {
                                      "base": "IDR",
                                      "date": "2024-01-05",
                                      "rates": {
                                        "USD": 0.0000631,
                                        "EUR": 0.0000574
                                      }
                                    }
                                    """)
                            .addHeader("Content-Type", "application/json");
                }

                if (path.startsWith("/2024-01-01")) {
                    return new MockResponse()
                            .setBody("""
                                    {
                                      "base": "IDR",
                                      "start_date": "2024-01-01",
                                      "end_date": "2024-01-05",
                                      "rates": {
                                        "2024-01-02": { "USD": 0.0000638 },
                                        "2024-01-03": { "USD": 0.0000642 }
                                      }
                                    }
                                    """)
                            .addHeader("Content-Type", "application/json");
                }

                if (path.startsWith("/currencies")) {
                    return new MockResponse()
                            .setBody("""
                                    {
                                      "USD": "United States Dollar",
                                      "EUR": "Euro",
                                      "IDR": "Indonesian Rupiah"
                                    }
                                    """)
                            .addHeader("Content-Type", "application/json");
                }

                return new MockResponse().setResponseCode(404);
            }
        });

        mockWebServer.start();
    }

    @AfterAll
    static void stopMockServer() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("frankfurter.base-url", () -> mockWebServer.url("/").toString());
    }

    @Test
    void dataStoreIsInitializedAfterStartup() {
        assertTrue(dataStoreService.isInitialized());
        assertNotNull(dataStoreService.getData("latest_idr_rates"));
        assertNotNull(dataStoreService.getData("historical_idr_usd"));
        assertNotNull(dataStoreService.getData("supported_currencies"));
    }

    @Test
    void latestRatesEndpointReturns200() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void historicalEndpointReturns200() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("historical_idr_usd"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void currenciesEndpointReturns200() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("supported_currencies"))
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    void invalidResourceTypeReturns404() throws Exception {
        mockMvc.perform(get("/api/finance/data/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
