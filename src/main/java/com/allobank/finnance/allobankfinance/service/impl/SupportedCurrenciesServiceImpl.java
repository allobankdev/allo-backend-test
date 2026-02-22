package com.allobank.finnance.allobankfinance.service;

import com.allobank.finnance.allobankfinance.constant.ResourceTypeConstant;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategy;
import org.springframework.stereotype.Service;

@Service
public class SupportedCurrenciesServiceImpl implements FinanceDataStrategy {

    private final FrankfurterIntegrationService frankfurterService;

    public SupportedCurrenciesServiceImpl(FrankfurterIntegrationService frankfurterService) {
        this.frankfurterService = frankfurterService;
    }

    @Override
    public String getResourceType() {
        return ResourceTypeConstant.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object fetchData(FinanceRequestDto financeRequestDto) {
        return frankfurterService.getSupportedCurrencies();
    }
}
