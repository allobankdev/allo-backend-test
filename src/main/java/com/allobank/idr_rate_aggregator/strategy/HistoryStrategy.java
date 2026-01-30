package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.wrapper.ChangeRateWrapper;
import com.allobank.idr_rate_aggregator.wrapper.HistoryRateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoryStrategy implements DataFetcher {

    private final WebClient webClient;

    private List<HistoryRateWrapper> cachedData = Collections.emptyList();

    @Override
    public List<HistoryRateWrapper> fetchData() {
        return cachedData;
    }

    @Override
    public void refreshData() {

        String startDate = "2024-01-01";
        String endDate = "2024-01-05";

        try {
            Map response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{startDate}..{endDate}")
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build(startDate, endDate))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<HistoryRateWrapper> historyList = new ArrayList<>();

            if (response != null && response.containsKey("rates")) {
                Map<String, Map<String, Object>> ratesByDate =
                        (Map<String, Map<String, Object>>) response.get("rates");

                for (Map.Entry<String, Map<String, Object>> entry : ratesByDate.entrySet()) {

                    String dateString = entry.getKey();
                    Map<String, Object> dailyRates = entry.getValue();
                    Object val = dailyRates.get("USD");

                    if (val != null) {
                        BigDecimal rateValue = new BigDecimal(val.toString());
                        HistoryRateWrapper dto = HistoryRateWrapper.builder()
                                .date(LocalDate.parse(dateString))
                                .rateUsd(rateValue)
                                .build();

                        historyList.add(dto);
                    }
                }
            }

            historyList.sort(Comparator.comparing(HistoryRateWrapper::getDate));
            this.cachedData = historyList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
