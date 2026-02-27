package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.application.service.SpreadFactorService;
import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import com.amri.apiintegration.dto.frankfurter.LatestRatesDto;
import com.amri.apiintegration.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final CurrencyRatesGateway currencyRatesGateway;
    private final SpreadFactorService spreadFactorService;

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public FinanceResourceResultDto fetch() {
        LatestRatesDto latestRatesDto = currencyRatesGateway.getLatestRates("IDR");

        BigDecimal usdRate = latestRatesDto.rates().get("USD");
        if (usdRate == null || BigDecimal.ZERO.compareTo(usdRate) == 0) {
            throw new ResourceNotFoundException("USD rate not available for base IDR");
        }

        BigDecimal usdBuySpreadIdr = BigDecimal.ONE
                .divide(usdRate, MathContext.DECIMAL64)
                .multiply(BigDecimal.ONE.add(spreadFactorService.getSpreadFactor()))
                .setScale(8, RoundingMode.HALF_UP);

        LatestRatesDto enriched = new LatestRatesDto(
                latestRatesDto.base(),
                latestRatesDto.date(),
                latestRatesDto.rates(),
                usdBuySpreadIdr
        );

        return new FinanceResourceResultDto(resourceType(), enriched);
    }
}
