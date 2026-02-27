package com.allobank.finance.controller;

import com.allobank.finance.model.response.FinanceDataResponseBuilder;
import com.allobank.finance.service.FinanceDataService;
import com.allobank.finance.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FinanceDataControllerTest {

    @InjectMocks
    FinanceDataController controller;

    @Mock
    FinanceDataService service;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnFinanceDataResponse() throws Exception {
        var resourceType = Constants.LATEST_IDR_RATES;

        var mockResponse = FinanceDataResponseBuilder.builder()
                .resourceType(resourceType)
                .fetchDate(ZonedDateTime.now())
                .data(List.of())
                .build();

        Mockito.when(service.get(resourceType)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/finance/data/{resourceType}", resourceType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
