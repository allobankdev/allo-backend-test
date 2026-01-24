package com.sdewa.IdrRateAggregator.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLatestIdrRatesEndpoint() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());

    }

    @Test
    void testLatestIdrRatesCalculationEndpoint() throws Exception {

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].usdBuySpreadIdr").exists());
    }

    @Test
    void testHistoricalIdrUsdEndpoint() throws Exception {

        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk());
    }

    @Test
    void testSupportedCurrenciesEndpoint() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk());
    }
}
