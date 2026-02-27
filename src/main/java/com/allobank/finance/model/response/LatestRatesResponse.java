package com.allobank.finance.model.response;

import com.allobank.finance.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Locale;
import java.util.Map;

@RecordBuilder
public record LatestRatesResponse(
        String currency,
        double rate,
        @JsonIgnore Map<String, Object> dynamicFields
) implements BaseResponse {

    public LatestRatesResponse(String currency, double rate, double buySpread) {
        this(currency, rate, Map.of(currency.toLowerCase(Locale.ROOT) + "_buy_spread_idr", buySpread));
    }

    @JsonAnyGetter
    public Map<String, Object> dynamic() {
        return dynamicFields;
    }
}
