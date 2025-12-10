package com.allo.backend.controller;

import com.allo.backend.service.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "github.username=testuser",
        "frankfurter.api.base-url=https://api.frankfurter.app"
})
class FinanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryDataStore dataStore;

    @Test
    void shouldReturnLatestIDRRates() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.base").value("IDR"))
                .andExpect(jsonPath("$.data.USD_BuySpread_IDR").exists())
                .andExpect(jsonPath("$.data.rates").exists());
    }

    @Test
    void shouldReturnHistoricalIDRUSDRates() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.base").value("IDR"))
                .andExpect(jsonPath("$.data.rates").exists());
    }

    @Test
    void shouldReturnSupportedCurrencies() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.USD").exists())
                .andExpect(jsonPath("$.data.IDR").exists());
    }

    @Test
    void shouldReturnNotFoundForInvalidResourceType() throws Exception {
        mockMvc.perform(get("/api/finance/data/invalid_resource"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }
}
