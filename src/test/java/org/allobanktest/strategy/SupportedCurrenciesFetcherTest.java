package org.allobanktest.strategy;

import org.allobanktest.dto.SupportedCurrencyItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SupportedCurrenciesFetcherTest {
    @Test
    void testCurrenciesTransformation() {
        String json = """
                {
                  "USD": "United States Dollar",
                  "IDR": "Indonesian Rupiah"
                }
                """;

        WebClient client = WebClient.builder()
                .exchangeFunction(request -> {
                    ClientResponse response = ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .body(json)
                            .build();
                    return Mono.just(response);
                })
                .build();

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher();

        List<?> result = fetcher.load(client, "bluntswordman");

        assertEquals(2, result.size());
        SupportedCurrencyItem usd = (SupportedCurrencyItem) result.get(0);
        assertNotNull(usd.code());
        assertNotNull(usd.name());
    }
}
