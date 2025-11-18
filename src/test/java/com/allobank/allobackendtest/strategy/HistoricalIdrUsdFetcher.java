package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.HistoricalRatesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalIdrUsdFetcherTest {

    @Test
    void fetchFromApi_shouldReturnHistoricalRates() {
        String fakeJson = """
                {
                  "rates": {
                    "2024-01-01": { "USD": 0.0001 },
                    "2024-01-02": { "USD": 0.00011 }
                  }
                }
                """;

        ExchangeFunction exchangeFunction = request -> {
            assertThat(request.url().getPath()).isEqualTo("/2024-01-01..2024-01-05");
            assertThat(request.url().getQuery()).isEqualTo("from=IDR&to=USD");
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

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(webClient);

        Object result = fetcher.fetchFromApi();

        assertThat(result).isInstanceOf(HistoricalRatesResponse.class);
        HistoricalRatesResponse response = (HistoricalRatesResponse) result;

        assertThat(response.resourceType()).isEqualTo("historical_idr_usd");
        Map<String, Map<String, BigDecimal>> rates = response.rates();

        assertThat(rates).containsKeys("2024-01-01", "2024-01-02");
        assertThat(rates.get("2024-01-01").get("USD"))
                .isEqualByComparingTo(new BigDecimal("0.0001"));
        assertThat(rates.get("2024-01-02").get("USD"))
                .isEqualByComparingTo(new BigDecimal("0.00011"));
    }
}
