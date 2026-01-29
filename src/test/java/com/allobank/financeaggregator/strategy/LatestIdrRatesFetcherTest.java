package com.allobank.financeaggregator.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.financeaggregator.config.FrankfurterProperties;
import com.allobank.financeaggregator.dto.LatestIdrRatesDto;
import com.allobank.financeaggregator.model.LatestRatesResponse;
import com.allobank.financeaggregator.service.FrankfurterClient;
import com.allobank.financeaggregator.service.SpreadFactorCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class LatestIdrRatesFetcherTest {

    @Test
    void fetchDataAddsUsdBuySpread() {
        LatestRatesResponse response = new LatestRatesResponse(
                new BigDecimal("1.0"),
                "IDR",
                "2024-01-05",
                Map.of("USD", new BigDecimal("0.000065"), "EUR", new BigDecimal("0.00006"))
        );
        StubFrankfurterClient client = new StubFrankfurterClient(response);

        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setGithubUsername("testuser");
        SpreadFactorCalculator calculator = new SpreadFactorCalculator(properties);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, calculator);
        LatestIdrRatesDto payload = fetcher.fetchData();
        BigDecimal spread = calculator.getSpreadFactor();
        BigDecimal usdRate = new BigDecimal("0.000065");
        BigDecimal expected = BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spread))
                .setScale(10, RoundingMode.HALF_UP);

        assertThat(payload.usdBuySpreadIdr().compareTo(expected)).isZero();
        assertThat(client.lastPath()).isEqualTo("/latest?base=IDR");
    }

    private static class StubFrankfurterClient extends FrankfurterClient {

        private final LatestRatesResponse response;
        private String lastPath;

        private StubFrankfurterClient(LatestRatesResponse response) {
            super(WebClient.builder().build());
            this.response = response;
        }

        @Override
        public <T> T get(String path, Class<T> responseType) {
            lastPath = path;
            if (path.equals("/latest?base=IDR")) {
                return responseType.cast(response);
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }

        String lastPath() {
            return lastPath;
        }
    }
}
