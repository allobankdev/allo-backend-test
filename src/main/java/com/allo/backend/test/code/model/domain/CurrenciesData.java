package com.allo.backend.test.code.model.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CurrenciesData {
    private Map<String, String> currencies;
    private Integer count;
}
