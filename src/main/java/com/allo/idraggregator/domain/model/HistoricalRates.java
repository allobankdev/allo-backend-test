package com.allo.idraggregator.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public final class HistoricalRates {

    private final Double amount;

    private final String base;

    private final Map<String, Map<String, Double>> rates;
    
    public static class HistoricalRatesBuilder {

        public HistoricalRates build() {
            
            Map<String, Map<String, Double>>  safeRates = rates == null ? Map.of() : Map.copyOf(rates);
            return new HistoricalRates(amount, base, safeRates);
        }
    }

}
