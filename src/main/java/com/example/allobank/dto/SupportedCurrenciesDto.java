package com.example.allobank.dto;

import java.util.Map;
import lombok.Data;

@Data
public class SupportedCurrenciesDto {
    /**
     * Frankfurter /currencies returns map { "USD": "United States Dollar", ... }
     */
    private Map<String, String> currencies;
}