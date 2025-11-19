package com.allobank.service;

import com.allobank.config.properties.ClientProperties;
import com.allobank.exceptions.ExternalException;
import com.allobank.helper.MockExchangeFunction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetLatestIDRServiceTest {

    @Test
    void fetch_success_spread_calc() {
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

        ClientProperties props = mock(ClientProperties.class);
        ClientProperties.Personalization personalization = mock(ClientProperties.Personalization.class);

        when(props.personalization()).thenReturn(personalization);
        when(personalization.githubUsername()).thenReturn("johndoe");

        GetLatestIDRService service = new GetLatestIDRService(props, wc);

        StepVerifier.create(service.fetch())
                .assertNext(resp -> {
                    BigDecimal usdRate = resp.getRates().get("USD");
                    assert usdRate != null;

                    int sum = "johndoe".chars().sum();
                    double spreadFactor = (sum % 1000) / 100000.0;

                    BigDecimal expected = BigDecimal.ONE
                            .divide(usdRate, 12, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(1 + spreadFactor))
                            .setScale(6, BigDecimal.ROUND_HALF_UP);

                    assert resp.getUsdBuySpreadIdr().compareTo(expected) == 0;
                })
                .verifyComplete();
    }

    @Test
    void fetch_missing_usd_rate() {
        String json = """
                {"amount":1,"base":"IDR","date":"2024-01-05","rates":{}}
                """;

        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(json)
                .build();

        WebClient wc = WebClient.builder()
                .exchangeFunction(new MockExchangeFunction(response))
                .build();

        ClientProperties props = mock(ClientProperties.class);
        ClientProperties.Personalization personalization = mock(ClientProperties.Personalization.class);
        when(props.personalization()).thenReturn(personalization);

        GetLatestIDRService service = new GetLatestIDRService(props, wc);

        StepVerifier.create(service.fetch())
                .expectError(ExternalException.class)
                .verify();
    }
}

