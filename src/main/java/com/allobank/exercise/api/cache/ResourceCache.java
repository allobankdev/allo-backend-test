package com.allobank.exercise.api.cache;

import com.allobank.exercise.api.dto.CurrencyInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResourceCache {

    private Map<String, String> currencyCache = new HashMap<>();

    public void initImmutableCache(Map<String, String> currencyCache){

        this.currencyCache = Collections.unmodifiableMap(currencyCache);
    }

    public Map<String, String> getCurrencyCache(){
        return currencyCache;
    }

}
