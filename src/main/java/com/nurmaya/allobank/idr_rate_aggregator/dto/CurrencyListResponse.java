package com.nurmaya.allobank.idr_rate_aggregator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class CurrencyListResponse {
    private Map<String, String> currencies;
}

