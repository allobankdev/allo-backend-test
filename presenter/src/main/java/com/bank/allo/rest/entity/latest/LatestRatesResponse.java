package com.bank.allo.rest.entity.latest;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder")
public class LatestRatesResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;
    private Double spreadFactor;
}
