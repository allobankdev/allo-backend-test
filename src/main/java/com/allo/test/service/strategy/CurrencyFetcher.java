package com.allo.test.service.strategy;

import com.allo.test.client.FrankfurterClient;
import com.allo.test.model.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public List<CurrencyDto> fetch() {

        var response = client.getCurrencies();

        return response.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new CurrencyDto(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
    }
}