package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.FrankfurterTimeseriesResponse;
import com.hanifnfl.allobank.dto.HistoricalIdrUsdView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private volatile List<HistoricalIdrUsdView> cache = List.of();

    @Value("${app.historical.from-date}")
    private String fromDate;

    @Value("${app.historical.to-date}")
    private String toDate;

    @Override
    public String getResourceTypeKey() {
        return "historical_idr_usd";
    }

    @Override
    public void loadData(WebClient client) {
        log.info("Fetching historical IDR→USD...");

        FrankfurterTimeseriesResponse response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + fromDate + ".." + toDate)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(FrankfurterTimeseriesResponse.class)
                .block();

        if (response == null || response.rates() == null) {
            throw new IllegalStateException("No historical rates returned.");
        }

        List<HistoricalIdrUsdView> views = response.rates().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    String date = entry.getKey();
                    Map<String, BigDecimal> rateMap = entry.getValue();
                    BigDecimal usd = rateMap.get("USD");
                    return new HistoricalIdrUsdView(date, usd);
                })
                .toList();

        this.cache = List.copyOf(views);
        log.info("historical_idr_usd loaded: {} records", cache.size());
    }

    @Override
    public List<HistoricalIdrUsdView> getCachedData() {
        return cache;
    }
}
