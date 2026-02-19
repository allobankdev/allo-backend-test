package co.id.allobank.finance.config;

import co.id.allobank.finance.service.InMemoryFinanceStore;
import co.id.allobank.finance.config.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FinanceDataStartupRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategyMap;
    private final InMemoryFinanceStore store;

    @Override
    public void run(ApplicationArguments args){
        strategyMap.forEach((k,v)->{
            Object data = v.fetchData();
            store.put(k,data);
        });

        store.makeImmutable();
    }
}
