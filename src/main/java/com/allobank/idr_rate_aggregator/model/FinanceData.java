package com.allobank.idr_rate_aggregator.model;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class FinanceData {

    private final String resourceType;
    private final Object data;
    private final LocalDateTime fetchedAt;

    // Static factory methods untuk tiap resource type
    public static FinanceData ofLatestRates(Object data) {
        return FinanceData.builder()
                .resourceType("latest_idr_rates")
                .data(data)
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    public static FinanceData ofHistoricalRates(Object data) {
        return FinanceData.builder()
                .resourceType("historical_idr_usd")
                .data(data)
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    public static FinanceData ofCurrencies(Object data) {
        return FinanceData.builder()
                .resourceType("supported_currencies")
                .data(data)
                .fetchedAt(LocalDateTime.now())
                .build();
    }
}
