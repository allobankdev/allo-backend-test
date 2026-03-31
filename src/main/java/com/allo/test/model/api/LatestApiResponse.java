package com.allo.test.model.api;

import lombok.Data;
import java.util.Map;

@Data
public class LatestApiResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;

}