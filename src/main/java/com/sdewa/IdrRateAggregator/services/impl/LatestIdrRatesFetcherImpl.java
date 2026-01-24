package com.sdewa.IdrRateAggregator.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sdewa.IdrRateAggregator.dtoes.LatestIdrRatesResponse;
import com.sdewa.IdrRateAggregator.dtoes.LatestIdrRatesWithSpreadRecord;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;
import com.sdewa.IdrRateAggregator.uitls.SpreadFactorUtils;

import reactor.core.publisher.Mono;

@Service
public class LatestIdrRatesFetcherImpl implements IDRDataFetcher<List<LatestIdrRatesWithSpreadRecord>> {
    private final WebClient webClient;

    public LatestIdrRatesFetcherImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<LatestIdrRatesWithSpreadRecord> fetchData() {

        LatestIdrRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus((c) -> c != HttpStatus.OK, r -> Mono.error(new RuntimeException("API error")))
                .bodyToMono(LatestIdrRatesResponse.class)
                .block();

        double usdRate = response.getRates().get("USD");
        double usdBuySpread = SpreadFactorUtils.calculateUsdBuySpread(usdRate);

        List<LatestIdrRatesWithSpreadRecord> resultList = response.getRates().entrySet().stream()
                .map((x) -> {
                    return LatestIdrRatesWithSpreadRecord.builder()
                            .countryCode(x.getKey())
                            .rates(new BigDecimal(x.getValue()))
                            .amount(response.getAmount())
                            .base(response.getBase())
                            .date(response.getDate())
                            .usdBuySpreadIdr(usdBuySpread)
                            .build();
                }).toList();

        return resultList;
    }
}
