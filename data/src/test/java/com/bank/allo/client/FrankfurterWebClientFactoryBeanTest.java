package com.bank.allo.client;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.*;

class FrankfurterWebClientFactoryBeanTest {

    @Test
    void testFactoryCreatesWebClient() throws Exception {
        FrankfurterWebClientFactoryBean factory =
                new FrankfurterWebClientFactoryBean("https://api.frankfurter.app");

        WebClient client = factory.getObject();

        assertNotNull(client);
    }
}
