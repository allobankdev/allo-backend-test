package com.allobank.financeaggregator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allobank.financeaggregator.dto.LatestIdrRatesDto;
import com.allobank.financeaggregator.exception.ApiExceptionHandler;
import com.allobank.financeaggregator.model.FinanceDataItem;
import com.allobank.financeaggregator.service.FinanceDataStore;
import com.allobank.financeaggregator.strategy.IDRDataFetcher;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class FinanceDataControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FinanceDataStore store = new FinanceDataStore();
        LatestIdrRatesDto payload = new LatestIdrRatesDto(
                new BigDecimal("1.0"),
                "IDR",
                "2024-01-05",
                Map.of("USD", new BigDecimal("0.000065")),
                new BigDecimal("15000.0")
        );

        store.load(Map.of(
                "latest_idr_rates",
                List.of(new FinanceDataItem<>("latest_idr_rates", payload))
        ));

        Map<String, IDRDataFetcher> strategies = Map.of(
                "latest_idr_rates",
                () -> payload
        );

        FinanceDataController controller = new FinanceDataController(strategies, store);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getDataReturnsPayload() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.data[0].data.base").value("IDR"))
                .andExpect(jsonPath("$.data[0].data.USD_BuySpread_IDR").value(15000.0));
    }

    @Test
    void unknownResourceReturns404() throws Exception {
        mockMvc.perform(get("/api/finance/data/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }
}
