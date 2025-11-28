package com.project.alloBank.service;

import com.project.alloBank.dto.LatestRatesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LatestIdrRatesFetcherTest {
    @Test
    public void testSpreadCalculation() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        LatestRatesResponse fake = new LatestRatesResponse();
        fake.setBase("IDR");
        fake.getRates().put("USD", 0.000064);

        when(webClient.get().uri(anyString()).retrieve().bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(fake));

        LatestIdrRatesFetcher f = new LatestIdrRatesFetcher(webClient);

    }
}
