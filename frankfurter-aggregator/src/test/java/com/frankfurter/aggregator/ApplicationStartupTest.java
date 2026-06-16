package com.frankfurter.aggregator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
    "app.github.username=andityadimas",
    "app.api.base-url=https://api.frankfurter.app"
})
class ApplicationStartupTest {
    
    @Test
    void contextLoads() {
        // Test passes if Spring context loads
        assertTrue(true);
    }
}