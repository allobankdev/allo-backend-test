package com.allobank.finance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;

import reactor.core.publisher.Mono;


@ExtendWith(MockitoExtension.class)
public class CurrenciesServiceTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrenciesService currenciesService;

    @Test
    public void fetchSupportedCurrencies_returnsMap() {
        doReturn(webClient).when(frankfurterClient).getWebClient();
        doReturn(uriSpec).when(webClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/currencies");
        doReturn(responseSpec).when(headersSpec).retrieve();


        com.allobank.finance.dto.CurrenciesResponse apiResp = new com.allobank.finance.dto.CurrenciesResponse();
        apiResp.put("USD", "United States Dollar");

        doReturn(Mono.just(apiResp)).when(responseSpec).bodyToMono(com.allobank.finance.dto.CurrenciesResponse.class);

        com.allobank.finance.dto.CurrenciesResponse resp = currenciesService.fetchSupportedCurrencies();

        assertNotNull(resp);
        assertEquals("United States Dollar", resp.get("USD"));
    }
}
