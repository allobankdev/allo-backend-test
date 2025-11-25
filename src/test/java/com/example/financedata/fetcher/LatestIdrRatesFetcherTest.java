package com.example.financedata.fetcher;

import com.example.financedata.dto.LatestRatesDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LatestIdrRatesFetcherTest {

    @Test
    public void testUsdBuySpreadCalculation() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);

        // mock response: rates { USD: 0.000066 }
        Map<String, Object> raw = Map.of(
                "base", "IDR",
                "date", "2024-06-01",
                "rates", Map.of("USD", 0.000066)
        );

        when(webClient.get().uri(anyString()).retrieve().bodyToMono(Map.class)).thenReturn(Mono.just(raw));

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(webClient, "aswindew");
        Object obj = fetcher.fetch().block();
        assertTrue(obj instanceof LatestRatesDto);
        LatestRatesDto dto = (LatestRatesDto) obj;

        // compute expected using rateUsd = 0.000066 and spreadFactor 0.00866
        double rateUsd = dto.getUsdRate();
        double expected = (1.0 / rateUsd) * (1.0 + 0.00866);
        assertEquals(expected, dto.getUsdBuySpreadIdr(), 1e-6);
    }
}
