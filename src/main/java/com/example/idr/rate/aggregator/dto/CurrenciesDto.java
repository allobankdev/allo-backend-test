package com.example.idr.rate.aggregator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrenciesDto {
    private Map<String, String> currencies;
}
