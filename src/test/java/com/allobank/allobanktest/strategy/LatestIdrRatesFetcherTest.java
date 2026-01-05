package com.allobank.allobanktest.strategy;

import com.allobank.allobanktest.dto.LatestIdrRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LatestIdrRatesFetcherTest {

    private LatestIdrRatesFetcher fetcher;
    private final String GITHUB_USERNAME = "hasanalmunawar";
    private void setupFetcherWithResponse(ExchangeFunction exchangeFunction) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        this.fetcher = new LatestIdrRatesFetcher(webClient, GITHUB_USERNAME);
    }

    @Test
    void shouldFetchLatestRatesAndCalculateUsdBuySpread() {
        // given
        setupFetcherWithResponse(request ->
                Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body("""
                                    {
                                      "amount": 1,
                                      "base": "IDR",
                                      "date": "2024-01-05",
                                      "rates": {
                                        "USD": 0.000064
                                      }
                                    }
                                """)
                                .build()
                ));

        // when
        LatestIdrRateResponse result = fetcher.fetchAndTransform();

        // then
        assertThat(result).isNotNull();
        assertThat(result.base()).isEqualTo("IDR");
        assertThat(result.rates()).containsKey("USD");

        BigDecimal usdRate = new BigDecimal("0.000064");
        BigDecimal spreadFactor = fetcher.calculateSpreadFactor(GITHUB_USERNAME);

        BigDecimal expectedSpread =
                BigDecimal.ONE
                        .divide(usdRate, 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.ONE.add(spreadFactor));

        assertThat(result.usdBuySpreadIdr())
                .isEqualByComparingTo(expectedSpread);
    }

    @Test
    void shouldThrowExceptionWhenUsdRateIsMissing() {
        // given
        setupFetcherWithResponse(request ->
                Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body("""
                                    {
                                      "amount": 1,
                                      "base": "IDR",
                                      "date": "2024-01-05",
                                      "rates": {
                                        "EUR": 0.000051
                                      }
                                    }
                                """)
                                .build()
                ));

        // then
        assertThatThrownBy(fetcher::fetchAndTransform)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("USD rate not found");
    }

}
