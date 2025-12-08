package com.example.allobank.project.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupportedCurrencies {
    private Map<String, String> currencies;  
}
