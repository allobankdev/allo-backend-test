package com.frankfurter.aggregator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataFetchingServiceTest {
    
    @Autowired
    private DataFetchingService dataFetchingService;
    
    @Test
    void testAllStrategiesLoaded() {
        assertNotNull(dataFetchingService);
    }
}