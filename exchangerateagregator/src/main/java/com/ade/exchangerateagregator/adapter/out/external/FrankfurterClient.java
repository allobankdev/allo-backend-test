package com.ade.exchangerateagregator.adapter.out.external;

import com.ade.exchangerateagregator.adapter.config.FrankFurterProperties;
import com.ade.exchangerateagregator.application.dto.out.HistorycalResponse;
import com.ade.exchangerateagregator.application.dto.out.LatesIdrRateExternalResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class FrankfurterClient {
    private final WebClient webClient;
    private final FrankFurterProperties frankFurterProperties;

    public FrankfurterClient(WebClient.Builder builder, FrankFurterProperties properties, FrankFurterProperties frankFurterProperties) {
        this.frankFurterProperties = frankFurterProperties;
        this.webClient = builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public LatesIdrRateExternalResponse getLatesIdrRate(String currency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(frankFurterProperties.getEndpoint().getLatestIdrRate())
                        .queryParam("base",currency)
                        .build()
                )
                .retrieve()
                .bodyToMono(LatesIdrRateExternalResponse.class)
                .block();
    }

    public HistorycalResponse getHistory(String fromCurrency, String toCurrency, String startDate, String endDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(frankFurterProperties.getEndpoint().getHistory())
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .build(Map.of("dateRange",startDate.concat("..").concat(endDate)))
                )
                .retrieve()
                .bodyToMono(HistorycalResponse.class)
                .block();
    }

    public Map<String, String> getCurrencies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(frankFurterProperties.getEndpoint().getCurrency())
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

}
