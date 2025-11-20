package com.chikohakles.allobank.agregator.constant;

import com.chikohakles.allobank.agregator.dto.Currency;
import lombok.Getter;

import java.util.List;

public class CurrencyList {
    private static List<Currency> currencies;

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
