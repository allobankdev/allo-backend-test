package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final CurrencyRatesGateway currencyRatesGateway;

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public FinanceResourceResultDto fetch() {
        return new FinanceResourceResultDto(
                resourceType(),
                currencyRatesGateway.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD")
        );
    }
}
