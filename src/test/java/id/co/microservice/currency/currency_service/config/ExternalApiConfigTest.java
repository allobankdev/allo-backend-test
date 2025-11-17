package id.co.microservice.currency.currency_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "external.api.base-url=http://test-api.com",
        "external.api.connect-timeout=5000",
        "external.api.read-timeout=10000"
})
class ExternalApiConfigTest {

    @Autowired
    private ExternalApiConfig externalApiConfig;

    @Test
    void testPropertiesAreBoundCorrectly() {
        assertEquals("http://test-api.com", externalApiConfig.getBaseUrl());
        assertEquals(5000, externalApiConfig.getConnectTimeout());
        assertEquals(10000, externalApiConfig.getReadTimeout());
    }

}