package com.finance.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenciesDTO {
    private Map<String, String> currencies;
    private String resourceType;
}