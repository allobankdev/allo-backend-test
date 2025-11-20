package com.allobank.assignment;

import com.allobank.assignment.support.FrankfurterClientStubConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(FrankfurterClientStubConfig.class)
public class IdrRateAggregatorApplicationTests {
    @Test
    void contextLoads() {
    }
}
