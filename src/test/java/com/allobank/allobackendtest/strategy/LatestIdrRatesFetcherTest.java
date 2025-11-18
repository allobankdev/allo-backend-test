package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.LatestRatesResponse;
import com.allobank.allobackendtest.util.SpreadFactorCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LatestIdrRatesFetcherTest {

    @Test
    void fetchFromApi_shouldCalculateSpreadAndUsdBuySpreadIdrCorrectly() {
        // arrange
        String fakeJson = """
                {
                  "base": "IDR",
                  "date": "2024-01-05",
                  "rates": {
                    "USD": 0.0001,
                    "EUR": 0.00006
                  }
                }
                """;

        ExchangeFunction exchangeFunction = request -> {
            // optional: pastikan path benar
            assertThat(request.url().getPath()).isEqualTo("/latest");
            assertThat(request.url().getQuery()).isEqualTo("base=IDR");
            return Mono.just(
                    ClientResponse.create(HttpStatus.OK)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(fakeJson)
                            .build()
            );
        };

        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        String githubUsername = "ivan-test";
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(webClient, githubUsername);

        // act
        Object result = fetcher.fetchFromApi();

        // assert
        assertThat(result).isInstanceOf(LatestRatesResponse.class);
        LatestRatesResponse response = (LatestRatesResponse) result;

        assertThat(response.resourceType()).isEqualTo("latest_idr_rates");
        assertThat(response.base()).isEqualTo("IDR");
        assertThat(response.date()).isEqualTo(LocalDate.parse("2024-01-05"));
        assertThat(response.rates()).containsEntry("USD", new BigDecimal("0.0001"));

        // cek spread factor
        double expectedSpread = SpreadFactorCalculator.calculateSpreadFactor(githubUsername);
        assertThat(response.spreadFactor())
                .isEqualByComparingTo(BigDecimal.valueOf(expectedSpread));

        // cek USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)
        BigDecimal rateUsd = new BigDecimal("0.0001");
        BigDecimal idrPerUsd = BigDecimal.ONE
                .divide(rateUsd, 8, BigDecimal.ROUND_HALF_UP);

        BigDecimal expectedUsdBuySpreadIdr = idrPerUsd
                .multiply(BigDecimal.valueOf(1.0 + expectedSpread));

        assertThat(response.usdBuySpreadIdr())
                .isEqualByComparingTo(expectedUsdBuySpreadIdr);
    }
}
