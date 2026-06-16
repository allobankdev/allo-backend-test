package com.example.allobank_backend_test.Service;

import com.example.allobank_backend_test.Client.FrankfurterClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class HistoricalFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return client.getHistorical();
    }
}
