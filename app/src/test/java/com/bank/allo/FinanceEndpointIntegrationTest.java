package com.bank.allo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FinanceEndpointIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void getLatest_returnsMappedLatestRatesWithSpread() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.base").value("IDR"))
                .andExpect(jsonPath("$.data.usdBuySpreadIdr").exists())
                .andExpect(jsonPath("$.data.spreadFactor").exists());
    }
}
