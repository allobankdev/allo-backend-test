package com.api.allorestapi.service;

import com.api.allorestapi.client.Currencies;
import com.api.allorestapi.client.HistoricalRates;
import com.api.allorestapi.client.LatestRates;
import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.model.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceDataService {

    private final LatestRates latestRatesResource;
    private final HistoricalRates historicalRatesResource;
    private final Currencies currenciesResource;
    private final SpreadCalculator spreadCalculator;

    public Mono<FinanceDataResponse> getData(ResourceType resourceType) {
        log.info("Fetching data for resourceType: {}", resourceType.getValue());
        return switch (resourceType) {
            case LATEST_IDR_RATES     -> fetchLatestIdrRates();
            case HISTORICAL_IDR_USD   -> fetchHistoricalIdrUsd();
            case SUPPORTED_CURRENCIES -> fetchSupportedCurrencies();
        };
    }

    private Mono<FinanceDataResponse> fetchLatestIdrRates() {
        return latestRatesResource.fetch().map(response -> {
            Map<String, BigDecimal> rates = response.getRates();
            BigDecimal rateUsd = rates.get("USD");
            BigDecimal usdBuySpread = spreadCalculator.calculate(rateUsd);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("base", response.getBase());
            entry.put("date", response.getDate());
            entry.put("rates", rates);
            entry.put("USD_BuySpread_IDR", usdBuySpread);
            entry.put("spreadFactor", spreadCalculator.getSpreadFactor());

            log.debug("USD_BuySpread_IDR={} spreadFactor={}", usdBuySpread, spreadCalculator.getSpreadFactor());

            return FinanceDataResponse.builder()
                    .resourceType(ResourceType.LATEST_IDR_RATES.getValue())
                    .data(List.of(entry))
                    .build();
        });
    }

    private Mono<FinanceDataResponse> fetchHistoricalIdrUsd() {
        return historicalRatesResource.fetch().map(response -> {
            List<Object> data = response.getRates().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("date", entry.getKey());
                        item.put("rates", entry.getValue());
                        return (Object) item;
                    })
                    .collect(Collectors.toList());

            return FinanceDataResponse.builder()
                    .resourceType(ResourceType.HISTORICAL_IDR_USD.getValue())
                    .data(data)
                    .build();
        });
    }

    private Mono<FinanceDataResponse> fetchSupportedCurrencies() {
        return currenciesResource.fetch().map(currencies -> {
            List<Object> data = currencies.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> {
                        Map<String, String> item = new LinkedHashMap<>();
                        item.put("code", entry.getKey());
                        item.put("name", entry.getValue());
                        return (Object) item;
                    })
                    .collect(Collectors.toList());

            return FinanceDataResponse.builder()
                    .resourceType(ResourceType.SUPPORTED_CURRENCIES.getValue())
                    .data(data)
                    .build();
        });
    }
}