package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.client.FrankfurterClient;
import com.thasya.frankfurter.dto.CurrencyDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "supported_currencies";

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<?> fetchData() {
        Map<String, String> raw = client.getCurrencies();
        List<CurrencyDto> list = new ArrayList<>();
        if (raw != null) {
            raw.forEach((code, desc) -> list.add(new CurrencyDto(code, desc)));
        }
        return list;
    }
}
