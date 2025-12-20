package com.allo.backendtest.client;

import com.allo.backendtest.constant.FrankfurterConstants;
import com.allo.backendtest.dto.frankfurter.HistoricalDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class FrankfurterClient {

    private final RestClient restClient;

    public FrankfurterClient(@Qualifier("frankfurterRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, Object> getLatest(){
        return restClient.get()
                .uri(FrankfurterConstants.PATH_LATEST)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public HistoricalDto getHistorical(){
        return restClient.get()
                .uri(FrankfurterConstants.PATH_HISTORICAL)
                .retrieve()
                .body(HistoricalDto.class);
    }

    public Map<String, Object> getCurrencies(){
        return restClient.get()
                .uri(FrankfurterConstants.PATH_CURRENCIES)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
