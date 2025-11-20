package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LatestRateStrategy implements BaseStrategy {
    AgregatorService agregatorService;

    LatestRateStrategy(AgregatorService agregatorService) {
        this.agregatorService = agregatorService;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public Object getData() {
        return agregatorService.getLatest("IDR");
    }
}
