package com.allobank.finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyResponse {

    private Map<String, String> currencies;
}
