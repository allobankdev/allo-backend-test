package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.client.FrankfurterClient;
import com.thasya.frankfurter.config.GithubProperties;
import com.thasya.frankfurter.dto.FrankfurterLatestResponse;
import com.thasya.frankfurter.dto.LatestIdrRateDto;
import static org.assertj.core.api.Assertions.within;


import com.thasya.frankfurter.strategy.LatestIdrRatesFetcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LatestIdrRatesFetcherTest {

    @Test
    void shouldCalculateSpreadFactorAndUsdBuySpreadIdr() {
        // Arrange
        FrankfurterClient client = mock(FrankfurterClient.class);
        GithubProperties githubProps = new GithubProperties();
        githubProps.setUsername("thasya07"); // ganti sesuai GitHub username kamu kalau mau

        FrankfurterLatestResponse resp = new FrankfurterLatestResponse();
        resp.setBase("IDR");
        resp.setDate("2024-01-01");
        resp.setRates(Map.of("USD", 0.00006)); // contoh rate

        when(client.getLatestIdrRates()).thenReturn(resp);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, githubProps);

        // Act
        List<?> result = fetcher.fetchData();

        // Assert
        assertThat(result).hasSize(1);
        Object first = result.get(0);
        assertThat(first).isInstanceOf(LatestIdrRateDto.class);

        LatestIdrRateDto dto = (LatestIdrRateDto) first;
        assertThat(dto.getBase()).isEqualTo("IDR");
        assertThat(dto.getDate()).isEqualTo("2024-01-01");
        assertThat(dto.getUsdRate()).isEqualTo(0.00006);

        // Hitung expected spread factor & nilai beli IDR
        String username = githubProps.getUsername(); 
        int sum = username.chars().sum();
        double spreadFactor = (sum % 1000) / 100000.0;

        double expectedUsdBuySpreadIdr = (1 / dto.getUsdRate()) * (1 + spreadFactor);

        assertThat(dto.getUsdBuySpreadIdr())
                .isCloseTo(expectedUsdBuySpreadIdr, within(0.00001));
    }


}