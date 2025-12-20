package com.allo_backend_test.strategy;

import com.allo_backend_test.client.FrankfurterApiClient;
import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.dto.LatestRatesResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LatestIdrRatesStrategyTest {

    @Test
    void shouldReturnUsdBuySpreadIdrWithCorrectCalculation() {

        FrankfurterApiClient apiClient =
                Mockito.mock(FrankfurterApiClient.class);

        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setRates(Map.of(
                "USD", 0.000060,
                "EUR", 0.000051
        ));

        Mockito.when(apiClient.fetchLatestIdrRates())
                .thenReturn(mockResponse);

        LatestIdrRatesStrategy strategy =
                new LatestIdrRatesStrategy(apiClient);

        List<FinanceResponseDto> result = strategy.fetch();

        FinanceResponseDto usdSpread = result.stream()
                .filter(r -> r.getKey().equals("USD_BuySpread_IDR"))
                .findFirst()
                .orElseThrow();

        // hitung expected (PAKAI LOGIC YANG SAMA)
        double usdRate = 0.000060;
        double spreadFactor = calculateExpectedSpread("Happid");
        double expected = (1 / usdRate) * (1 + spreadFactor);

        assertThat((Double) usdSpread.getValue())
                .isCloseTo(expected, org.assertj.core.data.Offset.offset(0.0001));
    }

    // ===== helper =====
    private double calculateExpectedSpread(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }

    private org.assertj.core.data.Offset<Double> withinTolerance() {
        return org.assertj.core.data.Offset.offset(0.0001);
    }

}
