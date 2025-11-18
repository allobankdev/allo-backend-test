package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.CurrenciesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedCurrenciesFetcherTest {

    @Test
    void fetchFromApi_shouldReturnCurrenciesMap() {
        String fakeJson = """
                {
                  "USD": "United States Dollar",
                  "IDR": "Indonesian Rupiah",
                  "EUR": "Euro"
                }
                """;

        ExchangeFunction exchangeFunction = request -> {
            assertThat(request.url().getPath()).isEqualTo("/currencies");
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

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(webClient);

        Object result = fetcher.fetchFromApi();

        assertThat(result).isInstanceOf(CurrenciesResponse.class);
        CurrenciesResponse response = (CurrenciesResponse) result;

        assertThat(response.resourceType()).isEqualTo("supported_currencies");
        Map<String, String> currencies = response.currencies();

        assertThat(currencies)
                .containsEntry("USD", "United States Dollar")
                .containsEntry("IDR", "Indonesian Rupiah")
                .containsEntry("EUR", "Euro");
    }
}
