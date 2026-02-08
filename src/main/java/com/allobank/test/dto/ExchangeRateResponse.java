package com.allobank.test.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRateResponse {
  private double amount;
  private String base;
  private String date;
  private Map<String, Double> rates;
}