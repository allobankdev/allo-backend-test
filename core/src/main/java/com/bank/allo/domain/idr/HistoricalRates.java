package com.bank.allo.domain.idr;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class HistoricalRates {
    String startDate;
    String endDate;
    Map<String, Map<String, Double>> rates;
}
