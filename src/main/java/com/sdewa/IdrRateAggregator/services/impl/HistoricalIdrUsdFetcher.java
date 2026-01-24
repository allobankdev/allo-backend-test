package com.sdewa.IdrRateAggregator.services.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sdewa.IdrRateAggregator.dtoes.HistoricalIdrUsdResponse;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;

@Service
public class HistoricalIdrUsdFetcher implements IDRDataFetcher<HistoricalIdrUsdResponse> {
    private final WebClient webClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public HistoricalIdrUsdResponse fetchData() {
        String uri = generateTimeRageUri();

        HistoricalIdrUsdResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(HistoricalIdrUsdResponse.class)
                .block(); // block since we only need it once at startup

        if (response == null) {
            throw new RuntimeException("Failed to fetch historical IDR → USD rates");
        }

        return response;
    }

    private String generateTimeRageUri(){
      // Get current date
        LocalDate now = LocalDate.now();

        // Get previous month
        YearMonth prevMonth = YearMonth.from(now.minusMonths(1));

        // First date of previous month
        LocalDate firstDay = prevMonth.atDay(1);

        // Last date of previous month
        LocalDate lastDay = prevMonth.atEndOfMonth();

        // Format as yyyy-MM-dd..yyyy-MM-dd?from=IDR&to=USD
        String result = String.format("%s..%s?from=IDR&to=USD",
                firstDay.format(FORMATTER),
                lastDay.format(FORMATTER));
        return result;
    }
}
