package com.allobank.idr_rate_aggregator.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the Finance Data Controller.
 * Tests the complete flow from HTTP request to response.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "app.github-username=test-user",
        "frankfurter.api.base-url=https://api.frankfurter.app"
})
class FinanceDataControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetLatestIDRRates_Success() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.rates").exists())
                .andExpect(jsonPath("$.USD_BuySpread_IDR").exists())
                .andExpect(jsonPath("$.github_username").exists());
    }

    @Test
    void testGetHistoricalIDRUSD_Success() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.rates").exists());
    }

    @Test
    void testGetSupportedCurrencies_Success() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@=='USD')]").exists())
                .andExpect(jsonPath("$[?(@=='IDR')]").exists());
    }

    @Test
    void testGetInvalidResourceType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/finance/data/invalid_type"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
