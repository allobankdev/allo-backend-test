package com.allo.idr.service;

import com.allo.idr.enums.ResourceType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FetcherStrategyRegistry {
    private final Map<ResourceType, IDRDataFetcher> register = new HashMap<>();

    public FetcherStrategyRegistry(List<IDRDataFetcher> strategy){
        for (IDRDataFetcher idrFeth : strategy){
            register.put(idrFeth.getType(), idrFeth);
        }
    }

    public Optional<IDRDataFetcher> getStrategy(ResourceType type){
        return Optional.ofNullable(register.get(type));
    }

    public Set<ResourceType> getAllResourcesType(){
        return Collections.unmodifiableSet(register.keySet());
    }
}
