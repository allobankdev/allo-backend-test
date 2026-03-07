package com.allo.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.allo.dto.FinanceResourceResponse;
import com.allo.exception.DataNotLoadedException;
import com.allo.exception.ResourceNotFoundException;
import com.allo.service.FinanceDataService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceDataController.class)
@DisplayName("FinanceDataController")
class FinanceDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanceDataService financeDataService;

    @Test
    @DisplayName("GET /api/finance/data/{type} returns 200 with data")
    void getDataReturns200() throws Exception {
        List<FinanceResourceResponse> data = List.of(
                new FinanceResourceResponse("latest_idr_rates", Map.of("base", "IDR"))
        );
        when(financeDataService.getDataByResourceType("latest_idr_rates", null, null))
                .thenReturn(data);

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resourceType").value("latest_idr_rates"));
    }

    @Test
    @DisplayName("GET /api/finance/data/unknown returns 404")
    void getDataReturns404ForUnknown() throws Exception {
        when(financeDataService.getDataByResourceType("unknown", null, null))
                .thenThrow(new ResourceNotFoundException("unknown"));

        mockMvc.perform(get("/api/finance/data/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Unknown resource type: unknown"));
    }

    @Test
    @DisplayName("GET returns 503 when data not loaded yet")
    void getDataReturns503WhenNotLoaded() throws Exception {
        when(financeDataService.getDataByResourceType("latest_idr_rates", null, null))
                .thenThrow(new DataNotLoadedException());

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    @DisplayName("GET historical with date range params returns 200")
    void getHistoricalWithDateRange() throws Exception {
        List<FinanceResourceResponse> data = List.of(
                new FinanceResourceResponse("historical_idr_usd", Map.of("base", "IDR"))
        );
        when(financeDataService.getDataByResourceType("historical_idr_usd", "2024-06-01", "2024-06-10"))
                .thenReturn(data);

        mockMvc.perform(get("/api/finance/data/historical_idr_usd")
                        .param("startDate", "2024-06-01")
                        .param("endDate", "2024-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resourceType").value("historical_idr_usd"));
    }
}
