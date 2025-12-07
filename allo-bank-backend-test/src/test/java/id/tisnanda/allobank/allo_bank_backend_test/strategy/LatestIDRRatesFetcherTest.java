package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.LatestIDRRateResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.impl.LatestIDRRatesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LatestIDRRatesFetcher fetcher;

    private final String mockUrl = "/latest?base=IDR";

    @BeforeEach
    void setUp() {
        fetcher.setLatestUrl(mockUrl);
        fetcher.setGithubUsername("tisnandanurhidayat");
    }

    @Test
    void testFetchData_withMockedApi_returnsDto() {
        Map<String, Object> mockedResponse = new HashMap<>();
        mockedResponse.put("base", "IDR");
        mockedResponse.put("date", "2025-12-07");

        Map<String, Object> rates = new HashMap<>();
        rates.put("USD", 15000.0);
        mockedResponse.put("rates", rates);

        when(restTemplate.getForObject(fetcher.getLatestUrl(), Map.class))
                .thenReturn(mockedResponse);

        List<LatestIDRRateResponseDTO> result = fetcher.fetchData();

        assertEquals(1, result.size());

        LatestIDRRateResponseDTO dto = result.get(0);

        assertEquals("2025-12-07", dto.getDate());
        assertEquals(15000.0, dto.getUSD());

        double expectedSpread = (1 / 15000.0) * (1 + fetcher.calculateSpreadFactor("tisnandanurhidayat"));
        assertEquals(expectedSpread, dto.getUSD_BuySpread_IDR(), 1e-6);
    }

    @Test
    void testFetchData_whenRestTemplateIsNull_shouldThrowException() {
        Map<String, Object> response = Collections.emptyMap();

        when(restTemplate.getForObject(fetcher.getLatestUrl(), Map.class)).thenReturn(response);

        BadRequestException ex = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals("Failed to fetch latest IDR rates", ex.getMessage());
    }

    @Test
    void testFetchData_whenRatesMissing_shouldThrowException() {
        Map<String, Object> response = Collections.emptyMap(); // rates missing
        when(restTemplate.getForObject(mockUrl, Map.class)).thenReturn(response);

        BadRequestException ex = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals("Failed to fetch latest IDR rates", ex.getMessage());
    }
}
