package id.co.microservice.currency.currency_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SwaggerConfig.class)
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void testCustomOpenAPIBeanCreated() {
        assertNotNull(openAPI, "OpenAPI bean should be created");

        Info info = openAPI.getInfo();
        assertNotNull(info, "Info section should not be null");
        assertEquals("My Spring Boot 3 API", info.getTitle());
        assertEquals("Backend Service for Finance Microservice", info.getDescription());
        assertEquals("1.0.0", info.getVersion());

        assertNotNull(openAPI.getServers(), "Servers list should not be null");
        assertEquals(1, openAPI.getServers().size());
        Server server = openAPI.getServers().get(0);
        assertEquals("http://localhost:9990", server.getUrl());
    }

}