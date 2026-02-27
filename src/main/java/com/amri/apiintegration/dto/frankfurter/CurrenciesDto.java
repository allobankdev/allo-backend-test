package com.amri.apiintegration.dto.frankfurter;

import java.util.Map;

public record CurrenciesDto(Map<String, String> currencies) implements FinanceDataDto {
}
