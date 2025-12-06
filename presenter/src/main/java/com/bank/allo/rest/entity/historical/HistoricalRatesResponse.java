package com.bank.allo.rest.entity.historical;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder")
public class HistoricalRatesResponse {
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;
}
