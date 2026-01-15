package com.allobank.finance.controller;

import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.allobank.finance.service.IDRService;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IDRService idrService;

    @Test
    void shouldReturn200WhenDataExists() throws Exception {
        when(idrService.getData("latest_idr_rates")).thenReturn(Map.of());

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenDataMissing() throws Exception {
        when(idrService.getData("unknown")).thenReturn(null);

        mockMvc.perform(get("/api/finance/data/unknown"))
                .andExpect(status().isNotFound());
    }
}
