package com.allobank.frankfurter.controller;

import com.allobank.frankfurter.service.InMemoryDataStore;
import com.allobank.frankfurter.service.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InMemoryDataStore dataStore;

    @MockBean
    private Map<String, IDRDataFetcher> fetcherMap; // to satisfy bean injection

    @Test
    void getData_shouldReturnDataWhenExists() throws Exception {
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("base", "IDR");
        mockData.put("USD_BuySpread_IDR", 15764.84);

        when(dataStore.get("latest_idr_rates")).thenReturn(new com.allobank.frankfurter.model.DataResult("latest_idr_rates", mockData));

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.USD_BuySpread_IDR").value(15764.84));
    }

    @Test
    void getData_shouldReturnNotFoundWhenMissing() throws Exception {
        when(dataStore.get("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/api/finance/data/nonexistent"))
                .andExpect(status().isNotFound());
    }
}