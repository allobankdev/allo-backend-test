package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.impl.LatestIDRRatesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LatestIDRRatesFetcherTest {

    private LatestIDRRatesFetcher fetcher;
    private RestTemplate mockRestTemplate;
    private String mockUrl = "/latest?base=IDR";

    @BeforeEach
    void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        fetcher = new LatestIDRRatesFetcher();
        fetcher.restTemplate = mockRestTemplate; // set manual
        fetcher.latestUrl = mockUrl;             // set manual
        fetcher.setGithubUsername("tisnandanurhidayat");
    }

    @Test
    void testFetchData_withMockedApi() {
        // Dummy API response
        Map<String, Object> rates = new HashMap<>();
        rates.put("USD", 6.0E-5);
        rates.put("EUR", 5.2E-5);

        Map<String, Object> response = new HashMap<>();
        response.put("rates", rates);

        // Stub RestTemplate
        when(mockRestTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        List<Map<String, Object>> result = fetcher.fetchData();

        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> record = result.get(0);
        assertTrue(record.containsKey("USD_BuySpread_IDR"));
        double usdBuySpread = ((Number) record.get("USD_BuySpread_IDR")).doubleValue();
        assertTrue(usdBuySpread > 0);
    }

    @Test
    void testFetchData_whenRestTemplateIsNull_shouldThrowException() {
        fetcher.restTemplate = null;

        BadRequestException exception = assertThrows(BadRequestException.class, () -> fetcher.fetchData());
        assertEquals("RestTemplate must be set before fetching data", exception.getMessage());
    }

    @Test
    void testFetchData_whenRatesMissing_shouldThrowException() {
        Map<String, Object> response = new HashMap<>();
        when(mockRestTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> fetcher.fetchData());
        assertEquals("Failed to fetch latest IDR rates", exception.getMessage());
    }
}
