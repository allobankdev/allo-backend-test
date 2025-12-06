package com.bank.allo.domain.idr;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class SupportedCurrencies {
    Map<String, String> currencies;
}
