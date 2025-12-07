package id.tisnanda.allobank.allo_bank_backend_test.runner;

import id.tisnanda.allobank.allo_bank_backend_test.service.IDRFinanceService;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IDRDataLoader implements ApplicationRunner {

    private static final Logger log = Logger.getLogger(IDRDataLoader.class);

    private final IDRFinanceService financeService;
    private final Map<String, IDRDataFetcher> dataFetchers;


    @Override
    public void run(ApplicationArguments args) {
        if (dataFetchers == null) {
            log.warn("No data fetchers configured");
            return;
        }

        dataFetchers.forEach((resourceType, fetcher) -> {
            try {
                List<Map<String, Object>> data = fetcher.fetchData();
                financeService.setData(resourceType, data);
            } catch (Exception e) {
                log.errorf(e, "Failed to fetch data for resource: %s", resourceType);
                throw new IllegalStateException("Startup aborted: cannot load required data");
            }
        });

        log.info("Startup data loaded successfully!");
    }
}
