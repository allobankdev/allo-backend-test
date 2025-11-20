package com.finance.service.fetchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.internal.LatestIdrRatesResponse;
import com.finance.dto.external.RateResponse;
import com.finance.exception.ExternalServiceException;
import com.finance.service.util.SpreadCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements DataFetcher {

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
        if (rates == null || rates.isEmpty()) {
            throw new ExternalServiceException(AppConstant.EMPTY_RATES_RESPONSE_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Double usdRate = rates.get(AppConstant.USD_BASE);
        if (usdRate == null) {
            throw new ExternalServiceException(AppConstant.USD_RATE_RESPONSE_MISSING_FROM_API_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);

        LatestIdrRatesResponse out = LatestIdrRatesResponse.builder()
                .base(dto.getBase())
                .date(dto.getDate())
                .rate(Collections.unmodifiableMap(rates))
                .USD_BuySpread_IDR(usdBuySpreadIdr)
                .build();

        // Convert POJO → Map
        Map<String, Object> map = new ObjectMapper()
                .convertValue(out, new TypeReference<Map<String, Object>>() {});

        return List.of(map);
    }


}
