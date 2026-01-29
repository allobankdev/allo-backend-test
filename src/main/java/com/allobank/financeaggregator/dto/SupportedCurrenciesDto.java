package com.allobank.financeaggregator.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Map;

public record SupportedCurrenciesDto(@JsonValue Map<String, String> currencies) {
}
