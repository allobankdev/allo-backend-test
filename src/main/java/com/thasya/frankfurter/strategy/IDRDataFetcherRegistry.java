package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.exception.UnknownResourceTypeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcher> fetcherByType;

    public IDRDataFetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetcherByType = fetchers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher getFetcher(String resourceType) {
        IDRDataFetcher fetcher = fetcherByType.get(resourceType);
        if (fetcher == null) {
            throw new UnknownResourceTypeException(resourceType);
        }
        return fetcher;
    }

    public Set<String> getSupportedTypes() {
        return fetcherByType.keySet();
    }
}
