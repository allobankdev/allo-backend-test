package com.allobankdev.exchangrate.startup;

import com.allobankdev.exchangrate.service.factory.DataFetcherFactory;
import com.allobankdev.exchangrate.service.store.DataStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final DataFetcherFactory factory;
    private final DataStore store;

    public DataLoader(DataFetcherFactory factory, DataStore store) {
        this.factory = factory;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        factory.getAllTypes().forEach(type -> {
            Object data = factory.get(type).fetch();
            store.save(type, data);
        });
    }
}
