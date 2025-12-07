package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
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

    @BeforeEach
    void setUp() {
        fetcher.setLatestUrl(Constant.MOCK_URL_LATEST);
        fetcher.setGithubUsername(Constant.TISNANDA_NUR_HIDAYAT);
    }

    @Test
    void testFetchData_withMockedApi_returnsDto() {
        Map<String, Object> mockedResponse = new HashMap<>();
        mockedResponse.put(Constant.BASE, Constant.IDR);
        mockedResponse.put(Constant.DATE, Constant.DATE_2025_12_07);

        Map<String, Object> rates = new HashMap<>();
        rates.put(Constant.USD, Constant.RATE_15000);
        mockedResponse.put(Constant.RATES_KEY, rates);

        when(restTemplate.getForObject(fetcher.getLatestUrl(), Map.class))
                .thenReturn(mockedResponse);

        List<LatestIDRRateResponseDTO> result = fetcher.fetchData();

        assertEquals(1, result.size());

        LatestIDRRateResponseDTO dto = result.get(0);

        assertEquals(Constant.DATE_2025_12_07, dto.getDate());
        assertEquals(Constant.RATE_15000, dto.getUsd());

        double expectedSpread = (1 / Constant.RATE_15000) * (1 + fetcher.calculateSpreadFactor(Constant.TISNANDA_NUR_HIDAYAT));
        assertEquals(expectedSpread, dto.getUsdBuySpreedIDR(), 1e-6);
    }

    @Test
    void testFetchData_whenRestTemplateIsNull_shouldThrowException() {
        Map<String, Object> response = Collections.emptyMap();

        when(restTemplate.getForObject(fetcher.getLatestUrl(), Map.class)).thenReturn(response);

        BadRequestException ex = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals(Constant.FAILED_FETCH_LATEST_IDR_RATES, ex.getMessage());
    }

    @Test
    void testFetchData_whenRatesMissing_shouldThrowException() {
        Map<String, Object> response = Collections.emptyMap(); // rates missing
        when(restTemplate.getForObject(Constant.MOCK_URL_LATEST, Map.class)).thenReturn(response);

        BadRequestException ex = assertThrows(BadRequestException.class, fetcher::fetchData);
        assertEquals(Constant.FAILED_FETCH_LATEST_IDR_RATES, ex.getMessage());
    }
}
