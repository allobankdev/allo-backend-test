package com.allobank.test.controller;

import com.allobank.test.service.FinanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanceService financeService;

    @Test
    void testGetFinanceData_Success() throws Exception {
        String resourceType = "latest_idr_rates";
        Object mockData = "Mock Data Content";
        when(financeService.getCachedData(resourceType)).thenReturn(mockData);

        mockMvc.perform(get("/api/finance/data/" + resourceType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Mock Data Content")); // Cek Unified JSON Array
    }

    @Test
    void testGetFinanceData_BadRequest() throws Exception {
        when(financeService.getCachedData("ngawur")).thenThrow(new IllegalArgumentException("Invalid Type"));
        mockMvc.perform(get("/api/finance/data/ngawur"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void testGetFinanceData_ServiceUnavailable() throws Exception {
        when(financeService.getCachedData("latest_idr_rates")).thenThrow(new IllegalStateException("Not Ready"));
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Service Unavailable"));
    }
}