package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.clent.dto.FrankfurterTimeSeriesResponse;
import org.allobank.com.finanacedataservice.domain.HistoricalIdrUsdResult;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(HistoricalIdrUsdFetcher.RESOURCE_TYPE)
public class HistoricalIdrUsdFetcher implements IdrDataFetcher {
    public static final String RESOURCE_TYPE = "historical_idr_usd";

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public void loadData(FrankfurterClient client, FinanceDataCache cache) {
        FrankfurterTimeSeriesResponse response = client.getHistoricalIdrUsd();
        List<HistoricalIdrUsdResult> results = new ArrayList<>();
        if (response != null && response.getRates() != null) {
            for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : response.getRates().entrySet()) {
                Map<String, BigDecimal> dailyRates = entry.getValue();
                BigDecimal usd = dailyRates.get("USD");
                if (usd != null) {
                    results.add(new HistoricalIdrUsdResult(entry.getKey(), usd));
                }
            }
        }
        cache.setHistoryIdrUsd(results);
    }

    @Override
    public List<?> getCachedData(FinanceDataCache cache) {
        return cache.getHistoricalIdrUsd();
    }
}
