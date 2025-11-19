package com.finance.allobackend.runner;

import com.finance.allobackend.strategy.FinanceStrategy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Component
public class RetrieveDataRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(RetrieveDataRunner.class);

    private final RestTemplate restTemplate;
    private final List<FinanceStrategy> strategies;

    public RetrieveDataRunner(RestTemplate restTemplate, List<FinanceStrategy> strategies) {
        this.restTemplate = restTemplate;
        this.strategies = strategies;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting Data process for {} service...", strategies.size());

        for (FinanceStrategy service : strategies) {
            try {
                logger.info("Fetching data for service: {}", service.getResourceType());
                service.getOrRefreshData(restTemplate);
            } catch (Exception e) {
                logger.error("Failed to load data for {}", service.getResourceType(), e);
            }
        }
        logger.info("Get Data Complete.");
    }
}
