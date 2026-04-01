package com.self.bs.source.component;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import com.self.bs.source.config.ExchangeRateProperties;

@Component
public class CacheManagerFactoryBean implements FactoryBean<ConcurrentMapCacheManager>{

    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Override
    public ConcurrentMapCacheManager getObject() {
        return new ConcurrentMapCacheManager(exchangeRateProperties.getCacheName());
    }

    @Override
    public Class<?> getObjectType() {
        return ConcurrentMapCacheManager.class;
    }

}
