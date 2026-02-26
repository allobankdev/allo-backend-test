package com.api.allorestapi.service;

import com.api.allorestapi.client.Currencies;
import com.api.allorestapi.client.HistoricalRates;
import com.api.allorestapi.client.LatestRates;
import com.api.allorestapi.model.IdrSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateAggregatorService {

    private final LatestRates latestRatesResource;
    private final HistoricalRates historicalRatesResource;
    private final Currencies currenciesResource;

    public Mono<IdrSummaryResponse> aggregateIdrData() {
        log.info("Initiating parallel fetch of all Frankfurter resources");

        return Mono.zip(
                latestRatesResource.fetch(),
                historicalRatesResource.fetch(),
                currenciesResource.fetch()
        ).map(tuple -> {
            var latest     = tuple.getT1();
            var historical = tuple.getT2();
            var currencies = tuple.getT3();

            log.info("All resources fetched successfully; assembling response");

            return IdrSummaryResponse.builder()
                    // Latest rates
                    .latestDate(latest.getDate())
                    .latestRates(latest.getRates())
                    // Historical time-series
                    .historicalStart(historical.getStartDate())
                    .historicalEnd(historical.getEndDate())
                    .historicalRates(historical.getRates())
                    // Currency list
                    .supportedCurrencies(currencies)
                    .build();
        });
    }
}
