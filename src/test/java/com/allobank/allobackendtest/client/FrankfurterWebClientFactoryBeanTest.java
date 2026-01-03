package com.allobank.allobackendtest.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
class FrankfurterWebClientFactoryBeanTest {

    static MockWebServer mockWebServer;

    @Autowired
    private WebClient webClient;

    @BeforeAll
    static void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add( "frankfurter.base-url",() -> mockWebServer.url("/").toString());
        registry.add("frankfurter.timeout-ms",() -> 2000);
    }

    @Test
    void shouldCreateWebClientFromFactoryBean() {
        assertThat(webClient).isNotNull();
    }

    @Test
    void shouldCallExternalApiUsingConfiguredBaseUrl() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("{\"USD\":\"United States Dollar\"}")
                        .addHeader("Content-Type", "application/json")
        );
        String response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertThat(response).contains("United States Dollar");
    }

}
