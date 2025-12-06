package com.bank.allo.client;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FrankfurterClientRepositoryImplTest {

    @Test
    void testFetchLatestBaseIdr() {
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);

        Map<String,Object> fakeResponse = Map.of("base","IDR");

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));

        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(fakeResponse));

        FrankfurterClientRepository repo = new FrankfurterClientRepositoryImpl(webClient);

        Map<String,Object> result = repo.fetchLatestBaseIdr();

        assertEquals("IDR", result.get("base"));
    }
}
