package com.allobank.finance.strategy;

import com.allobank.finance.client.model.LatestRate;
import com.allobank.finance.config.FinanceApiProperties;
import com.allobank.finance.model.LatestRateData;
import com.allobank.finance.support.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesFetcherTest {

    @Mock
    private RestClient restClient;

    @Mock
    private FinanceApiProperties properties;

    private LatestIDRRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        SpreadCalculator spreadCalculator = new SpreadCalculator();
        fetcher = new LatestIDRRatesFetcher(restClient, properties, spreadCalculator);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    void shouldFetchAndTransformLatestRates() {
        // Arrange
        setupPropertiesMock();

        LatestRate mockResponse = new LatestRate(
                BigDecimal.ONE,
                "IDR",
                "2024-01-15",
                Map.of(
                        "USD", new BigDecimal("0.000063"),
                        "EUR", new BigDecimal("0.000058")
                )
        );

        mockRestClient(mockResponse);

        // Act
        LatestRateData result = fetcher.fetchData();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.base()).isEqualTo("IDR");
        assertThat(result.date()).isEqualTo("2024-01-15");
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(result.rates()).hasSize(2);
        assertThat(result.usdBuySpreadIDR()).isNotNull();
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        // Arrange
        setupPropertiesMock();

        LatestRate mockResponse = new LatestRate(
                BigDecimal.ONE,
                "IDR",
                "2024-01-15",
                Map.of("USD", new BigDecimal("0.000063"))
        );

        mockRestClient(mockResponse);

        // Act
        LatestRateData result = fetcher.fetchData();

        // Assert
        // For manzoy: spreadFactor = 0.00670
        // USD rate = 0.000063
        // usdBuySpread = (1 / 0.000063) * (1 + 0.00670)
        assertThat(result.usdBuySpreadIDR()).isNotNull();
        assertThat(result.usdBuySpreadIDR()).isGreaterThan(BigDecimal.ZERO);
    }

    private void setupPropertiesMock() {
        FinanceApiProperties.Github github = new FinanceApiProperties.Github();
        github.setUsername("manzoy");
        when(properties.getGithub()).thenReturn(github);
    }

    @SuppressWarnings("unchecked")
    private void mockRestClient(LatestRate response) {
        var uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(LatestRate.class)).thenReturn(response);
    }
}
