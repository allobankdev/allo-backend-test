package org.allobank.com.finanacedataservice.web;

import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.allobank.com.finanacedataservice.strategy.IdrDataFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(FinanceDataController.class)
class FinanceDataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private FinanceDataCache cache;
    @MockitoBean(name = "latest_idr_rates")
    private IdrDataFetcher latestIdrRatesFetcher;
    @MockitoBean(name = "historical_idr_usd")
    private IdrDataFetcher historicalIdrUsd;
    @MockitoBean(name = "supported_currencies")
    private IdrDataFetcher supported_currencies;

    @Test
    void returnDataForLatestIdrRates() throws Exception {
        List<?> data = List.of("latest_idr_value");
        when(latestIdrRatesFetcher.getCachedData(cache)).thenAnswer(invocationOnMock -> data);

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("latest_idr_value"));

    }

    @Test
    void returnDataForHistoricalIdrUsd() throws Exception {
        List<?> data = List.of("historical_value");
        when(historicalIdrUsd.getCachedData(cache)).thenAnswer(invocationOnMock -> data);

        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("historical_value"));

    }

    @Test
    void returnDataForSupportedCurrencies() throws Exception {
        List<?> data = List.of("supported_currency_value");
        when(supported_currencies.getCachedData(cache)).thenAnswer(invocationOnMock -> data);

        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("supported_currency_value"));

    }


    @Test
    void returnNotFoundForUnknownResourcetype() throws Exception {
        mockMvc.perform(get("/api/finance/data/unkown"))
                .andExpect(status().isNotFound());
    }


}