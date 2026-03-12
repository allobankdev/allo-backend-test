package com.musfiul.idrrateaggregator.service.resolver;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;

public interface FetchResolver {
    IDRDataFetcher resolve(ResourceType type);
}
