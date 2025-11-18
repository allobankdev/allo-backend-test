package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CurrenciesResponse;
import com.allobank.allobackendtest.dto.HistoricalRatesResponse;
import com.allobank.allobackendtest.dto.LatestRatesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "frankfurter.base-url=http://dummy",     // base URL bebas, tidak dipakai
        "frankfurter.github-username=ivan-test"  // ganti ke GitHub kamu kalau mau
        // kalau masih ada masalah DataSource, bisa tambahin:
        // "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class DataPreloadRunnerIntegrationTest {

    @TestConfiguration
    static class StubWebClientConfig {

        @Bean
        @Primary
        public WebClient stubWebClient() {
            ExchangeFunction exchangeFunction = request -> {
                String path = request.url().getPath();
                String query = request.url().getQuery();

                String body;

                if (path.equals("/latest") && "base=IDR".equals(query)) {
                    // response untuk /latest?base=IDR
                    body = """
                            {
                              "base": "IDR",
                              "date": "2024-01-05",
                              "rates": {
                                "USD": 0.0001,
                                "EUR": 0.00006
                              }
                            }
                            """;
                } else if (path.equals("/2024-01-01..2024-01-05")
                        && "from=IDR&to=USD".equals(query)) {
                    // response untuk /2024-01-01..2024-01-05?from=IDR&to=USD
                    body = """
                            {
                              "rates": {
                                "2024-01-01": { "USD": 0.0001 },
                                "2024-01-02": { "USD": 0.00011 }
                              }
                            }
                            """;
                } else if (path.equals("/currencies")) {
                    // response untuk /currencies
                    body = """
                            {
                              "USD": "United States Dollar",
                              "IDR": "Indonesian Rupiah"
                            }
                            """;
                } else {
                    // kalau ada path lain, anggap error biar ketahuan
                    return Mono.just(
                            ClientResponse.create(HttpStatus.NOT_FOUND)
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                                    .body("Unexpected path: " + path + "?" + query)
                                    .build()
                    );
                }

                return Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .body(body)
                                .build()
                );
            };

            return WebClient.builder()
                    .exchangeFunction(exchangeFunction)
                    .build();
        }
    }

    @Autowired
    private InMemoryFinanceStore store;

    @Test
    void contextLoads_andStoreHasPreloadedDataFromAllStrategies() {
        // ApplicationRunner sudah jalan saat context naik
        Object latest = store.getByResourceType("latest_idr_rates");
        Object historical = store.getByResourceType("historical_idr_usd");
        Object currencies = store.getByResourceType("supported_currencies");

        assertThat(latest).isInstanceOf(LatestRatesResponse.class);
        assertThat(historical).isInstanceOf(HistoricalRatesResponse.class);
        assertThat(currencies).isInstanceOf(CurrenciesResponse.class);

        LatestRatesResponse latestResp = (LatestRatesResponse) latest;
        assertThat(latestResp.base()).isEqualTo("IDR");
        assertThat(latestResp.rates()).containsEntry("USD", new BigDecimal("0.0001"));

        HistoricalRatesResponse histResp = (HistoricalRatesResponse) historical;
        assertThat(histResp.rates()).containsKey("2024-01-01");

        CurrenciesResponse currResp = (CurrenciesResponse) currencies;
        assertThat(currResp.currencies())
                .containsEntry("IDR", "Indonesian Rupiah")
                .containsEntry("USD", "United States Dollar");
    }
}
