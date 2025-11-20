package com.allo.idr.service;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.enums.ResourceType;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.LatestRateResponse;
import com.allo.idr.util.SpreadCalculator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class LatestRatesFetcher implements IDRDataFetcher{
    private final ExternalApiClient client;
    private final SpreadCalculator sprdCalculator;

    public LatestRatesFetcher(ExternalApiClient client, SpreadCalculator sprdCalculator) {
        this.client = client;
        this.sprdCalculator = sprdCalculator;
    }


    @Override
    public ResourceType getType() {
        return ResourceType.LATEST_IDR_RATES;
    }


    @Override
    public List<LatestRateResponse> fetcData() {
        try {
            Map<String, Object> res = client.getLatestBaseIdr();
            LatestRateResponse lateRateRes =  new LatestRateResponse();
            lateRateRes.setBase((String) res.get("base"));
            lateRateRes.setDate((String) res.get("date"));

            @SuppressWarnings("unchecked")
            Map<String, Double> rates = (Map<String, Double>) res.get("rates");

            lateRateRes.setRates(rates);
            Double rateUsd = rates == null ? null : rates.get("USD");

            if (rateUsd == null) {
                throw new ExternalApiException("USD not found in latest rates");
            }

            double spread = sprdCalculator.calculate();
            double usdSpread = ((1.0/rateUsd) * (1.0 + spread));
            lateRateRes.setUsdBuySpreadIdr(usdSpread);

            return Collections.singletonList(lateRateRes);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Failed get latest rates", e);
        }
    }
}
