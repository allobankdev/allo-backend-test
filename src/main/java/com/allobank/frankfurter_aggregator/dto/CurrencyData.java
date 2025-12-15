package com.allobank.frankfurter_aggregator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class CurrencyData {
    private Map<String, String> currencies;
}
