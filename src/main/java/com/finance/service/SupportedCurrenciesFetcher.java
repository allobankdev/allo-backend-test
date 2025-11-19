package com.finance.service;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.RateResponse;
import com.finance.dto.SupportedCurrenciesResponse;
import com.finance.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class SupportedCurrenciesFetcher implements DataFetcher{
    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() { return "supported_currencies"; }

    @Override
    public List<Map<String, Object>> fetch() {

        SupportedCurrenciesResponse dto = client.getSupportedCurrencies()
                .blockOptional()
                .orElseThrow(() -> new ExternalServiceException(AppConstant.NO_RESPONSE_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR));

        String currencies = dto.get("IDR");
        if (currencies == null || currencies.isEmpty()) {
            throw new ExternalServiceException(AppConstant.EMPTY_CURRENCIES_RESPONSE_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return List.of(out);
    }
}
