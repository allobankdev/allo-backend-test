package com.project.alloBank.service;

import com.project.alloBank.repository.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);
    private final List<IDRDataFetcher> fetchers;
    private final DataStore dataStore;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> results = new HashMap<>();
        for (IDRDataFetcher f : fetchers) {
            try {
                log.info("Fetching data: {}", f.getResourceType());
                Object data = f.fetchData();
                results.put(f.getResourceType(), data);
                log.info("Fetched data {} sukses", f.getResourceType());
            } catch (Exception e) {
                log.error("fail to fech {} : {}", f.getResourceType(), e.getMessage(), e);
                results.put(f.getResourceType(), null);
            }
        }
        dataStore.setAll(results);
        log.info("All data success loaded");
    }
}
