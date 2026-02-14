package cory.sakti.Financial.runner;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import cory.sakti.Financial.strategy.FinancialDataStrategy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class StartupDataRunner implements ApplicationRunner {

    private final List<FinancialDataStrategy> strategies;
    private final InMemoryDataStoreService dataStore;
    private final RestTemplate restTemplate;

    public StartupDataRunner(List<FinancialDataStrategy> strategies,
                             InMemoryDataStoreService dataStore,
                             RestTemplate restTemplate) {
        this.strategies = strategies;
        this.dataStore = dataStore;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
