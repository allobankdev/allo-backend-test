package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import org.springframework.stereotype.Service;

@Service
public class CurrenciesStrategy implements BaseStrategy{
    AgregatorService agregatorService;
    CurrenciesStrategy(AgregatorService agregatorService) {
        this.agregatorService = agregatorService;
    }
    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object getData() {
        return agregatorService.getCurrencies();
    }
}
