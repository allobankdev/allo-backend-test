package com.allobank.assignment.bootstrap;


import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.LatestRatesAggregation;
import com.allobank.assignment.service.FinanceDataCache;
import com.allobank.assignment.service.FinanceDataService;
import com.allobank.assignment.support.FrankfurterClientStubConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(FrankfurterClientStubConfig.class)
public class FinanceDataInitializerIntegrationTest {

    @Autowired
    private FinanceDataService financeDataService;

    @Autowired
    private FinanceDataCache cache;

    @Test
    void shouldServeCachedDataLoadedAtStartup() {
        List<FinanceDataResponse> latest = financeDataService.getFinanceData("latest_idr_rates");
        assertThat(latest).hasSize(1);
        assertThat(latest.get(0).resourceType()).isEqualTo("latest_idr_rates");
        assertThat(latest.get(0).payload()).isInstanceOf(LatestRatesAggregation.class);

        List<FinanceDataResponse> historical = financeDataService.getFinanceData("historical_idr_usd");
        assertThat(historical).hasSize(1);

        List<FinanceDataResponse> currencies = financeDataService.getFinanceData("supported_currencies");
        assertThat(currencies).hasSize(1);

        assertThat(cache.get(com.allobank.assignment.model.ResourceType.SUPPORTED_CURRENCIES).payload())
                .isInstanceOf(Map.class);
    }
}
