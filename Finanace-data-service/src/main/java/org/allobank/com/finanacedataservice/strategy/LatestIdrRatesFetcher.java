package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.clent.dto.FrankfurterLatestResponse;
import org.allobank.com.finanacedataservice.domain.LatestIdrRateResult;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.allobank.com.finanacedataservice.service.SpreadFactorCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component(LatestIdrRatesFetcher.RESOURCE_TYPE)
public class LatestIdrRatesFetcher implements IdrDataFetcher{

    public static final String RESOURCE_TYPE = "latest_idr_rates";
    private final SpreadFactorCalculator spreadFactorCalculator;

    public LatestIdrRatesFetcher(SpreadFactorCalculator spreadFactorCalculator) {
        this.spreadFactorCalculator = spreadFactorCalculator;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public void loadData(FrankfurterClient client, FinanceDataCache cache) {
        FrankfurterLatestResponse response = client.getLatestIdrRates();
        if (response == null || response.getBase() == null) {
            cache.setLatestIdrRates(List.of());
            return;
        }
        Map<String, BigDecimal> rates = response.getRates();
        BigDecimal rateUsd = rates.get("USD");
        if (rateUsd == null || BigDecimal.ZERO.compareTo(rateUsd) == 0) {
            cache.setLatestIdrRates(List.of());
            return;
        }
        BigDecimal spreadFactor = spreadFactorCalculator.calculateSpreadFactor();
        BigDecimal usdBuySpreadIdr = BigDecimal.ONE.divide(rateUsd, 8, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));
        LatestIdrRateResult result = new LatestIdrRateResult(
                response.getBase(),
                response.getDate(),
                rateUsd,
                usdBuySpreadIdr,
                spreadFactor
        );
        cache.setLatestIdrRates(List.of(result));
    }

    @Override
    public List<?> getCachedData(FinanceDataCache cache) {
        return cache.getLatestIdrRate();
    }
}
