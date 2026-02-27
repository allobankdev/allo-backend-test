package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    private final CurrencyRatesGateway currencyRatesGateway;

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public FinanceResourceResultDto fetch() {
        return new FinanceResourceResultDto(resourceType(), currencyRatesGateway.getCurrencies());
    }
}
