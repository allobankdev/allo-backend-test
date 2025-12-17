package com.example.allobank.service;

import com.example.allobank.dto.FinanceDataItemDto;
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

public class HistoricalRatesFetcherTest {

    @Test
    void fetch_shouldReturnListOfDates() {
        String json = """
        {
          "amount": 1.0,
          "base": "IDR",
          "start_date": "2024-01-01",
          "end_date": "2024-01-05",
          "rates": {
            "2024-01-01": { "USD": 0.000064 },
            "2024-01-02": { "USD": 0.000065 }
          }
        }
        """;

        WebClient webClient = WebClient.builder()
                .exchangeFunction(fakeJsonResponse(json))
                .build();

        HistoricalRatesFetcher fetcher = new HistoricalRatesFetcher(webClient);
        List<FinanceDataItemDto> items = fetcher.fetch();

        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals("historical_idr_usd", items.get(0).getResourceType());
        Assertions.assertTrue(items.stream().anyMatch(i -> "2024-01-01".equals(i.getKey())));
        Assertions.assertTrue(items.stream().anyMatch(i -> "2024-01-02".equals(i.getKey())));
    }

    private ExchangeFunction fakeJsonResponse(String json) {
        return request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(json)
                        .build()
        );
    }
}

