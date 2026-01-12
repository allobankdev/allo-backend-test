package id.co.evan.project.aggregator.service.strategy.impl;

import id.co.evan.project.aggregator.config.properties.FinanceProperties;
import id.co.evan.project.aggregator.util.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HistoricalIdrStrategyTest {

    @Test
    void historical_fetch_success() {
        var strategy = new HistoricalIdrStrategy(
            stubWebClient("""
          {"amount":1.0,"base":"IDR","start_date":"2023-12-29","end_date":"2024-01-05","rates":{"2024-01-05":{"USD":0.000064}}}
        """),
            defaultFinanceProps()
        );

        StepVerifier.create(strategy.fetchData())
            .assertNext(res -> {
                assertEquals(Constants.HISTORICAL_IDR_USD, res.resourceType());
                assertFalse(res.data().isEmpty());
            })
            .verifyComplete();
    }

    private FinanceProperties defaultFinanceProps() {
        return new FinanceProperties(
            new FinanceProperties.Retry(3, 2000),
            new FinanceProperties.Resources(
                new FinanceProperties.Resources.Latest("IDR", "USD"),
                new FinanceProperties.Resources.Historical("2024-01-01..2024-01-05", "IDR", "USD")
            )
        );
    }

    private WebClient stubWebClient(String json) {
        ExchangeFunction ef = Mockito.mock(ExchangeFunction.class);
        Mockito.when(ef.exchange(Mockito.any()))
            .thenReturn(Mono.just(
                ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                    .build()
            ));

        return WebClient.builder()
            .exchangeFunction(ef)
            .build();
    }
}
