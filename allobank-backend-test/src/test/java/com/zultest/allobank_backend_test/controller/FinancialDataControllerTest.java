package com.zultest.allobank_backend_test.controller;

import com.zultest.allobank_backend_test.store.InMemoryStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(FinancialDataController.class)
public class FinancialDataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InMemoryStore store;

    @Test
    void shouldReturnDataForValidResourceType() throws Exception {
        when(store.get("latest_idr_rates"))
                .thenReturn(Map.of("USD", 0.000064));

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorForInvalidResourceType() throws Exception {
        when(store.get("unknown"))
                .thenThrow(new IllegalArgumentException("No data found"));

        mockMvc.perform(get("/api/finance/data/unknown"))
                .andExpect(status().isBadRequest());
    }
}
