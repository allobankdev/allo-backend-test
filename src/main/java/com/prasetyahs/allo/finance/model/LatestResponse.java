package com.prasetyahs.allo.finance.model;

import java.util.Map;

public record LatestResponse(
        double amount,
        String base,
        String date,
        Map<String, Double> rates) {
}
