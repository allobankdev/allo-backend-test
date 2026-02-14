package com.vii.idragregator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private Map<String, String> currencies;
}
