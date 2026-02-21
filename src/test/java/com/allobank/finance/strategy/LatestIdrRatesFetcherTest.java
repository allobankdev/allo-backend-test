package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.config.FinanceProperties;
import com.allobank.finance.dto.LatestRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private FinanceProperties financeProperties;

    @InjectMocks
    private LatestIdrRatesFetcher latestIdrRatesFetcher;

    @Test
    void calculateSpreadCorrectly() {

        // GIVEN
        String githubUsername = "abc";
        when(financeProperties.getGithubUsername()).thenReturn(githubUsername);

        LatestRateResponse latestRateResponse = new LatestRateResponse();
        latestRateResponse.setBaseCurrency("IDR");
        latestRateResponse.setDate("2025-03-03");
        latestRateResponse.setRates(Map.of("USD", BigDecimal.valueOf(0.000064)));

        when(frankfurterClient.getLatestIdrRates()).thenReturn(latestRateResponse);

        // WHEN
        LatestRateResponse result = (LatestRateResponse) latestIdrRatesFetcher.fetch();

        // ASCII sum: 97+98+99=294
        double expectedSpread = 294 / 100000.0;
        double usdRate = 0.000064;
        double expectedUsd = (1 / usdRate) * (1 + expectedSpread);

        // THEN
        assertEquals(expectedUsd, result.getUsdSpreadIdr());

    }
}
