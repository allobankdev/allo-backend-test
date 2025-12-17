package com.example.allobank;

import com.example.allobank.config.GithubProperties;
import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.service.LatestRatesFetcher;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class LatestRatesFetcherTest {

    @Test
    void fetch_shouldReturnComputedUsdBuySpreadIdr() {
        String json = """
        {
          "amount": 1.0,
          "base": "IDR",
          "date": "2025-01-01",
          "rates": {
            "USD": 0.000064,
            "EUR": 0.000059
          }
        }
        """;

        WebClient webClient = WebClient.builder()
                .exchangeFunction(fakeJsonResponse(json))
                .build();

        GithubProperties gh = new GithubProperties();
        gh.setUsername("RiskyAdit06");

        LatestRatesFetcher fetcher = new LatestRatesFetcher(webClient, gh);

        List<FinanceDataItemDto> items = fetcher.fetch();

        FinanceDataItemDto computed = items.stream()
                .filter(i -> "USD_BuySpread_IDR".equals(i.getKey()))
                .findFirst()
                .orElseThrow();

        BigDecimal usdRate = new BigDecimal("0.000064");
        BigDecimal spreadFactor = calcFactor("RiskyAdit06");

        BigDecimal expected = BigDecimal.ONE
                .divide(usdRate, 12, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));

        Assertions.assertEquals(0, expected.compareTo((BigDecimal) computed.getValue()));
        Assertions.assertEquals("latest_idr_rates", computed.getResourceType());
    }

    private ExchangeFunction fakeJsonResponse(String json) {
        return request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(json)   // <-- FIX: String, bukan byte[]
                        .build()
        );
    }

    private BigDecimal calcFactor(String username) {
        int sum = 0;
        for (char c : username.toCharArray()) {
            if (c >= 'a' && c <= 'z') sum += (int) c;
        }
        int mod = sum % 1000;
        return BigDecimal.valueOf(mod).divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }
}