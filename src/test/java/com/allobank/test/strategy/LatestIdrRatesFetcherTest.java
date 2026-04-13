package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import com.allobank.test.config.FrankfurterApiProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    private static final MathContext MATH_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private FrankfurterApiProperties properties;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @Test
    void resourceTypeShouldMatchContract() {
        assertEquals("latest_idr_rates", fetcher.resourceType());
    }

    @Test
    void fetchShouldCalculateUsdBuySpreadIdr() {
        Map<String, Object> rates = new LinkedHashMap<>();
        rates.put("USD", new BigDecimal("0.000064"));
        rates.put("EUR", new BigDecimal("0.000057"));

        Map<String, Object> rawResponse = new LinkedHashMap<>();
        rawResponse.put("base", "IDR");
        rawResponse.put("date", "2024-01-05");
        rawResponse.put("rates", rates);

        when(frankfurterClient.fetchLatestIdrRatesRaw()).thenReturn(rawResponse);
        when(properties.getGithubUsername()).thenReturn("joniheri");

        List<Map<String, Object>> actual = fetcher.fetch();
        Map<String, Object> result = actual.get(0);

        BigDecimal expectedSpreadFactor = calculateSpreadFactor("joniheri");
        BigDecimal expectedUsdBuySpread = BigDecimal.ONE
                .divide(new BigDecimal("0.000064"), MATH_CONTEXT)
                .multiply(BigDecimal.ONE.add(expectedSpreadFactor), MATH_CONTEXT)
                .setScale(8, RoundingMode.HALF_UP);

        @SuppressWarnings("unchecked")
        Map<String, Object> actualRates = (Map<String, Object>) result.get("rates");

        assertEquals(1, actual.size());
        assertEquals("latest_idr_rates", result.get("resourceType"));
        assertEquals("IDR", result.get("base"));
        assertEquals("2024-01-05", result.get("date"));
        assertEquals(expectedSpreadFactor.setScale(5, RoundingMode.HALF_UP), result.get("spread_factor"));
        assertEquals(expectedUsdBuySpread, actualRates.get("USD_BuySpread_IDR"));
    }

    @Test
    void fetchShouldThrowWhenUsdMissing() {
        Map<String, Object> rawResponse = new LinkedHashMap<>();
        rawResponse.put("base", "IDR");
        rawResponse.put("date", "2024-01-05");
        rawResponse.put("rates", Map.of("EUR", new BigDecimal("0.000057")));

        when(frankfurterClient.fetchLatestIdrRatesRaw()).thenReturn(rawResponse);

        assertThrows(IllegalArgumentException.class, () -> fetcher.fetch());
    }

    private static BigDecimal calculateSpreadFactor(String githubUsername) {
        int asciiSum = githubUsername.toLowerCase(Locale.ROOT).chars().sum();
        int modValue = asciiSum % 1000;
        return BigDecimal.valueOf(modValue).divide(BigDecimal.valueOf(100000L), MATH_CONTEXT);
    }
}
