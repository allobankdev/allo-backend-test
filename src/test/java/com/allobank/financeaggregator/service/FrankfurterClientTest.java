package com.allobank.financeaggregator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allobank.financeaggregator.exception.ExternalServiceException;
import com.allobank.financeaggregator.model.LatestRatesResponse;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class FrankfurterClientTest {

    @Test
    void getReturnsParsedBody() {
        String body = "{\"amount\":1.0,\"base\":\"IDR\",\"date\":\"2024-01-05\","
                + "\"rates\":{\"USD\":0.000065}}";

        WebClient webClient = buildClient((request) -> okResponse(body));
        FrankfurterClient client = new FrankfurterClient(webClient);

        LatestRatesResponse response = client.get("/latest?base=IDR", LatestRatesResponse.class);

        assertThat(response.base()).isEqualTo("IDR");
        assertThat(response.rates()).containsEntry("USD", new BigDecimal("0.000065"));
    }

    @Test
    void getWithTypeReferenceReturnsParsedBody() {
        String body = "{\"USD\":\"United States Dollar\",\"IDR\":\"Indonesian Rupiah\"}";

        WebClient webClient = buildClient((request) -> okResponse(body));
        FrankfurterClient client = new FrankfurterClient(webClient);

        Map<String, String> response = client.get("/currencies", new ParameterizedTypeReference<>() {});

        assertThat(response).containsEntry("USD", "United States Dollar");
    }

    @Test
    void getThrowsExternalServiceExceptionOnError() {
        WebClient webClient = buildClient((request) -> errorResponse("upstream error"));
        FrankfurterClient client = new FrankfurterClient(webClient);

        assertThatThrownBy(() -> client.get("/latest?base=IDR", LatestRatesResponse.class))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("502")
                .hasMessageContaining("upstream error");
    }

    private WebClient buildClient(ExchangeFunction exchangeFunction) {
        return WebClient.builder()
                .baseUrl("http://localhost")
                .exchangeFunction(exchangeFunction)
                .build();
    }

    private Mono<ClientResponse> okResponse(String body) {
        return Mono.just(ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(body)
                .build());
    }

    private Mono<ClientResponse> errorResponse(String body) {
        return Mono.just(ClientResponse.create(HttpStatus.BAD_GATEWAY)
                .header("Content-Type", "text/plain")
                .body(body)
                .build());
    }
}
