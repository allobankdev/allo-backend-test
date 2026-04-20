package id.allobank.exchangerate.runner;

import id.allobank.exchangerate.store.InMemoryDataStore;
import id.allobank.exchangerate.service.StrategyRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);
    private final StrategyRegistry strategyRegistry;
    private final InMemoryDataStore store;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> result = new HashMap<>();

        for (String type : strategyRegistry.getSupportedTypes()) {
            try {
                log.info("Loading {}", type);
                result.put(type, strategyRegistry.get(type).fetch());
            } catch (Exception e) {
                log.error("Failed at {}", type, e);
                throw e;
            }
        }

        store.setAll(result);
    }
}
