package com.allobank.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "finance.preload.enabled=false")
class AlloBankTestApplicationTests {

    @Test
    void contextLoads() {
    }
}
