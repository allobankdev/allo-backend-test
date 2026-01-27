package allobankdev.test.finance.starup;


import allobankdev.test.finance.registry.StrategyRegistry;
import allobankdev.test.finance.store.FinanceDataStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceStartupRunner implements ApplicationRunner {

    private final StrategyRegistry registry;
    private final FinanceDataStore store;

    public FinanceStartupRunner(
            StrategyRegistry registry,
            FinanceDataStore store
    ) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<String, Object> loaded = new HashMap<>();

        for (String type : List.of(
                "latest_idr_rates",
                "historical_idr_usd",
                "supported_currencies")) {

            loaded.put(type, registry.get(type).fetch());
        }

        store.load(loaded);
    }
}

