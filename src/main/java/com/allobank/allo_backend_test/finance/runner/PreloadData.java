package com.allobank.allo_backend_test.finance.runner;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreloadData implements ApplicationRunner {

    private final DataSourceClient client;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("PreloadData Is Running");
        log.info("{}", client.getLatestRates("IDR"));
        log.info("{}", client.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD"));
        log.info("{}", client.getCurrencies());
    }
}
