package cory.sakti.Financial.controller;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
public class FinanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InMemoryDataStoreService dataStore;

    @Test
    @DisplayName("Controller should return 200 and data for valid strategy key")
    void shouldReturnDataForValidResource() throws Exception {
        String resourceKey = "latest_idr_rates"; // can be changed
        Object mockData = Map.of("base", "IDR");

        when(dataStore.get(resourceKey)).thenReturn(mockData);

        mockMvc.perform(get("/api/finance/data/" + resourceKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].base").value("IDR"));
    }

    @Test
    @DisplayName("Controller should return 404 for unknown resource")
    void shouldReturn404ForUnknownResource() throws Exception {
        when(dataStore.get("invalid")).thenReturn(null);

        mockMvc.perform(get("/api/finance/data/invalid"))
                .andExpect(status().isNotFound());
    }
}
