package com.example.allobank.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.example.allobank.calculator.SpreadCalculator;
import com.example.allobank.calculator.SpreadFactorCalculator;
import com.example.allobank.dto.RatesResponseDTO;
import com.example.allobank.dto.SpreadDetailDTO;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SpreadCalculator spreadCalculator;

    @Mock
    private SpreadFactorCalculator spreadFactorCalculator;

    @Mock
    private Environment env;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @Test
    void shouldReturnLatestRatesWithUsdBuySpread() {

        when(env.getProperty("app.github.username"))
                .thenReturn("journalvian-dev");

        Map<String, Object> rates = new HashMap<>();
        rates.put("USD", 0.00006);

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("amount", 1);
        apiResponse.put("base", "IDR");
        apiResponse.put("date", "2026-02-02");
        apiResponse.put("rates", rates);

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(apiResponse);

        when(spreadFactorCalculator.calculateFromUsername(anyString()))
                .thenReturn(new BigDecimal("0.005"));

        when(spreadCalculator.calculate(
                any(BigDecimal.class),
                any(BigDecimal.class)
        )).thenReturn(
                new SpreadDetailDTO(
                        new BigDecimal("16750.00"),
                        new BigDecimal("16583.33")
                )
        );

        Object result = fetcher.fetch();

        assertNotNull(result);
        assertTrue(result instanceof RatesResponseDTO);

        RatesResponseDTO dto = (RatesResponseDTO) result;

        assertNotNull(dto.getRates());
        assertNotNull(dto.getUsdBuySpreadIdr());
        assertEquals(new BigDecimal("16750.00"), dto.getUsdBuySpreadIdr());
    }
}
