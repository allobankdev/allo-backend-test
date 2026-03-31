package com.allo.test.service.strategy;

import com.allo.test.client.FrankfurterClient;
import com.allo.test.model.dto.LatestRateDto;
import com.allo.test.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LatestRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final SpreadCalculator spreadCalculator;

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public List<LatestRateDto> fetch() {

        var response = client.getLatestRates();
        double spread = spreadCalculator.getSpread();

        return response.getRates().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {

                    String currency = entry.getKey();
                    Double rate = entry.getValue();

                    Double usdBuyRate = null;

                    if ("USD".equals(currency)) {
                        usdBuyRate = (1 / rate) * (1 + spread);
                    }

                    return new LatestRateDto(currency, rate, usdBuyRate);
                })
                .toList();
    }
}