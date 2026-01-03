package com.allobank.allobackendtest.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.allobackendtest.model.DTO.HistoricalIdrUsdResponse;
import com.allobank.allobackendtest.strategy.HistoricalIdrUsdFetcher;

import reactor.core.publisher.Mono;

@SpringBootTest
public class HistoricalIdrUsdFetcherTest {

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fetcher = new HistoricalIdrUsdFetcher(webClient);
    }

    @Test
    void shouldFetchHistoricalIdrUsdRatesSuccessfully() {

        // given
        HistoricalIdrUsdResponse mockResponse = new HistoricalIdrUsdResponse();
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of("2024-01-02", Map.of("USD", new BigDecimal("6.4e-05"))
        ));

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalIdrUsdResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // when
        Object result = fetcher.fetchData();

        // then
        assertThat(result).isInstanceOf(HistoricalIdrUsdResponse.class);

        HistoricalIdrUsdResponse response = (HistoricalIdrUsdResponse) result;
        assertThat(response.getRates()).containsKey("2024-01-02");
        assertThat(response.getRates().get("2024-01-02")).containsKey("USD");
    }

}
