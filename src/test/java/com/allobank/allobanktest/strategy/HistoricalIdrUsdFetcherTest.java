package com.allobank.allobanktest.strategy;

import com.allobank.allobanktest.dto.HistoricalRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HistoricalIdrUsdFetcherTest {

    private HistoricalIdrUsdFetcher fetcher;

    private void setupFetcherWithResponse(ExchangeFunction exchangeFunction) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        this.fetcher = new HistoricalIdrUsdFetcher(webClient);
    }

    @Test
    void shouldFetchHistoricalRatesSuccessfully() {
        // given
        setupFetcherWithResponse(request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                    {
                                      "amount": 1,
                                      "base": "IDR",
                                      "start_date": "2023-12-29",
                                      "end_date": "2024-01-05",
                                      "rates": {
                                        "2023-12-29": { "USD": 0.000065 },
                                        "2024-01-05": { "USD": 0.000064 }
                                      }
                                    }
                                """)
                        .build()
        ));

        // when
        HistoricalRateResponse result =
                (HistoricalRateResponse) fetcher.fetchAndTransform();

        // then
        assertThat(result).isNotNull();
        assertThat(result.base()).isEqualTo("IDR");
        assertThat(result.rates()).containsKey("2023-12-29");
        assertThat(result.rates().get("2023-12-29"))
                .containsEntry("USD", 0.000065);
    }

    @Test
    void shouldThrowExceptionWhenApiReturnsError() {
        // given
        setupFetcherWithResponse(request ->
                Mono.just(
                        ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
                                .header("Content-Type", "application/json")
                                .body("""
                                    {
                                      "error": "Internal Server Error"
                                    }
                                """)
                                .build()
                ));

        // then
        assertThatThrownBy(fetcher::fetchAndTransform)
                .isInstanceOf(WebClientResponseException.class);
    }

}
