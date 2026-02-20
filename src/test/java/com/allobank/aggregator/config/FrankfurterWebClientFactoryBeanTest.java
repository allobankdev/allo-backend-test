package com.allobank.aggregator.config;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FrankfurterWebClientFactoryBeanTest {

    private MockWebServer server;

    @BeforeEach
    void setup() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void factoryBuildsWebClientWithBaseUrl() {
        String base = server.url("/").toString();
        FrankfurterProperties props = new FrankfurterProperties();
        props.setBaseUrl(base);
        props.setTimeoutMs(1000);

        FrankfurterWebClientFactoryBean factory = new FrankfurterWebClientFactoryBean(props);
        WebClient client = factory.getObject();

        server.enqueue(new MockResponse().setResponseCode(200).setBody("OK"));

        String body = client.get().uri("/hello").retrieve().bodyToMono(String.class).block();
        assertThat(body).isEqualTo("OK");
        assertThat(factory.getObjectType()).isEqualTo(WebClient.class);
        assertThat(factory.isSingleton()).isTrue();
    }
}
