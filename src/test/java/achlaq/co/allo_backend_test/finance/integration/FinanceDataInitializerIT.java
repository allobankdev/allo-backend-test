package achlaq.co.allo_backend_test.finance.integration;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FinanceDataInitializerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FrankfurterClient frankfurterClient;

    @Test
    void startupLoadsDataAndEndpointServesCachedData() throws Exception {
        var latest = new achlaq.co.allo_backend_test.external.frankfurter.dto.LatestRatesResponse();
        latest.setBase("IDR");
        latest.setAmount(java.math.BigDecimal.ONE);
        latest.setDate(java.time.LocalDate.now());
        latest.setRates(java.util.Map.of("USD", new java.math.BigDecimal("0.000064")));

        var hist = new achlaq.co.allo_backend_test.external.frankfurter.dto.HistoricalRatesResponse();
        hist.setBase("IDR");
        hist.setRates(java.util.Map.of("2024-01-01", java.util.Map.of("USD", new java.math.BigDecimal("0.000064"))));
        hist.setStartDate(LocalDate.parse("2024-01-01"));
        hist.setEndDate(LocalDate.parse("2024-01-05"));

        var currencies = java.util.Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah");

        when(frankfurterClient.getLatestIdrRates()).thenReturn(latest);
        when(frankfurterClient.getHistoricalIdrUsd()).thenReturn(hist);
        when(frankfurterClient.getCurrencies()).thenReturn(currencies);

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].base").value("IDR"))
                .andExpect(jsonPath("$[0].rates.USD").exists());

        verify(frankfurterClient, times(1)).getLatestIdrRates();
        verify(frankfurterClient, times(1)).getHistoricalIdrUsd();
        verify(frankfurterClient, times(1)).getCurrencies();

        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(frankfurterClient);
    }
}

