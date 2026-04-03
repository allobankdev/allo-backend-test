package com.allo.finance.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FinanceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnLatestRates() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());
    }
}