package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.client.FrankfurterClient;
import com.thasya.frankfurter.config.GithubProperties;
import com.thasya.frankfurter.dto.FrankfurterLatestResponse;
import com.thasya.frankfurter.dto.LatestIdrRateDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "latest_idr_rates";

    private final FrankfurterClient client;
    private final GithubProperties githubProperties;

    public LatestIdrRatesFetcher(FrankfurterClient client, GithubProperties githubProperties) {
        this.client = client;
        this.githubProperties = githubProperties;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<?> fetchData() {
        FrankfurterLatestResponse latest = client.getLatestIdrRates();
        if (latest == null) {
            return Collections.emptyList();
        }
        Map<String, Double> rates = latest.getRates();

        Double usdRate = rates != null ? rates.get("USD") : null;
        if (usdRate == null) {
            throw new IllegalStateException("USD rate is missing from latest rates");
        }

        String username = githubProperties.getUsername();
        int sum = username != null ? username.chars().sum() : 0;
        double spreadFactor = (sum % 1000) / 100000.0; // 0.00000 - 0.00999

        double usdBuySpreadIdr = (1 / usdRate) * (1 + spreadFactor);

        LatestIdrRateDto dto = new LatestIdrRateDto(
                latest.getDate(),
                latest.getBase(),
                usdRate,
                usdBuySpreadIdr
        );

        return Collections.singletonList(dto);
    }
}
