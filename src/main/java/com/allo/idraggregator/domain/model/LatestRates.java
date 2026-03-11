package com.allo.idraggregator.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder(toBuilder = true)
public final class LatestRates {

    private final String base;

    private final LocalDate date;

    private final Map<String, Double> rates;

    private final Double usdBuySpreadIdr;
    
    public static class LatestRatesBuilder {
        
        public LatestRates build() {
            
            Map<String, Double> safeRates = rates == null ? Map.of() : Map.copyOf(rates);
            return new LatestRates(base, date, safeRates, usdBuySpreadIdr);
        }
    }

}