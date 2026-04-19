package com.allobank.allobackend;

import com.allobank.allobackend.core.fetcher.impl.LatestIdrStrategy;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class LatestIdrStrategyTest {

    @Autowired
    private LatestIdrStrategy strategy;

    private MockRestServiceServer mockServer;

    private  RestClient restClient;

    @BeforeEach
    void setUp(){
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
    }

    @Test
    void testFetchAndCalculate(){
        String mockApiJson = "{\"rates\": {\"USD\": 0.0000625}}";

        mockServer.expect(requestTo(containsString("/latest")))
                .andRespond(withSuccess(mockApiJson, MediaType.APPLICATION_JSON));

        JSONObject result = strategy.fetchData(restClient);
        JSONObject datas = result.getJSONObject("datas");

        assertTrue(datas.has("USD_BuySpread_IDR"));
        assertTrue(datas.getDouble("USD_BuySpread_IDR") >= 16000);
        mockServer.verify();


    }
}
