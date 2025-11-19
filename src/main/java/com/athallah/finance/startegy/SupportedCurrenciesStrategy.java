package com.athallah.finance.startegy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.util.constant.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("supported_currencies")
@Slf4j
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    @Autowired
    private FinanceFrankfurterWebClient webClient;

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies");
        return webClient.getSupportedCurrencies();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.supported_currencies;
    }
}
