package com.allobank.allobackendtest.dto;

import java.util.Map;

public record CurrenciesResponse(
        String resourceType,
        Map<String, String> currencies
) {}
