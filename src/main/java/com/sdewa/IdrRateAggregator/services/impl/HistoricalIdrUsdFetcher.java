package com.sdewa.IdrRateAggregator.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sdewa.IdrRateAggregator.dtoes.HistoricalIdrUsdResponse;
import com.sdewa.IdrRateAggregator.dtoes.HistoricalIdrUsdResponseRecord;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;

@Service
public class HistoricalIdrUsdFetcher implements IDRDataFetcher<List<HistoricalIdrUsdResponseRecord>> {
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
    public List<HistoricalIdrUsdResponseRecord> fetchData() {
        String uri = generateTimeRageUri();

        HistoricalIdrUsdResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(HistoricalIdrUsdResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Failed to fetch historical IDR → USD rates");
        }
        List<HistoricalIdrUsdResponseRecord> resultList = response.getRates().entrySet().stream()
                .flatMap((x) -> {
                    var value = x.getValue();
                    return value.entrySet().stream()
                            .map((y) -> {
                                return HistoricalIdrUsdResponseRecord.builder()
                                        .date(x.getKey())
                                        .currency(y.getKey())
                                        .rates(new BigDecimal(y.getValue()))
                                        .amount(response.getAmount())
                                        .base(response.getBase())
                                        .startDate(response.getStartDate())
                                        .endDate(response.getEndDate())
                                        .build();
                            });
                }).toList();

        return resultList;
    }

    private String generateTimeRageUri() {
        LocalDate now = LocalDate.now();

        YearMonth prevMonth = YearMonth.from(now.minusMonths(1));

        LocalDate firstDay = prevMonth.atDay(1);

        LocalDate lastDay = prevMonth.atEndOfMonth();

        String result = String.format("%s..%s?from=IDR&to=USD",
                firstDay.format(FORMATTER),
                lastDay.format(FORMATTER));
        return result;
    }
}
