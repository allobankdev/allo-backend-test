package com.bank.allo.utils;

import com.bank.allo.domain.idr.HistoricalRates;
import com.bank.allo.domain.idr.LatestRates;
import com.bank.allo.domain.idr.SupportedCurrencies;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class IdrRateMapperUtilsTest {

    @Test
    void testToLatestRatesMappingCorrect() {
        Map<String, Object> raw = Map.of(
                "base", "IDR",
                "date", "2024-01-01",
                "rates", Map.of("USD", 0.000065)
        );

        String username = "putrasaputra";

        LatestRates result = IdrRateMapperUtils.toLatestRates(raw, username);

        assertEquals("IDR", result.getBase());
        assertEquals("2024-01-01", result.getDate());
        assertNotNull(result.getUsdBuySpreadIdr());
        assertNotNull(result.getSpreadFactor());

        double usdRate = 0.000065;
        double expected = (1.0 / usdRate) * (1 + result.getSpreadFactor());

        assertEquals(expected, result.getUsdBuySpreadIdr());
    }

    @Test
    void testToLatestRatesWhenUsdMissing() {
        Map<String, Object> raw = Map.of(
                "base", "IDR",
                "date", "2024-01-02",
                "rates", Map.of("EUR", 0.000061)
        );

        LatestRates result = IdrRateMapperUtils.toLatestRates(raw, "testUser");

        assertNull(result.getUsdBuySpreadIdr());
    }

    @Test
    void testToHistoricalRatesMapping() {
        Map<String, Object> raw = Map.of(
                "start_date", "2024-01-01",
                "end_date", "2024-01-05",
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.000065),
                        "2024-01-02", Map.of("USD", 0.000066)
                )
        );

        HistoricalRates hist = IdrRateMapperUtils.toHistoricalRates(raw);

        assertEquals("2024-01-01", hist.getStartDate());
        assertEquals("2024-01-05", hist.getEndDate());
        assertEquals(2, hist.getRates().size());
    }

    @Test
    void testToHistoricalRatesWithDefaults() {
        Map<String, Object> raw = Map.of(); // empty

        HistoricalRates hist = IdrRateMapperUtils.toHistoricalRates(raw);

        assertEquals("2024-01-01", hist.getStartDate());
        assertEquals("2024-01-05", hist.getEndDate());
        assertEquals(0, hist.getRates().size());
    }

    @Test
    void testToSupportedCurrencies() {
        Map<String, String> raw = Map.of(
                "USD", "United States Dollar",
                "JPY", "Japanese Yen"
        );

        SupportedCurrencies result = IdrRateMapperUtils.toSupportedCurrencies(raw);

        assertEquals(2, result.getCurrencies().size());
        assertTrue(result.getCurrencies().containsKey("USD"));
        assertTrue(result.getCurrencies().containsKey("JPY"));
    }
}
