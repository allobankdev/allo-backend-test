package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.impl.HistoricalIDRUSDFetcher;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HistoricalIDRUSDFetcherTest {

    private static final Logger log = Logger.getLogger(HistoricalIDRUSDFetcherTest.class);

    private HistoricalIDRUSDFetcher fetcher;
    private RestTemplate restTemplate;
    private String mockUrl = "http://mock-url";

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        fetcher = new HistoricalIDRUSDFetcher();
        fetcher.restTemplate = restTemplate; // set manual
        fetcher.historicalUrl = mockUrl;     // set manual
    }

    @Test
    void testFetchData_withMockedApi() {
        Map<String, Object> usdRate1 = new HashMap<>();
        usdRate1.put("USD", 15000);
        Map<String, Object> usdRate2 = new HashMap<>();
        usdRate2.put("USD", 15100);

        Map<String, Object> rates = new HashMap<>();
        rates.put("2025-12-05", usdRate1);
        rates.put("2025-12-06", usdRate2);

        Map<String, Object> response = new HashMap<>();
        response.put("rates", rates);

        when(restTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        List<Map<String, Object>> result = fetcher.fetchData();

        assertEquals(2, result.size());
        assertEquals("2025-12-05", result.get(0).get("date"));
        assertEquals(15000, result.get(0).get("USD"));
        assertEquals("2025-12-06", result.get(1).get("date"));
        assertEquals(15100, result.get(1).get("USD"));
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
        when(restTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> fetcher.fetchData());
        assertEquals("Failed to fetch historical IDR->USD rates", exception.getMessage());
    }
}
