package com.allobank.allobackend;

import com.allobank.allobackend.core.fetcher.impl.SupportedCurrencyStrategy;
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
public class SupportCurrencyStrategyTest {

    @Autowired
    private SupportedCurrencyStrategy strategy;

    private MockRestServiceServer mockServer;

    private RestClient restClient;

    @BeforeEach
    void setUp(){
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
    }

    @Test
    void testSupportedCurrencies(){
        String mockApiJson = "{\"symbols\": {\"USD\": \"United States Dollar\", \"IDR\": \"Indonesian Rupiah\"}}";
        mockServer.expect(requestTo(containsString("/symbols")))
                .andRespond(withSuccess(mockApiJson, MediaType.APPLICATION_JSON));

        JSONObject result = strategy.fetchData(restClient);

        assertTrue(result.has("datas"));
        JSONObject symbols = result.getJSONObject("datas").getJSONObject("symbols");
        assertTrue(symbols.has("USD"));
        mockServer.verify();
    }
}
