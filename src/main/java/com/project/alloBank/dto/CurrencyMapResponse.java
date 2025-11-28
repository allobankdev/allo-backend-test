package com.project.alloBank.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CurrencyMapResponse {
    private Map<String, String> currencies;

}
