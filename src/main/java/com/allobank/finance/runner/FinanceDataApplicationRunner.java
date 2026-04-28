package com.allobank.finance.runner;

import com.allobank.finance.service.FinanceDataService;
import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class FinanceDataApplicationRunner implements ApplicationRunner {

    private static final Logger log = Logger.getLogger(FinanceDataApplicationRunner.class.getName());

    private final FinanceDataService financeDataService;

    public FinanceDataApplicationRunner(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[FinanceDataApplicationRunner] Mulai fetch semua resource...");

        Map<String, IDRDataFetcher> strategies = financeDataService.getStrategyMap();
        InMemoryFinanceStore store = financeDataService.getStore();

        int successCount = 0;
        int failureCount = 0;

        for (Map.Entry<String, IDRDataFetcher> entry : strategies.entrySet()) {
            String resourceType = entry.getKey();
            IDRDataFetcher fetcher = entry.getValue();

            try {
                log.info("[FinanceDataApplicationRunner] Fetching: " + resourceType);
                List<Map<String, Object>> data = fetcher.fetch();
                store.put(resourceType, data);
                successCount++;
                log.info(String.format("[FinanceDataApplicationRunner] Berhasil load '%s' (%d records)",
                        resourceType, data.size()));
            } catch (Exception ex) {
                failureCount++;
                log.severe(String.format("[FinanceDataApplicationRunner] Gagal load '%s': %s",
                        resourceType, ex.getMessage()));
            }
        }

        store.seal();

        log.info(String.format(
                "[FinanceDataApplicationRunner] Selesai. Berhasil: %d, Gagal: %d. Store sealed.",
                successCount, failureCount));
    }
}
