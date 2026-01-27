package com.interview.backend.strategy.impl;

import com.interview.backend.models.TimeSeriesResponse;
import com.interview.backend.strategy.IDRDataFetcher;
import com.interview.backend.utils.RateFormatterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Value("${frankfurter.api.base-url:https://api.frankfurter.app}")
    private String baseUrl;

    @Value("${app.history.default-start:2024-01-01}")
    private String defaultStart;

    @Value("${app.history.default-end:2024-01-05}")
    private String defaultEnd;

    @Override
    public Map<String, Object> fetchData() {
        return fetchData(Map.of());
    }

    @Override
    public Map<String, Object> fetchData(Map<String, String> params) {
        String start = params.getOrDefault("start_date", defaultStart);
        String end = params.getOrDefault("end_date", defaultEnd);

        try {
            validateRange(start, end);

            String url = baseUrl + "/" + start + ".." + end + "?from=IDR&to=USD";

            TimeSeriesResponse response = restTemplate.getForObject(url, TimeSeriesResponse.class);

            if (response == null) {
                throw new RuntimeException("Failed to fetch historical rates - null response");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("base", response.getBase());
            result.put("start_date", response.getStartDate());
            result.put("end_date", response.getEndDate());
            result.put("rates", RateFormatterUtil.formatTimeSeriesRates(response.getRates()));

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch historical rates: " + e.getMessage(), e);
        }
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    private void validateRange(String start, String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);

            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("end_date must be on or after start_date");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected YYYY-MM-DD", e);
        }
    }
}
