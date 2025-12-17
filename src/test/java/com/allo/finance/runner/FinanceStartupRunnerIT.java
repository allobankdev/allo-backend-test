package com.allo.finance.runner;

import com.allo.finance.service.FinanceDataService;
import com.allo.finance.service.InMemoryStore;
import com.allo.finance.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FinanceStartupRunnerIT {

    @Test
    void shouldLoadAllDataOnStartup() {
        IDRDataFetcher mockFetcher = new IDRDataFetcher() {
            @Override
            public String resourceType() {
                return "latest_idr_rates";
            }

            @Override
            public Object fetch() {
                return Map.of("test", "data");
            }
        };

        InMemoryStore store = new InMemoryStore();
        FinanceDataService service =
                new FinanceDataService(Map.of("latest_idr_rates", mockFetcher), store);

        FinanceStartupRunner runner = new FinanceStartupRunner(service);
        runner.run(null);

        assertNotNull(store.get("latest_idr_rates"));
    }
}
