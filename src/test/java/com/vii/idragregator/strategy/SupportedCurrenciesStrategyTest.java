package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.CurrencyDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private SupportedCurrenciesStrategy currenciesStrategy;

    @Test
    void testFetchSupportedCurrencies_Success() {
        Map<String, String> mockMap = Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        // Penting: Mocking untuk ParameterizedTypeReference
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockMap));

        // Execute
        CurrencyDTO result = (CurrencyDTO) currenciesStrategy.fetch();

        // Verify
        assertNotNull(result);
        assertEquals(2, result.getCurrencies().size());
        assertEquals("Indonesian Rupiah", result.getCurrencies().get("IDR"));
    }
}