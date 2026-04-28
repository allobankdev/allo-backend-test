package com.ade.exchangerateagregator.application.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportedCurrencyResp implements FinanceBaseResponse {
    private String currencyCode;
    private String name;
}
