package co.id.allobank.finance.config.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @InjectMocks
    SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldFetchAndMapSupportedCurrencies() {

        Map<String, String> rawResponse =
                Map.of(
                        "USD", "United States Dollar",
                        "IDR", "Indonesian Rupiah"
                );

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);
        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<Map<String,String>>>any()))
                .thenReturn(Mono.just(rawResponse));

        var result = fetcher.fetchData();

        assertThat(result).isNotNull();
        assertThat((java.util.List<?>) result)
                .hasSize(2);
    }
}
