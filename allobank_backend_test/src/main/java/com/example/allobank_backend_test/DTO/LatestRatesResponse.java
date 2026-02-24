package com.example.allobank_backend_test.DTO;

import java.util.Map;

public record LatestRatesResponse(Map<String, Double> rates, double usdBuySpreadIdr) {
}
