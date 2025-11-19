package com.finance.service;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.RateResponse;
import com.finance.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(FrankfurterClient client,
                                 @Value("${candidate.github-username:}") String githubUsername) {
        this.client = client;
        String githubUsernameLowerCase = githubUsername.toLowerCase(Locale.ROOT);
        this.spreadFactor = SpreadCalculator.computeSpread(githubUsernameLowerCase);
    }

    @Override
    public String resourceType() { return "latest_idr_rates"; }

    @Override
    public List<Map<String, Object>> fetch() {

        RateResponse dto = client.getLatestRates(AppConstant.IDR_BASE)
                .blockOptional()
                .orElseThrow(() -> new ExternalServiceException(AppConstant.NO_RESPONSE_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR));

        Map<String, Double> rates = dto.getRates();
        System.out.println("rates : "+rates);
        if (rates == null || rates.isEmpty()) {
            throw new ExternalServiceException(AppConstant.EMPTY_RATES_RESPONSE_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Double usdRate = rates.get(AppConstant.USD_BASE);
        if (usdRate == null) {
            throw new ExternalServiceException(AppConstant.USD_RATE_RESPONSE_MISSING_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);

        Map<String, Object> out = Map.of(
                "base", dto.getBase(),
                "date", dto.getDate(),
                "rates", Collections.unmodifiableMap(rates),
                "USD_BuySpread_IDR", usdBuySpreadIdr
        );

        return List.of(out);
    }

}
