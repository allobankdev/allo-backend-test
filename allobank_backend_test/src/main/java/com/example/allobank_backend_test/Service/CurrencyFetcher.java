package com.example.allobank_backend_test.Service;

import com.example.allobank_backend_test.Client.FrankfurterClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CurrencyFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return client.getCurrencies();
    }
}
