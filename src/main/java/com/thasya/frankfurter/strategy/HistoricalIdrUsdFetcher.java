package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.client.FrankfurterClient;
import com.thasya.frankfurter.dto.FrankfurterTimeseriesResponse;
import com.thasya.frankfurter.dto.HistoricalIdrUsdDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "historical_idr_usd";

    private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<?> fetchData() {
        // range fixed sesuai soal
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 5);

        FrankfurterTimeseriesResponse response = client.getHistoricalIdrUsd(start, end);
        if (response == null) {
            return Collections.emptyList();
        }
        Map<String, Map<String, Double>> rates = response.getRates();

        List<HistoricalIdrUsdDto> result = new ArrayList<>();
        if (rates != null) {
            for (Map.Entry<String, Map<String, Double>> entry : rates.entrySet()) {
                String date = entry.getKey();
                Double usdRate = entry.getValue().get("USD");
                if (usdRate != null) {
                    result.add(new HistoricalIdrUsdDto(date, usdRate));
                }
            }
        }
        // Sort by date ascending
        result.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return result;
    }
}
