package com.allo.backendtest.config;

import com.allo.backendtest.service.IdrDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppInitializer implements ApplicationRunner {

    private final Map<String, IdrDataFetcher> idrDataFetcherMap;

    public AppInitializer(Map<String, IdrDataFetcher> idrDataFetcherMap) {
        this.idrDataFetcherMap = idrDataFetcherMap;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (IdrDataFetcher idrDataFetcher : idrDataFetcherMap.values()) {
            idrDataFetcher.update();
        }
    }
}
