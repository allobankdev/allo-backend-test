package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.service.CurrenciesService;

@Component("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final CurrenciesService currenciesService;

    public SupportedCurrenciesStrategy(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @Override
    public Object fetchData() {
        return currenciesService.fetchSupportedCurrencies();
    }
}
