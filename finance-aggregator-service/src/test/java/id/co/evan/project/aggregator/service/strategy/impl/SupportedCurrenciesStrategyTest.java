package id.co.evan.project.aggregator.service.strategy.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
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
    private SupportedCurrenciesStrategy strategy;

    @Test
    void shouldTransformCurrenciesMapToList() {
        Map<String, String> mockExternalResponse = Map.of(
            "USD", "United States Dollar",
            "IDR", "Indonesian Rupiah"
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
            .thenReturn(Mono.just(mockExternalResponse));

        StepVerifier.create(strategy.fetchData())
            .assertNext(response -> {
                assert response.resourceType().equals("supported_currencies");
                List<Map<String, Object>> data = response.data();
                assert data.size() == 2;

                var hasIdr = data.stream().anyMatch(m -> m.get("code").equals("IDR"));
                assert hasIdr;
            })
            .verifyComplete();
    }
}