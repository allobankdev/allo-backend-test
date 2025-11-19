package com.allo.backend.test.code.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FinanceDataControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetLatestIDRRates() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.usdBuySpreadIDR").exists())
                .andExpect(jsonPath("$.spreadFactorNote").exists());
    }

    @Test
    void testGetHistoricalIDRUSD() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.rates").exists());
    }

    @Test
    void testGetSupportedCurrencies() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").exists())
                .andExpect(jsonPath("$.count").exists());
    }

    @Test
    void testGetInvalidResourceType() throws Exception {
        mockMvc.perform(get("/api/finance/data/invalid_resource"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unknown resource type: invalid_resource"));
    }
}
