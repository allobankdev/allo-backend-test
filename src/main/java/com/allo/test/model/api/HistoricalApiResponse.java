package com.allo.test.model.api;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalApiResponse {

    private String base;
    private Map<String, Map<String, BigDecimal>> rates;

}