package com.finance.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyInfoResponse {
    private String currencyCode;
    private String currencyName;
}
