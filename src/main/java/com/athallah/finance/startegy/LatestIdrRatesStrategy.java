package com.athallah.finance.startegy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.util.constant.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("latest_idr_rates")
@Slf4j
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    @Autowired
    private FinanceFrankfurterWebClient webClient;

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates");
        return webClient.getLatestIdrRates();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.latest_idr_rates;
    }
}
