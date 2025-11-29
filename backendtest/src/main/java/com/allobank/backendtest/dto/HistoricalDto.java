package com.allobank.backendtest.dto;

import java.math.BigDecimal;

public record HistoricalDto(String date, BigDecimal usdRate) {}
