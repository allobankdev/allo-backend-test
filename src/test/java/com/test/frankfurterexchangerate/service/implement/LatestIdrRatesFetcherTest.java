package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.dto.FrankfurterLatestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private WebClient client;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fetcher = Mockito.spy(new LatestIdrRatesFetcher(
                "https://api.frankfurter.app",
                "sampleUser",
                null,
                restTemplate
        ));
    }

    @Test
    void testFetchData_Success() throws Exception {
        // GIVEN
        FrankfurterLatestDto dto = new FrankfurterLatestDto();
        dto.setBase("IDR");
        dto.setDate("2025-02-10");

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000064); // Kurs 1 IDR → USD
        dto.setRates(rates);

        ResponseEntity<FrankfurterLatestDto> response =
                new ResponseEntity<>(dto, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(
                Mockito.eq("https://api.frankfurter.app/latest?base=IDR"),
                Mockito.eq(FrankfurterLatestDto.class)
        )).thenReturn(response);

        // Mock computeSpreadFactor
        Mockito.spy(fetcher);
        Mockito.doReturn(0.10).when(fetcher).computeSpreadFactor("sampleUser");

        // WHEN
        List<Object> result = fetcher.fetchData();

        // THEN
        Assertions.assertEquals(1, result.size());

        Map<String, Object> out = (Map<String, Object>) result.get(0);

        Assertions.assertEquals("IDR", out.get("base"));
        Assertions.assertEquals("2025-02-10", out.get("date"));
        Assertions.assertEquals(0.000064, out.get("rate"));

        // Spread calculation:
        double expectedBuySpread =
                (1.0 / 0.000064) * (1.0 + 0.10);

        Assertions.assertEquals(expectedBuySpread, out.get("USD_BuySpread_IDR"));
        Assertions.assertEquals(0.10, out.get("spread_factor"));
    }

    @Test
    void testFetchData_RateNull() throws Exception {
        FrankfurterLatestDto dto = new FrankfurterLatestDto();
        dto.setBase("IDR");
        dto.setDate("2025-02-10");

        dto.setRates(null); // NO RATES

        ResponseEntity<FrankfurterLatestDto> response =
                new ResponseEntity<>(dto, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(
                Mockito.anyString(),
                Mockito.eq(FrankfurterLatestDto.class)
        )).thenReturn(response);

        List<Object> result = fetcher.fetchData();
        Map<String, Object> out = (Map<String, Object>) result.get(0);

        Assertions.assertNull(out.get("rate"));
        Assertions.assertNull(out.get("USD_BuySpread_IDR"));
        Assertions.assertNull(out.get("spread_factor"));
    }

    @Test
    void testFetchData_RateZero() throws Exception {
        FrankfurterLatestDto dto = new FrankfurterLatestDto();
        dto.setBase("IDR");
        dto.setDate("2025-02-10");

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.0); // RATE ZERO
        dto.setRates(rates);

        ResponseEntity<FrankfurterLatestDto> response =
                new ResponseEntity<>(dto, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(
                Mockito.anyString(),
                Mockito.eq(FrankfurterLatestDto.class)
        )).thenReturn(response);

        List<Object> result = fetcher.fetchData();
        Map<String, Object> out = (Map<String, Object>) result.get(0);

        Assertions.assertEquals(0.0, out.get("rate"));
        Assertions.assertNull(out.get("USD_BuySpread_IDR"));
        Assertions.assertNull(out.get("spread_factor"));
    }
}