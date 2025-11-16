package id.co.microservice.currency.currency_service.service;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;

public interface CurrencyService {

    /**
     * Execute strategy based on resource type
     * @param resourceType
     * @return
     */
    CurrencyResponseDto executeStrategy(String resourceType);

}
