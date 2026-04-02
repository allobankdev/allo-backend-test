package com.example.idrapi.integration;

import com.example.idrapi.controller.FinanceDataController;
import com.example.idrapi.controller.GlobalExceptionHandler;
import com.example.idrapi.controller.ResourceNotFoundException;
import com.example.idrapi.model.FinanceDataResponse;
import com.example.idrapi.service.FinanceDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanceDataController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("FinanceDataController Web MVC Tests")
class FinanceDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanceDataService financeDataService;

    @Test
    @DisplayName("GET /api/finance/data/latest_idr_rates → 200 OK with data")
    void getLatestIDRRates_returns200() throws Exception {
        FinanceDataResponse mockResponse = new FinanceDataResponse(
                "latest_idr_rates",
                Instant.parse("2024-01-05T00:00:00Z"),
                List.of(Map.of(
                        "base", "IDR",
                        "date", "2024-01-05",
                        "USD_BuySpread_IDR", 15750.25
                ))
        );
        when(financeDataService.getData("latest_idr_rates")).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/finance/data/latest_idr_rates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].USD_BuySpread_IDR").value(15750.25));
    }

    @Test
    @DisplayName("GET /api/finance/data/unknown_type → 404 Not Found")
    void getUnknownResourceType_returns404() throws Exception {
        when(financeDataService.getData("unknown_type")).thenReturn(Optional.empty());
        when(financeDataService.getRegisteredResourceTypes())
                .thenReturn(Set.of("latest_idr_rates", "historical_idr_usd", "supported_currencies"));

        mockMvc.perform(get("/api/finance/data/unknown_type")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("GET /api/finance/data/historical_idr_usd → 200 OK with multiple records")
    void getHistoricalRates_returns200() throws Exception {
        FinanceDataResponse mockResponse = new FinanceDataResponse(
                "historical_idr_usd",
                Instant.now(),
                List.of(
                        Map.of("date", "2024-01-02", "base", "IDR", "USD", 0.000064),
                        Map.of("date", "2024-01-03", "base", "IDR", "USD", 0.000065)
                )
        );
        when(financeDataService.getData("historical_idr_usd")).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/finance/data/historical_idr_usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("historical_idr_usd"))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/finance/data/supported_currencies → 200 OK")
    void getSupportedCurrencies_returns200() throws Exception {
        FinanceDataResponse mockResponse = new FinanceDataResponse(
                "supported_currencies",
                Instant.now(),
                List.of(
                        Map.of("code", "USD", "name", "US Dollar"),
                        Map.of("code", "IDR", "name", "Indonesian Rupiah")
                )
        );
        when(financeDataService.getData("supported_currencies")).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/finance/data/supported_currencies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceType").value("supported_currencies"))
                .andExpect(jsonPath("$.results[0].code").exists())
                .andExpect(jsonPath("$.results[0].name").exists());
    }
}
