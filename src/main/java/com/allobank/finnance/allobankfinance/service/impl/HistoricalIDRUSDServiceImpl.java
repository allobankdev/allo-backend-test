package com.allobank.finnance.allobankfinance.service.impl;

import com.allobank.finnance.allobankfinance.constant.Currency;
import com.allobank.finnance.allobankfinance.constant.ResourceTypeConstant;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class HistoricalIDRUSDServiceImpl implements FinanceDataStrategy {

    private final FrankfurterIntegrationService frankfurterService;

    public HistoricalIDRUSDServiceImpl(FrankfurterIntegrationService frankfurterService ) {
        this.frankfurterService = frankfurterService;
    }

    @Override
    public String getResourceType() {
        return ResourceTypeConstant.HISTORICAL_IDR_USD;
    }

    @Override
    public Object fetchData(FinanceRequestDto financeRequestDto) {
        try {
            if (financeRequestDto.getStartDate() == null || financeRequestDto.getEndDate() == null) {
                throw new IllegalArgumentException(
                        "startDate and endDate are required for historical data"
                );
            }
            return frankfurterService.getHistoricalRates(
                    financeRequestDto.getStartDate(),
                    financeRequestDto.getEndDate(), Currency.IDR.name(), Currency.USD.name());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
