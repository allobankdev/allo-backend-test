package id.allobank.exchangerate.runner;

import id.allobank.exchangerate.store.InMemoryDataStore;
import id.allobank.exchangerate.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> strategies;
    private final InMemoryDataStore store;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> result = new HashMap<>();

        for (IDRDataFetcher strategy : strategies) {
            try {
                log.info("Loading {}", strategy.getType());
                result.put(strategy.getType(), strategy.fetch());
            } catch (Exception e) {
                log.error("Failed at {}", strategy.getType(), e);
                throw e;
            }
        }

        store.setAll(result);
    }
}
