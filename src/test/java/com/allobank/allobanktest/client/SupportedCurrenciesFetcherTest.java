package com.allobank.allobanktest.client;

import com.allobank.allobanktest.strategy.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupportedCurrenciesFetcherTest {

    private SupportedCurrenciesFetcher fetcher;

    private void setupFetcherWithResponse(ExchangeFunction exchangeFunction) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        this.fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @Test
    void shouldFetchSupportedCurrenciesSuccessfully() {
        // GIVEN: Mocking sukses response
        setupFetcherWithResponse(request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "IDR": "Indonesian Rupiah",
                                  "USD": "United States Dollar"
                                }
                                """)
                        .build()
        ));

        // WHEN
        Map<String, String> result = fetcher.fetchAndTransform();

        // THEN
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsEntry("IDR", "Indonesian Rupiah")
                .containsEntry("USD", "United States Dollar");
    }

    @Test
    void shouldThrowExceptionWhenApiFails() {
        // GIVEN: Mocking error behavior
        setupFetcherWithResponse(request ->
                Mono.error(new RuntimeException("Frankfurter API unavailable")));

        // WHEN & THEN
        assertThatThrownBy(() -> fetcher.fetchAndTransform())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Frankfurter API unavailable");
    }

}
