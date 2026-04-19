package com.allobank.allobackend;

import com.allobank.allobackend.core.domain.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FinanceDataStoreTest {

    @Autowired
    private FinanceDataStore store;

    @Test
    void testInitAfterStartup(){
        assertNotNull(store.get("latest_idr_rates"), "Latest rates should be loaded");
        assertNotNull(store.get("historical_idr_usd"), "Historical data should be loaded");
        assertNotNull(store.get("supported_currencies"), "Historical data should be loaded");


        Map<String, Object> latest = (Map<String, Object>) store.get("latest_idr_rates");
        Map<String, Object> history = (Map<String, Object>) store.get("historical_idr_usd");
        Map<String, Object> support = (Map<String, Object>) store.get("supported_currencies");
        assertTrue(latest.containsKey("datas"));
        assertTrue(history.containsKey("datas"));
        assertTrue(support.containsKey("datas"));

    }

}
