package com.example.allo.strategy;

import com.example.allo.client.FrankfurterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldReturnCorrectResourceType() {
        // when
        String resourceType = fetcher.getResourceType();

        // then
        assertThat(resourceType).isEqualTo("supported_currencies");
    }

    @Test
    void shouldFetchSupportedCurrenciesFromClient() {
        // given
        Map<String, String> mockResponse = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        when(client.getCurrencies()).thenReturn(mockResponse);

        // when
        Object result = fetcher.fetch();

        // then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);

        verify(client, times(1)).getCurrencies();
        verifyNoMoreInteractions(client);
    }
}