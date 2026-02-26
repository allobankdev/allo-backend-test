package org.allobank.com.finanacedataservice.domain;

public class SupportedCurrenciesResult {
    private final String symbol;
    private final String name;

    public SupportedCurrenciesResult(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}

