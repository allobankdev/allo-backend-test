package com.bank.allo.rest.entity.supported;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder(builderClassName = "Builder")
public class SupportedCurrenciesResponse {
    private Map<String, String> currencies;
}
