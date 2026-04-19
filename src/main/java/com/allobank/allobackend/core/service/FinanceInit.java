package com.allobank.allobackend.core.service;

import com.allobank.allobackend.core.domain.FinanceDataStore;
import com.allobank.allobackend.core.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FinanceInit implements ApplicationRunner {
    private final List<IDRDataFetcher> straegies;
    private final RestClient restClient;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args){
        log.info("Call frankfurter api ...");

        straegies.forEach(strategy -> {
            try{
                JSONObject data = strategy.fetchData(restClient);
                store.save(strategy.getResourceType() , data);
                log.info("Success load: {}" , strategy.getResourceType());
            }catch (Exception e){
                log.error("Failed load: {}" ,  strategy.getResourceType());
            }

        });

        store.lock();
    }
}
