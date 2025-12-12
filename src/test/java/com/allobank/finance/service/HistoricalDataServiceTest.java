package com.allobank.finance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalDataResponse;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class HistoricalDataServiceTest {

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
    private HistoricalDataService historicalDataService;

    @Test
    public void fetchHistoricalData_returnsData() {
        doReturn(webClient).when(frankfurterClient).getWebClient();
            doReturn(uriSpec).when(webClient).get();
            doReturn(headersSpec).when(uriSpec).uri(org.mockito.ArgumentMatchers.anyString());
        doReturn(responseSpec).when(headersSpec).retrieve();

        HistoricalDataResponse apiResp = new HistoricalDataResponse();
        apiResp.setBase("IDR");
        apiResp.setRates(Map.of("2024-01-01", Map.of("USD", 0.000062)));

        doReturn(Mono.just(apiResp)).when(responseSpec).bodyToMono(HistoricalDataResponse.class);

        HistoricalDataResponse resp = historicalDataService.fetchHistoricalData();

        assertNotNull(resp);
        assertEquals("IDR", resp.getBase());
    }
}
