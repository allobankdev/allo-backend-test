package com.allobank.dto;

import java.util.Map;

public record CurrenciesResponse(
        Map<String, String> currencies
) {}