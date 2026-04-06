package com.allobank.idr_rate_aggregator.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CurrenciesResponse {

    private Map<String, String> currencies;
}