package com.allo.idraggregator.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public final class Currency {

    private final Map<String, String> currencies;
    
    public static class CurrencyBuilder {

        public Currency build() {

            Map<String, String> safeCurrencies = currencies == null ? Map.of() : Map.copyOf(currencies);
            return new Currency(safeCurrencies);
        }
    }

}
