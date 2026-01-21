package com.allo.app.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.app.dto.FrankfurterProperties;
import com.allo.app.dto.response.LastestIdrRatesResponse;
import com.allo.app.service.IDRDataFetcher;
import com.allo.app.util.Common;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LastestIdrRatesServiceImpl implements IDRDataFetcher<LastestIdrRatesResponse> {

    private final WebClient webClient;
    private final FrankfurterProperties frankfurterProperties;

    @Override
    public Mono<LastestIdrRatesResponse> getData() {
        return webClient.get()
                .uri(frankfurterProperties.getUrl() + "/latest?from=IDR")
                .retrieve()
                .bodyToMono(LastestIdrRatesResponse.class)
                .map(response -> {
                    response.setUSDBuySpreadIDR(calculateUsdBuySpreadIdr(response.getRates().get("USD")));
                    return response;
                });
    }

    private BigDecimal calculateUsdBuySpreadIdr(Object rates) {
        return new BigDecimal((1d / ((Double) rates)) * (Common.calculateSpreadFactor("PatriaSP").doubleValue() + 1));
    }

}
