package com.finance.service;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.external.SupportedCurrenciesResponse;
import com.finance.dto.internal.CurrencyInfoResponse;
import com.finance.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
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
                .orElseThrow(() -> new ExternalServiceException(
                        AppConstant.NO_RESPONSE_FROM_API_MESSAGE,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));

        return dto.getSupportedCurrencies()
                .entrySet()
                .stream()
                .map(entry -> new CurrencyInfoResponse(entry.getKey(), entry.getValue()))
                .map(info -> Map.<String, Object>of(
                        "currencyCode", info.getCurrencyCode(),
                        "currencyName", info.getCurrencyName()
                ))
                .toList();
    }

}
