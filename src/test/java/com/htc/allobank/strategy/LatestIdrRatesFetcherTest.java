package com.htc.allobank.strategy;

import com.htc.allobank.config.ExternalApiProperties;
import com.htc.allobank.util.SpreadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LatestIdrRatesFetcherTest {

    LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {

        String fakeJson = """
        {
          "base":"IDR",
          "date":"2024-01-01",
          "rates": { "USD": 0.000067 }
        }
        """;

        // 2️⃣ Create an ExchangeFunction that returns the fakeJson
        ExchangeFunction fakeExchange = request ->
          Mono.just(
            ClientResponse.create(HttpStatusCode.valueOf(200))
              .header("Content-Type", "application/json")
              .body(fakeJson)
              .build()
          );

        // 3️⃣ Build WebClient using our fake ExchangeFunction
        WebClient fakeClient = WebClient.builder()
          .exchangeFunction(fakeExchange)
          .build();

        ExternalApiProperties props = new ExternalApiProperties();
        props.getPersonalization().setGithubUsername("exampleuser");
        SpreadUtil spreadUtil = new SpreadUtil(props);

        fetcher = new LatestIdrRatesFetcher(fakeClient, spreadUtil);
    }

    @Test
    void fetch_correctlyAddsSpreadValues() {
        Map result = (Map) fetcher.fetch().block();

        assertNotNull(result);
        assertTrue(result.containsKey("USD_BuySpread_IDR"));
        assertTrue(result.containsKey("SpreadFactor"));

        double spread = (double) result.get("SpreadFactor");

        assertEquals(0.00195, spread, 1e-9);

        double usdBuySpread = (double) result.get("USD_BuySpread_IDR");
        assertTrue(usdBuySpread > 0);
    }
}
