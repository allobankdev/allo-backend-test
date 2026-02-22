package com.allobank.finnance.allobankfinance.controller;

import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategy;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategyResolver;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@WebMvcTest(AlloFinanceController.class)
public class AlloFinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanceDataStrategyResolver financeDataStrategyResolver;

    @MockBean
    private FinanceDataStrategy financeDataStrategy;

    @Test
    void shouldReturnLatestIdrRates() throws Exception {

        String resourceType = "latest_idr_rates";

        Mockito.when(financeDataStrategyResolver.resolve(resourceType))
                .thenReturn(financeDataStrategy);

        Mockito.when(financeDataStrategy.fetchData(Mockito.any(FinanceRequestDto.class)))
                .thenReturn(Map.of("status", "ok"));

        mockMvc.perform(MockMvcRequestBuilders.get("/data/{resourceType}", resourceType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ok"));
    }
}
