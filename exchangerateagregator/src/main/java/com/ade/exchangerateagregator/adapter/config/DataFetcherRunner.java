package com.ade.exchangerateagregator.adapter.config;

import com.ade.exchangerateagregator.adapter.implementation.HistoryService;
import com.ade.exchangerateagregator.adapter.implementation.LastIdrRatesService;
import com.ade.exchangerateagregator.adapter.implementation.SupportedCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class DataFetcherRunner implements CommandLineRunner {
    private final HistoryService historyService;
    private final LastIdrRatesService lastIdrRatesService;
    private final SupportedCurrencyService supportedCurrencyService;
    private final ObjectMapper objectMapper;
    @Override
    public void run(String... args) {

        System.out.println("========== Fetching latest IDR rate...");
        var rate = lastIdrRatesService.fetchData();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rate));

        System.out.println("========== Fetching history...");
        var history = historyService.fetchData();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(history));

        System.out.println("========== Fetching currencies...");
        var currencies = supportedCurrencyService.fetchData();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencies));
    }
}
