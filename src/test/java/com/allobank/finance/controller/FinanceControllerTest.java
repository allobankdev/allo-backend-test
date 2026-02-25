package com.allobank.finance.controller;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.exception.ResourceNotFoundException;
import com.allobank.finance.service.FinanceDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanceDataService financeDataService;

    @Test
    void getFinanceData_latestIdrRates_shouldReturn200() throws Exception {
        FinanceDataResponse mockResponse = FinanceDataResponse.builder()
                .resourceType("latest_idr_rates")
                .fetchedAt("2024-01-05T08:00:00Z")
                .data(Map.of("USD", 0.000064))
                .usdBuySpreadIdr(15666.75)
                .spreadFactor(0.00264)
                .build();

        when(financeDataService.getByResourceType("latest_idr_rates")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/finance/data/latest_idr_rates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.usdBuySpreadIdr").value(15666.75))
                .andExpect(jsonPath("$.spreadFactor").value(0.00264));
    }

    @Test
    void getFinanceData_invalidResourceType_shouldReturn404() throws Exception {
        when(financeDataService.getByResourceType("invalid_type"))
                .thenThrow(new ResourceNotFoundException("Resource type 'invalid_type' not found"));

        mockMvc.perform(get("/api/finance/data/invalid_type")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFinanceData_historicalIdrUsd_shouldReturn200() throws Exception {
        FinanceDataResponse mockResponse = FinanceDataResponse.builder()
                .resourceType("historical_idr_usd")
                .fetchedAt("2024-01-05T08:00:00Z")
                .data(Map.of("2024-01-02", Map.of("USD", 0.000064)))
                .build();

        when(financeDataService.getByResourceType("historical_idr_usd")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/finance/data/historical_idr_usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("historical_idr_usd"))
                .andExpect(jsonPath("$.usdBuySpreadIdr").doesNotExist());
    }

    @Test
    void getFinanceData_supportedCurrencies_shouldReturn200() throws Exception {
        FinanceDataResponse mockResponse = FinanceDataResponse.builder()
                .resourceType("supported_currencies")
                .fetchedAt("2024-01-05T08:00:00Z")
                .data(Map.of("IDR", "Indonesian Rupiah", "USD", "US Dollar"))
                .build();

        when(financeDataService.getByResourceType("supported_currencies")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/finance/data/supported_currencies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("supported_currencies"));
    }

    @Test
    void getFinanceData_withPostMethod_shouldReturn405() throws Exception {
        mockMvc.perform(post("/api/finance/data/latest_idr_rates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}