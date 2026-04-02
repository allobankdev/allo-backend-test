
package com.allo.finance.runner;

import com.allo.finance.store.DataStore;
import com.allo.finance.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final DataStore store;

    public DataLoader(List<IDRDataFetcher> fetchers, DataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    public void run(ApplicationArguments args){
        Map<String,Object> temp = new HashMap<>();
        fetchers.forEach(f -> temp.put(f.getType(), f.fetch()));
        store.setAll(temp);
    }
}
