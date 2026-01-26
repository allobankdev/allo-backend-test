package com.chnh16.backendtest;

import com.chnh16.backendtest.controller.MainController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

    private final MainController controller;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Executing application runner");
        controller.getByResourceType("latest_idr_rates");
        controller.getByResourceType("historical_idr_usd");
        controller.getByResourceType("supported_currencies");
    }
}
