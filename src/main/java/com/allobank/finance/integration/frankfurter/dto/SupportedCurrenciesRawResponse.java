package com.allobank.finance.integration.frankfurter.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Map;

@RecordBuilder
public record SupportedCurrenciesRawResponse(
        @JsonAnySetter
        Map<String, String> currencies
) {
}
