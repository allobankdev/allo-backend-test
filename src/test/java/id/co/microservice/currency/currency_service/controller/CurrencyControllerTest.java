package id.co.microservice.currency.currency_service.controller;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    void testGetCurrency_ReturnsResponseDto() throws Exception {
        // Arrange: mock service response
        CurrencyResponseDto mockResponse = new CurrencyResponseDto();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");

        when(currencyService.executeStrategy("latest_idr_usd")).thenReturn(mockResponse);

        // Act & Assert: perform GET request
        mockMvc.perform(get("/api/finance/data/latest_idr_usd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.date").value("2024-01-05"));
    }

}