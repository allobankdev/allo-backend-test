package com.finance.service.intializers;

import com.finance.service.AggregatedDataStore;
import com.finance.service.fetchers.DataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataInitializer {

    public DataInitializer(Map<String, DataFetcher> fetchers,
                           AggregatedDataStore store) {

        Map<String, List<Map<String, Object>>> initial =
                fetchers.values()
                        .stream()
                        .collect(Collectors.toMap(
                                DataFetcher::resourceType,
                                DataFetcher::fetch
                        ));

        store.initialize(initial);
    }
}

