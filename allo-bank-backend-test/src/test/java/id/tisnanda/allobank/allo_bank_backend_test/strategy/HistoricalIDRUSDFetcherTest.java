package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.HistoricalIDRUSDResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.impl.HistoricalIDRUSDFetcher;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalIDRUSDFetcherTest {

    private static final Logger log = Logger.getLogger(HistoricalIDRUSDFetcherTest.class);

    private HistoricalIDRUSDFetcher fetcher;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        fetcher = new HistoricalIDRUSDFetcher();
        fetcher.restTemplate = restTemplate; // set manual
        fetcher.historicalUrl = Constant.MOCK_URL;     // set manual
    }

    @Test
    void testFetchData_withMockedApi() {
        Map<String, Object> usdRate1 = new HashMap<>();
        usdRate1.put(Constant.USD, Constant.RATE_15000);
        Map<String, Object> usdRate2 = new HashMap<>();
        usdRate2.put(Constant.USD, Constant.RATE_15100);

        Map<String, Object> rates = new HashMap<>();
        rates.put(Constant.DATE_2025_12_05, usdRate1);
        rates.put(Constant.DATE_2025_12_06, usdRate2);

        Map<String, Object> response = new HashMap<>();
        response.put(Constant.RATES_KEY, rates);

        when(restTemplate.getForObject(Constant.MOCK_URL, Map.class)).thenReturn(response);

        List<HistoricalIDRUSDResponseDTO> result = fetcher.fetchData();

        assertEquals(2, result.size());

        HistoricalIDRUSDResponseDTO first = result.get(0);
        HistoricalIDRUSDResponseDTO second = result.get(1);

        assertEquals(Constant.DATE_2025_12_05, first.getDate());
        assertEquals(Constant.RATE_15000, first.getUsd());
        assertEquals(Constant.DATE_2025_12_06, second.getDate());
        assertEquals(Constant.RATE_15100, second.getUsd());
    }

    @Test
    void testFetchData_whenRatesMissing_shouldThrowException() {
        Map<String, Object> response = new HashMap<>();
        when(restTemplate.getForObject(Constant.MOCK_URL, Map.class)).thenReturn(response);

        BadRequestException exception = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals(Constant.FAILED_FETCH_HISTORICAL_IDR_USD_RATES, exception.getMessage());
    }
}
