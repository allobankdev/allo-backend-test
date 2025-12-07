package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.CurrenciesResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.impl.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportedCurrenciesFetcherTest {

    private SupportedCurrenciesFetcher fetcher;
    private RestTemplate mockRestTemplate;
    private String mockUrl = "http://mock-currencies-url";

    @BeforeEach
    void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        fetcher = new SupportedCurrenciesFetcher();
        fetcher.restTemplate = mockRestTemplate;
        fetcher.currenciesUrl = mockUrl;
    }

    @Test
    void testFetchData_withMockedApi() {
        Map<String, String> response = new HashMap<>();
        response.put("USD", "United States Dollar");
        response.put("EUR", "Euro");
        response.put("JPY", "Japanese Yen");

        when(mockRestTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        List<CurrenciesResponseDTO> result = fetcher.fetchData();

        // Hanya satu DTO karena kita bungkus Map di DTO
        assertEquals(1, result.size());

        CurrenciesResponseDTO dto = result.get(0);
        Map<String, String> currencies = dto.getCurrencies();

        assertEquals(3, currencies.size());
        assertTrue(currencies.containsKey("USD"));
        assertEquals("United States Dollar", currencies.get("USD"));
    }

    @Test
    void testFetchData_whenRestTemplateIsNull_shouldThrowException() {
        fetcher.restTemplate = null;

        BadRequestException exception = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals("RestTemplate must be set before fetching data", exception.getMessage());
    }

    @Test
    void testFetchData_whenResponseNull_shouldThrowException() {
        when(mockRestTemplate.getForObject(mockUrl, Map.class)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals("Failed to fetch supported currencies", exception.getMessage());
    }
}
