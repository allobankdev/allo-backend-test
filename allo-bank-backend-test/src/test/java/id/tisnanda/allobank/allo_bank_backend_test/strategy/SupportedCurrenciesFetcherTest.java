package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
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
//    private String mockUrl = "http://mock-currencies-url";

    @BeforeEach
    void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        fetcher = new SupportedCurrenciesFetcher();
        fetcher.restTemplate = mockRestTemplate;
        fetcher.currenciesUrl = Constant.MOCK_CURRENCIES_URL;
    }

    @Test
    void testFetchData_withMockedApi() {
        Map<String, String> response = new HashMap<>();
        response.put(Constant.USD_CODE, Constant.USD_NAME);
        response.put(Constant.EUR_CODE, Constant.EUR_NAME);
        response.put(Constant.JPY_CODE, Constant.JPY_NAME);

        when(mockRestTemplate.getForObject(Constant.MOCK_CURRENCIES_URL, Map.class)).thenReturn(response);

        List<CurrenciesResponseDTO> result = fetcher.fetchData();

        // Hanya satu DTO karena kita bungkus Map di DTO
        assertEquals(1, result.size());

        CurrenciesResponseDTO dto = result.get(0);
        Map<String, String> currencies = dto.getCurrencies();

        assertEquals(3, currencies.size());
        assertTrue(currencies.containsKey(Constant.USD));
        assertEquals(Constant.USD_NAME, currencies.get(Constant.USD));
    }

    @Test
    void testFetchData_whenRestTemplateIsNull_shouldThrowException() {
        fetcher.restTemplate = null;

        BadRequestException exception = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals(Constant.REST_TEMPLATE_NOT_SET, exception.getMessage());
    }

    @Test
    void testFetchData_whenResponseNull_shouldThrowException() {
        when(mockRestTemplate.getForObject(Constant.MOCK_CURRENCIES_URL, Map.class)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals(Constant.FAILED_FETCH_SUPPORTED_CURRENCIES, exception.getMessage());
    }
}
