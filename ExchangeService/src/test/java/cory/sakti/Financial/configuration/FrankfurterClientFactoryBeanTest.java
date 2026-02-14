package cory.sakti.Financial.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FrankfurterClientFactoryBeanTest {
    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("RestTemplate must be created via FactoryBean (Constraint B)")
    void shouldCreateRestTemplateViaFactoryBean() {
        // Verify the bean exists
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean must be present");

        // Verify it's created
        Object factoryBean = context.getBean("&FrankfurterClientFactoryBean");
        assertTrue(factoryBean instanceof FactoryBean, "Must be a FactoryBean implementation");
    }
}
