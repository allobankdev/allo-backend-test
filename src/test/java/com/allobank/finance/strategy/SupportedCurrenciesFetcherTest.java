package com.allobank.finance.strategy;

import com.allobank.finance.model.SupportedCurrenciesData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private RestClient restClient;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher(restClient);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    void shouldFetchAndTransformSupportedCurrencies() {
        // Arrange
        Map<String, String> mockCurrencies = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah",
                "GBP", "British Pound Sterling"
        );

        mockRestClient(mockCurrencies);

        // Act
        SupportedCurrenciesData result = fetcher.fetchData();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.currencies()).hasSize(4);
        assertThat(result.count()).isEqualTo(4);
        assertThat(result.currencies().get("USD")).isEqualTo("United States Dollar");
        assertThat(result.currencies().get("IDR")).isEqualTo("Indonesian Rupiah");
    }

    private void mockRestClient(Map<String, String> response) {
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);
    }
}
