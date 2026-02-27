package com.amri.apiintegration.application.runner;

import com.amri.apiintegration.application.cache.FinanceDataInMemoryStore;
import com.amri.apiintegration.application.strategy.IDRDataFetcher;
import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FinanceDataLoaderRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> dataFetchers;
    private final FinanceDataInMemoryStore inMemoryStore;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, FinanceResourceResultDto> loadedData = dataFetchers.values()
                .stream()
                .collect(Collectors.toUnmodifiableMap(IDRDataFetcher::resourceType, IDRDataFetcher::fetch));

        inMemoryStore.initialize(loadedData);
    }
}
