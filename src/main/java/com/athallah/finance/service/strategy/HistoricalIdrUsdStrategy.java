package com.athallah.finance.service.strategy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.util.constant.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("historical_idr_usd")
@Slf4j
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    @Autowired
    private FinanceFrankfurterWebClient webClient;

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR to USD data");
        return webClient.getHistoricalIdrUsd();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.historical_idr_usd;
    }
}
