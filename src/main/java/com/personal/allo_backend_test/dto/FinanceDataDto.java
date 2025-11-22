package com.personal.allo_backend_test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FinanceDataDto(
  String code,
  String name,
  Double rate,
  String date,
  Double usdBuySpreadIdr
){}
