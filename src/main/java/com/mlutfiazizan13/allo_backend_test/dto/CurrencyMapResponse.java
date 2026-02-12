package com.mlutfiazizan13.allo_backend_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyMapResponse {

    private Map<String, String> currencies;
}
