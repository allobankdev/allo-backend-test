package com.example.finance.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.finance.service.DataInitializationService;

import org.springframework.context.annotation.Profile;

@Component
@Profile("!test")
public class DataLoaderRunner implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);
	
    private final DataInitializationService service;

    public DataLoaderRunner(DataInitializationService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
    		log.info("Starting data loading...");
        
        service.loadAllData();
        
        log.info("Data successfully loaded into memory");
    }
}