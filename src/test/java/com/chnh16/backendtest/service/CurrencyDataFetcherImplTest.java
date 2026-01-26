package com.chnh16.backendtest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CurrencyDataFetcherImplTest {

    @Autowired
    CurrencyDataFetcherImpl service;

    @Autowired
    InMemoryStoreService storeService;

    @Test
    @DisplayName("supported_currencies")
    public void fetch() {
        service.fetch();
        assertNotNull(storeService.get("supported_currencies"));
    }

}
