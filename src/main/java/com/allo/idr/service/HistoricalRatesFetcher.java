package com.allo.idr.service;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.enums.ResourceType;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.HistoricalRatesResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HistoricalRatesFetcher implements IDRDataFetcher{
    private final ExternalApiClient resT;

    public HistoricalRatesFetcher(ExternalApiClient resT) {
        this.resT = resT;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.HISTORICAL_IDR_USD;
    }

    @Override
    public List<HistoricalRatesResponse> fetcData() {
        try {
            Map<String, Object> res = resT.getHistoricalIdrToUsd("2024-01-01", "2024-01-05");
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Double>> rates = (Map<String, Map<String, Double>>) res.get("rates");

            List<HistoricalRatesResponse> cek = new ArrayList<>();
            if (rates != null) {
                for (Map.Entry<String, Map<String, Double>> getHis : rates.entrySet()) {
                    Double usd = getHis.getValue().get("USD");
                    cek.add(new HistoricalRatesResponse(getHis.getKey(), usd));
                }
            }
            return cek;
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Faild get historical rates", e);
        }
    }
}
