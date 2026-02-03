package com.example.allobank.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldFetchSupportedCurrencies() {

        Object dummy = new Object();

        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(dummy);

        Object result = fetcher.fetch();

        assertNotNull(result);
    }
}

