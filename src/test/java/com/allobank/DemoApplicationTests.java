package com.allobank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        // Test basic context loading
        assertNotNull(restTemplate);
    }

    @Test
    void getData_whenCacheReady_returnsData() {
        // Test endpoint
        String response = restTemplate.getForObject(
            "http://localhost:" + port + "/api/finance/data/health", 
            String.class
        );
        assertNotNull(response);
        assertTrue(response.contains("ready"));
    }
}