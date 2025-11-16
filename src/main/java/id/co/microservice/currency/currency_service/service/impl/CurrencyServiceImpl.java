package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.exception.CurrencyException;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final Map<String, CurrencyStrategy> strategies;

    @Autowired
    public CurrencyServiceImpl(Map<String, CurrencyStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public CurrencyResponseDto executeStrategy(String resourceType) {
        CurrencyStrategy strategy = strategies.get(resourceType);
        if (strategy == null) {
            throw new CurrencyException("Unsupported currency type: " + resourceType);
        }
        return strategy.execute();
    }
}
