package com.allobank.service;

import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import com.allobank.helper.MockExchangeFunction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class GetSupportedCurrencyServiceTest {

    @Test
    void fetch_success() {
        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("{\"USD\":\"United States Dollar\",\"EUR\":\"Euro\"}")
                .build();

        WebClient wc = WebClient.builder()
                .exchangeFunction(new MockExchangeFunction(response))
                .build();

        GetSupportedCurrencyService service = new GetSupportedCurrencyService(wc);

        StepVerifier.create(service.fetch())
                .expectNextMatches(map ->
                        map.get("USD").equals("United States Dollar") &&
                                map.get("EUR").equals("Euro"))
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

        GetSupportedCurrencyService service = new GetSupportedCurrencyService(wc);

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

        GetSupportedCurrencyService service = new GetSupportedCurrencyService(wc);

        StepVerifier.create(service.fetch())
                .expectError(ExternalException.class)
                .verify();
    }
}
