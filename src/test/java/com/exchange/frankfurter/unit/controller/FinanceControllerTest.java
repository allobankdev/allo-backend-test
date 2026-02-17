package com.exchange.frankfurter.unit.controller;

import com.exchange.frankfurter.controller.FinanceController;
import com.exchange.frankfurter.store.FinanceDataStore;
import com.exchange.frankfurter.strategy.IDRDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FinanceControllerTest {

    private MockMvc mockMvc;
    private FinanceDataStore dataStore;
    private IDRDataFetcher mockFetcher;

    @BeforeEach
    void setup() {
        dataStore = Mockito.mock(FinanceDataStore.class);
        mockFetcher = Mockito.mock(IDRDataFetcher.class);

        when(mockFetcher.getResourceType()).thenReturn("latest_idr_rates");

        FinanceController controller = new FinanceController(
                List.of(mockFetcher),
                dataStore
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnOkStatus() throws Exception {
        // GIVEN
        Map<String, String> mockData = Map.of("USD", "15000");
        when(dataStore.get("latest_idr_rates")).thenReturn(mockData);

        // WHEN & THEN
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.data.USD").value("15000"));
    }
}