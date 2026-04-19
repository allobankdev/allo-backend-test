package com.allobank.allobackend;

import com.allobank.allobackend.core.fetcher.impl.HistoricalIdrStrategy;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class HistoricalIdrStrategyTest {
    @Autowired
    private HistoricalIdrStrategy strategy;

    private MockRestServiceServer mockServer;

    private RestClient restClient;

    @BeforeEach
    void setUp(){
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
    }

    @Test
    void testHistoricalFetching(){
        String mockApiJson = "{\"2026-04-18\": {\"USD\": 0.000058}}";

        mockServer.expect(requestTo(containsString("..")))
                .andRespond(withSuccess(mockApiJson, MediaType.APPLICATION_JSON));

        JSONObject result = strategy.fetchData(restClient);
        assertNotNull(result.get("datas"));
        JSONObject history = result.getJSONObject("datas");
        assertTrue(history.keySet().iterator().next().matches("\\d{4}-\\d{2}-\\d{2}"));
        mockServer.verify();

    }

}
