package com.allobank.backendtest.fetcher;

import com.allobank.backendtest.dto.LatestRateDto;
import com.allobank.backendtest.util.SpreadCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LatestIdrRatesFetcherTest {
    @Test
    public void calculatesUsdBuySpreadCorrectly() throws Exception {
        // Mock WebClient
        WebClient webClient = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Map<String, Object> mockResp = new HashMap<>();
        mockResp.put("rates", Map.of("USD", 0.000057));

        // match the exact WebClient signature:
        when(webClient.get()
                .uri(any(java.util.function.Function.class))
                .retrieve()
                .bodyToMono(Map.class)
        ).thenReturn(Mono.just(mockResp));

        LatestIdrRatesFetcher f = new LatestIdrRatesFetcher(webClient, "nurhamim96");
        List<LatestRateDto> list = f.fetchSync();

        assertNotNull(list);
        assertEquals(1, list.size());
        LatestRateDto dto = list.get(0);

        assertEquals("USD", dto.currency());

        // convert spread (double) → BigDecimal
        BigDecimal spread = SpreadCalculator.computeSpreadFactor("nurhamim96");

        // expected IDR per USD = 1 / rateUsd
        BigDecimal rateUsd = dto.rateUsdWhenBaseIdr();

        BigDecimal idrPerUsd = BigDecimal.ONE.divide(
                rateUsd, new MathContext(20, RoundingMode.HALF_UP)
        );

        // apply spread
        BigDecimal expected = idrPerUsd
                .multiply(BigDecimal.ONE.add(spread))
                .setScale(6, RoundingMode.HALF_UP);

        // assert BigDecimal to BigDecimal
        assertEquals(0, expected.compareTo(dto.usdBuySpreadIdr()));
    }
}
