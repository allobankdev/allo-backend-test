package id.allobank.exchangerate.runner;

import id.allobank.exchangerate.store.InMemoryDataStore;
import id.allobank.exchangerate.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> strategies;
    private final InMemoryDataStore store;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> data = new HashMap<>();

        for (IDRDataFetcher strategy : strategies) {
            data.put(strategy.getType(), strategy.fetch());
        }

        store.setData(data); // load sekali
    }
}
