package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.adapter.out.external.ExternalServiceImpl;
import com.ade.exchangerateagregator.application.dto.in.FinanceBaseResponse;
import com.ade.exchangerateagregator.application.dto.in.SupportedCurrencyResp;
import com.ade.exchangerateagregator.domain.constant.ResourceType;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupportedCurrencyService implements FinanceBaseService {
    private final ExternalServiceImpl externalService;
    @Override
    public ResourceType getSourceType() {
        return ResourceType.supported_currencies;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        var currencies = externalService.getCurrencies();
        return mapCurrenciesResponse(currencies);
    }

    private List<? extends FinanceBaseResponse> mapCurrenciesResponse(Map<String,String> response){
        List<? extends FinanceBaseResponse> result;
        result = response
                .entrySet()
                .stream()
                .map(data -> SupportedCurrencyResp.builder()
                        .currencyCode(data.getKey())
                        .name(data.getValue())
                        .build())
                .toList();
        return result;
    }
}
