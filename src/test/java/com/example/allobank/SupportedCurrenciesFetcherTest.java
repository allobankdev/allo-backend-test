package com.example.allobank;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.service.SupportedCurrenciesFetcher;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class SupportedCurrenciesFetcherTest {

    @Test
    void fetch_shouldReturnCurrenciesAsUnifiedItems() {
        String json = """
        {
          "USD": "United States Dollar",
          "IDR": "Indonesian Rupiah"
        }
        """;

        WebClient webClient = WebClient.builder()
                .exchangeFunction(fakeJsonResponse(json))
                .build();

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(webClient);
        List<FinanceDataItemDto> items = fetcher.fetch();

        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals("supported_currencies", items.get(0).getResourceType());
        Assertions.assertTrue(items.stream().anyMatch(i -> "USD".equals(i.getKey())));
        Assertions.assertTrue(items.stream().anyMatch(i -> "IDR".equals(i.getKey())));
    }

    private ExchangeFunction fakeJsonResponse(String json) {
        return request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(json) // <-- FIX
                        .build()
        );
    }
}