package id.co.evan.project.aggregator.service.strategy.impl;

import id.co.evan.project.aggregator.config.properties.FinanceProperties;
import id.co.evan.project.aggregator.config.properties.GithubProperties;
import id.co.evan.project.aggregator.fault.ResourceNotFoundException;
import id.co.evan.project.aggregator.util.Constants;
import id.co.evan.project.aggregator.util.ErrorCode;
import id.co.evan.project.aggregator.util.SpreadFactorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LatestIdrStrategyTest {

    @Test
    @DisplayName("Missing rate for currency")
    void fetchData_Failed() {
        var strategy = new LatestIdrStrategy(
            stubWebClient("""
            {"amount":1.0,"base":"IDR","date":"2024-01-05","rates":{}}
        """),
            new SpreadFactorUtil(),
            defaultFinanceProps(),
            new GithubProperties("evanahmad")
        );

        StepVerifier.create(strategy.fetchData())
            .expectErrorSatisfies(ex -> {
                Assertions.assertInstanceOf(ResourceNotFoundException.class, ex);
                assertTrue(ex.getMessage().contains(ErrorCode.RESOURCE_NOT_FOUND.getDefaultMessage()));
            })
            .verify();
    }

    @Test
    void fetchData_success() {
        var strategy = new LatestIdrStrategy(
            stubWebClient("""
                {"amount":1.0,"base":"IDR","date":"2024-01-05","rates":{"USD":0.000064}}
            """),
            new SpreadFactorUtil(),
            defaultFinanceProps(),
            new GithubProperties("evanahmad")
        );

        StepVerifier.create(strategy.fetchData())
            .assertNext(res -> {
                assertEquals(Constants.LATEST_IDR_RATES, res.resourceType());
                assertFalse(res.data().isEmpty());

                Map<String, Object> d = res.data().get(0);

                assertEquals("USD", d.get("currency"));
                assertNotNull(d.get("rate"));
                assertNotNull(d.get("USD_BuySpread_IDR"));
            })
            .verifyComplete();
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

    private FinanceProperties defaultFinanceProps() {
        return new FinanceProperties(
            new FinanceProperties.Retry(3, 2000),
            new FinanceProperties.Resources(
                new FinanceProperties.Resources.Latest("IDR", "USD"),
                new FinanceProperties.Resources.Historical("2024-01-01..2024-01-05", "IDR", "USD")
            )
        );
    }
}