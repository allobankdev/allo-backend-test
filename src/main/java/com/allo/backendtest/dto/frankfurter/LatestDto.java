package com.allo.backendtest.dto.frankfurter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LatestDto(String message,
                        BigDecimal amount,
                        String base,
                        LocalDate date,
                        Map<String, BigDecimal> rates,
                        @JsonProperty("USD_BuySpread_IDR") BigDecimal buySpread) { }

