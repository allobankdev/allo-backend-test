package com.finance.allobackend.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.finance.allobackend.strategy.impl.HistoryIdrToUsdStrategyImpl;
import com.finance.allobackend.strategy.impl.SupportedCurrenciesStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class SupportedCurrenciesStrategyTest {
    private SupportedCurrenciesStrategyImpl strategy;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        strategy = new SupportedCurrenciesStrategyImpl();
    }

    @Test
    void testGetOrRefreshData_GetCorrectly(){
        String jsonResp  = "{\n" +
                "  \"AUD\": \"Australian Dollar\",\n" +
                "  \"BGN\": \"Bulgarian Lev\",\n" +
                "  \"BRL\": \"Brazilian Real\",\n" +
                "  \"CAD\": \"Canadian Dollar\",\n" +
                "  \"CHF\": \"Swiss Franc\",\n" +
                "  \"CNY\": \"Chinese Renminbi Yuan\",\n" +
                "  \"CZK\": \"Czech Koruna\",\n" +
                "  \"DKK\": \"Danish Krone\",\n" +
                "  \"EUR\": \"Euro\",\n" +
                "  \"GBP\": \"British Pound\",\n" +
                "  \"HKD\": \"Hong Kong Dollar\",\n" +
                "  \"HUF\": \"Hungarian Forint\",\n" +
                "  \"IDR\": \"Indonesian Rupiah\",\n" +
                "  \"ILS\": \"Israeli New Sheqel\",\n" +
                "  \"INR\": \"Indian Rupee\",\n" +
                "  \"ISK\": \"Icelandic Króna\",\n" +
                "  \"JPY\": \"Japanese Yen\",\n" +
                "  \"KRW\": \"South Korean Won\",\n" +
                "  \"MXN\": \"Mexican Peso\",\n" +
                "  \"MYR\": \"Malaysian Ringgit\",\n" +
                "  \"NOK\": \"Norwegian Krone\",\n" +
                "  \"NZD\": \"New Zealand Dollar\",\n" +
                "  \"PHP\": \"Philippine Peso\",\n" +
                "  \"PLN\": \"Polish Złoty\",\n" +
                "  \"RON\": \"Romanian Leu\",\n" +
                "  \"SEK\": \"Swedish Krona\",\n" +
                "  \"SGD\": \"Singapore Dollar\",\n" +
                "  \"THB\": \"Thai Baht\",\n" +
                "  \"TRY\": \"Turkish Lira\",\n" +
                "  \"USD\": \"United States Dollar\",\n" +
                "  \"ZAR\": \"South African Rand\"\n" +
                "}";

        mockServer.expect(requestTo("/currencies"))
                .andRespond(withSuccess(jsonResp, MediaType.APPLICATION_JSON));
        strategy.getOrRefreshData(restTemplate);
        mockServer.verify();

        JsonNode cachedData = (JsonNode) strategy.getCacheData();
        assertNotNull(cachedData);
    }
    @Test
    void testGetResourceType_ReturnsCorrectIdentifier() {
        assertEquals("supportedCurrencies", strategy.getResourceType());
    }
}
