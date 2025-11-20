package com.allo.idr.service;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.enums.ResourceType;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.CurrencyResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher{

    private final ExternalApiClient resT;

    public SupportedCurrenciesFetcher(ExternalApiClient resT) {
        this.resT = resT;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public List<CurrencyResponse> fetcData() {
        try {
            Map<String, String> res = resT.getCurrencies();
            List<CurrencyResponse> curr = new ArrayList<>();
            if (res != null) res.forEach((key, val) -> curr.add(new CurrencyResponse(key, val)));
            return curr;
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Failed get currencies", e);
        }
    }
}
