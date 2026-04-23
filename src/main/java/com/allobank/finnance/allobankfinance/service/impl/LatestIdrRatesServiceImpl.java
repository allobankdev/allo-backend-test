package com.allobank.finnance.allobankfinance.service.impl;

import com.allobank.finnance.allobankfinance.config.OtherProperties;
import com.allobank.finnance.allobankfinance.constant.Currency;
import com.allobank.finnance.allobankfinance.constant.ResourceTypeConstant;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategy;
import com.allobank.finnance.allobankfinance.util.SpreadFactorCalculatorUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LatestIdrRatesServiceImpl implements FinanceDataStrategy {

    private final FrankfurterIntegrationService frankfurterService;
    private final OtherProperties otherProperties;

    public LatestIdrRatesServiceImpl(FrankfurterIntegrationService frankfurterService, OtherProperties otherProperties) {
        this.frankfurterService = frankfurterService;
        this.otherProperties = otherProperties;
    }


    @Override
    public String getResourceType() {
        return ResourceTypeConstant.LATEST_IDR_RATES;
    }

    @Override
    public Object fetchData(FinanceRequestDto financeRequestDto) {
        var rates = frankfurterService.getLatestUsdRates(Currency.IDR.name());
        BigDecimal rateUsd = rates.getRates().get("USD");
        BigDecimal spreadFactor = SpreadFactorCalculatorUtil.calculateSpreadFactor(otherProperties.getGithubUsername());
        return BigDecimal.ONE
                .divide(rateUsd, 10, RoundingMode.HALF_UP)
                .multiply(
                        BigDecimal.ONE.add(spreadFactor)
                );
    }
}
