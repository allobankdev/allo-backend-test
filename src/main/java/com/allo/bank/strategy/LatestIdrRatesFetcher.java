package com.allo.bank.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.client.dto.FrankfurterLatestResponse;
import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.util.SpreadFactorCalculator;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "latest_idr_rates";

    private final FrankfurterClient frankfurterClient;
    private final SpreadFactorCalculator spreadFactorCalculator;

    public LatestIdrRatesFetcher(FrankfurterClient frankfurterClient,
                                 SpreadFactorCalculator spreadFactorCalculator) {
        this.frankfurterClient = frankfurterClient;
        this.spreadFactorCalculator = spreadFactorCalculator;
    }

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataItem> fetch() {
        FrankfurterLatestResponse response = frankfurterClient.fetchLatestIdrRates();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("base", response.getBase());
        payload.put("date", response.getDate());
        payload.put("amount", response.getAmount());
        payload.put("rates", response.getRates() == null ? Map.of() : Map.copyOf(response.getRates()));
        payload.put("USD_BuySpread_IDR", calculateUsdBuySpread(response.getRates()));
        payload.put("spreadFactor", spreadFactorCalculator.calculateSpreadFactor());

        return List.of(new FinanceDataItem(resourceType(), Map.copyOf(payload)));
    }

    private BigDecimal calculateUsdBuySpread(Map<String, Double> rates) {
        if (rates == null || rates.get("USD") == null || rates.get("USD") == 0D) {
            return BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP);
        }

        BigDecimal rateUsd = BigDecimal.valueOf(rates.get("USD"));
        BigDecimal baseValue = BigDecimal.ONE.divide(rateUsd, 6, RoundingMode.HALF_UP);
        BigDecimal spreadMultiplier = BigDecimal.ONE.add(spreadFactorCalculator.calculateSpreadFactor());

        return baseValue.multiply(spreadMultiplier).setScale(6, RoundingMode.HALF_UP);
    }
}
