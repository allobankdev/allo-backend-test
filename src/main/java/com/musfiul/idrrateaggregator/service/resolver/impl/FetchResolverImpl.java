package com.musfiul.idrrateaggregator.service.resolver.impl;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;
import com.musfiul.idrrateaggregator.service.resolver.FetchResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FetchResolverImpl implements FetchResolver {
    private final Map<ResourceType, IDRDataFetcher> fetchers;

    public FetchResolverImpl(List<IDRDataFetcher> fetcherList) {

        this.fetchers = fetcherList.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getType,
                        f -> f
                ));
    }

    @Override
    public IDRDataFetcher resolve(ResourceType type) {
        return fetchers.get(type);
    }
}
