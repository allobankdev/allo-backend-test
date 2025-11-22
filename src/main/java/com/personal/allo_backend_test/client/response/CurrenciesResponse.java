package com.personal.allo_backend_test.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenciesResponse {
  private Map<String, String> currencies;
}

