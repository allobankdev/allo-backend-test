package com.chikohakles.allobank.agregator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class DateQueryResponse {
    BigDecimal amount;
    String baseCurrency;
    Date startDate;
    Date endDate;
    Map<Date, Map<String, BigDecimal>> rates;
}
