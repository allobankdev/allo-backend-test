package com.allobank.finance.runner;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.allobank.finance.store.IDRDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;

/**
 * Loads all IDR finance data on application startup.
 * Fetches data from the three Frankfurter API resources and stores them in memory.
 */
@Component
public class IDRDataInitializationRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(IDRDataInitializationRunner.class);

    private final IDRDataStore dataStore;
    private final ApplicationContext applicationContext;

    public IDRDataInitializationRunner(IDRDataStore dataStore, ApplicationContext applicationContext) {
        this.dataStore = dataStore;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting IDR data initialization...");

        // Get all beans that implement IDRDataFetcher
        Map<String, IDRDataFetcher> strategies = applicationContext.getBeansOfType(IDRDataFetcher.class);

        for (String beanName : strategies.keySet()) {
            IDRDataFetcher strategy = strategies.get(beanName);
            try {
                logger.info("Fetching data for resource type: {}", beanName);
                Object data = strategy.fetchData();
                dataStore.store(beanName, data);
                logger.info("Successfully loaded data for resource type: {}", beanName);
            } catch (Exception e) {
                logger.error("Failed to load data for resource type: {}", beanName, e);
                throw new RuntimeException("Failed to initialize IDR data for: " + beanName, e);
            }
        }

        logger.info("IDR data initialization completed successfully");
    }
}
