package com.self.bs.source.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.dto.response.HistoryCurrencyRateResponseDto;
import com.self.bs.source.dto.response.LatestCurrencyRateResponseDto;

@Component
public class ExchangeRateWebClient {
    @Autowired
    protected WebClient webClient;

    public Map<String, String> getCurrencyList(){
        return webClient.get()
            .uri("/currencies")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
            .block();
    }

    public LatestCurrencyRateResponseDto getLatestCurrencyRate(ExchangeRateDataFetcherRequestDto requestDto){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest")
                    .queryParam("base", requestDto.getBaseCurrency())
                    .build()
                )
                .retrieve()
                .bodyToMono(LatestCurrencyRateResponseDto.class)
                .block();
    }

    public HistoryCurrencyRateResponseDto getHistoryCurrencyRate(ExchangeRateDataFetcherRequestDto requestDto, String rangeDate){
        return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/{rangeDate}")
                        .queryParam("from", requestDto.getBaseCurrency())
                        .queryParam("to", requestDto.getTargetCurrency())
                        .build(rangeDate)
                    )
                    .retrieve()
                    .bodyToMono(HistoryCurrencyRateResponseDto.class)
                    .block();
    }
}
