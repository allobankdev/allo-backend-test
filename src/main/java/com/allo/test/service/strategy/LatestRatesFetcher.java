package com.allo.test.service.strategy;

import com.allo.test.client.FrankfurterClient;
import com.allo.test.model.dto.LatestRateDto;
import com.allo.test.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        BigDecimal spread = BigDecimal.valueOf(spreadCalculator.getSpread());

        return response.getRates().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {

                    String currency = entry.getKey();
                    BigDecimal rate = entry.getValue().setScale(10, RoundingMode.HALF_UP);

                    BigDecimal usdBuyRate = null;

                    if ("USD".equals(currency)) {
                        usdBuyRate = BigDecimal.ONE
                                .divide(rate, 10, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.ONE.add(spread))
                                .setScale(10, RoundingMode.HALF_UP);
                    }

                    return new LatestRateDto(currency, rate, usdBuyRate);
                })
                .toList();
    }
}