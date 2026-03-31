package com.allo.test.service.strategy;

import com.allo.test.client.FrankfurterClient;
import com.allo.test.model.dto.HistoricalRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public List<HistoricalRateDto> fetch() {

        var response = client.getHistoricalRates();

        return response.getRates().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> 
                        new HistoricalRateDto(
                        entry.getKey(),
                        entry.getValue().get("USD").setScale(10, RoundingMode.HALF_UP)
                ))
                .toList();
    }
}