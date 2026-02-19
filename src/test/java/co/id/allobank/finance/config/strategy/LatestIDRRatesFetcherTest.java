package co.id.allobank.finance.config.strategy;

import co.id.allobank.finance.model.response.LatestRatesRawResponse;
import co.id.allobank.finance.model.response.LatestRatesRawResponseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesFetcherTest {

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @InjectMocks
    LatestIDRRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(fetcher, "username", "dhimaspanji");
    }

    @Test
    void shouldFetchAndMapLatestRates() {
        var raw = LatestRatesRawResponseBuilder.builder()
                .amount(1)
                .base("IDR")
                .date("2026-01-01")
                .rates(Map.of("USD", 0.000064))
                .build();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);
        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(
                LatestRatesRawResponse.class))
                .thenReturn(Mono.just(raw));

        var result = fetcher.fetchData();

        assertThat(result).isNotNull();
    }
}
