package com.allobank.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.allobank.finance.AlloBankTestApplication;

@SpringBootTest(
        classes = AlloBankTestApplication.class,
        properties = "finance.preload.enabled=false")
class AlloBankTestApplicationTests {

    @Test
    void contextLoads() {
    }
}
