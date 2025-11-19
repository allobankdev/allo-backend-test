package com.allo.test.modules.finance.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrankfurterCurrenciesResponse {

    private Map<String, String> currencies;
}
