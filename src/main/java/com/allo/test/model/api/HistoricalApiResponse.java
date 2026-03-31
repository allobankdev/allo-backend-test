package com.allo.test.model.api;

import lombok.Data;
import java.util.Map;

@Data
public class HistoricalApiResponse {

    private String base;
    private Map<String, Map<String, Double>> rates;

}