package com.allobank.service;

import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import com.allobank.helper.MockExchangeFunction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class GetHistoricalServiceTest {

    @Test
    void fetch_success() {
        String json = """
                {"amount":1,"base":"IDR","date":"2024-01-05","rates":{"USD":0.000065}}
                """;

        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(json)
                .build();

        WebClient wc = WebClient.builder()
                .exchangeFunction(new MockExchangeFunction(response))
                .build();

        GetHistoricalService service = new GetHistoricalService(wc);

        StepVerifier.create(service.fetch())
                .expectNextMatches(resp ->
                        resp.getBase().equals("IDR") &&
                                resp.getRates().get("USD").doubleValue() == 0.000065
                )
                .verifyComplete();
    }

    @Test
    void fetch_4xx_error() {
        ClientResponse response = ClientResponse.create(HttpStatus.BAD_REQUEST)
                .body("BAD REQUEST")
                .build();

        WebClient wc = WebClient.builder()
                .exchangeFunction(new MockExchangeFunction(response))
                .build();

        GetHistoricalService service = new GetHistoricalService(wc);

        StepVerifier.create(service.fetch())
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void fetch_5xx_error() {
        ClientResponse response = ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("SERVER ERROR")
                .build();

        WebClient wc = WebClient.builder()
                .exchangeFunction(new MockExchangeFunction(response))
                .build();

        GetHistoricalService service = new GetHistoricalService(wc);

        StepVerifier.create(service.fetch())
                .expectError(ExternalException.class)
                .verify();
    }
}