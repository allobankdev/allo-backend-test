package com.self.bs.source.startup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.service.IExchangeRateDataFetcher;

@Component
public class StartUpApplicationRunner implements CommandLineRunner{
    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Autowired
    protected IExchangeRateDataFetcher currencyListDataFetcherService;

    @Autowired
    protected IExchangeRateDataFetcher historyExchangeRateDataFetcherService;

    @Autowired
    protected IExchangeRateDataFetcher latestCurrencyRateDataFetcherService;

    @Override
    public void run(String... args) throws Exception {
        // Fetch Exchange Rate Data   
        ExchangeRateDataFetcherRequestDto requestDto = new ExchangeRateDataFetcherRequestDto(
            LocalDate.now().minusDays(exchangeRateProperties.getDefaultHistoricalRangeDate()).format(DateTimeFormatter.ofPattern(exchangeRateProperties.getDateFormat())),
            LocalDate.now().format(DateTimeFormatter.ofPattern(exchangeRateProperties.getDateFormat())), 
            exchangeRateProperties.getBaseCurrency(), exchangeRateProperties.getTargetCurrency());

        currencyListDataFetcherService.fetchData(null);
        historyExchangeRateDataFetcherService.fetchData(requestDto);
        latestCurrencyRateDataFetcherService.fetchData(requestDto);
    }
}
