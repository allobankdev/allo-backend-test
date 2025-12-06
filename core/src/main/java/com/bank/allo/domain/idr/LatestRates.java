package com.bank.allo.domain.idr;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class LatestRates {
    String base;
    String date;
    Map<String, Double> rates;
    Double usdBuySpreadIdr;
    Double spreadFactor;
}
