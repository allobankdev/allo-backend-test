package com.allobank.exercise.api.integration;

import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import com.allobank.exercise.api.integration.impl.FrankfurterClientImpl;
import com.allobank.exercise.api.properties.FrankfurterApiProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FrankfurterClientTest {
    static MockWebServer mockServer;
    FrankfurterClientImpl client;

    @BeforeAll
    static void setupServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setupClient() {

        FrankfurterApiProperties props = new FrankfurterApiProperties();
        String baseUrl = mockServer.url("/").toString();

        props.setBaseUrl(baseUrl.substring(0, baseUrl.length() - 1));
        props.setLatestIdrPath("/latest?base=IDR");
        props.setCurrencyPath("/currencies");

        WebClient webClient = WebClient.builder().baseUrl(props.getBaseUrl()).build();
        client = new FrankfurterClientImpl(webClient, props);
    }

    @Test
    void testGetLatestRates_success() throws Exception {

        mockServer.enqueue(new MockResponse()
                .setBody("{\"amount\":1.0,\"base\":\"IDR\",\"date\":\"2025-11-18\",\"rates\":{\"AUD\":9.2e-05,\"BGN\":0.0001,\"BRL\":0.00032,\"CAD\":8.4e-05,\"CHF\":4.8e-05,\"CNY\":0.00042,\"CZK\":0.00125,\"DKK\":0.00038,\"EUR\":5.1e-05,\"GBP\":4.5e-05,\"HKD\":0.00046,\"HUF\":0.01982,\"ILS\":0.0002,\"INR\":0.00529,\"ISK\":0.00758,\"JPY\":0.00927,\"KRW\":0.08731,\"MXN\":0.0011,\"MYR\":0.00025,\"NOK\":0.0006,\"NZD\":0.00011,\"PHP\":0.00351,\"PLN\":0.00022,\"RON\":0.00026,\"SEK\":0.00057,\"SGD\":7.8e-05,\"THB\":0.00193,\"TRY\":0.00253,\"USD\":6.0e-05,\"ZAR\":0.00103}}")
                .addHeader("Content-Type", "application/json"));

        ExchangeRateResponse response = client.getLatestRates();

        assertNotNull(response);
        assertEquals("IDR", response.getBase());
        assertEquals(BigDecimal.valueOf(0.000060), response.getRates().get("USD"));
    }

    @Test
    void testGetLatestRates_fail_returnsEmpty() {

        mockServer.enqueue(new MockResponse().setResponseCode(500));

        ExchangeRateResponse response = client.getLatestRates();

        assertNotNull(response);
        assertNull(response.getBase());
        assertTrue(response.getRates() == null || response.getRates().isEmpty());
    }

    @Test
    void testGetExchangeHistory_success() {

        mockServer.enqueue(new MockResponse()
                .setBody("{\"amount\":1.0,\"base\":\"IDR\",\"start_date\":\"2023-12-29\",\"end_date\":\"2024-01-05\",\"rates\":{\"2023-12-29\":{\"USD\":6.5e-05},\"2024-01-02\":{\"USD\":6.4e-05},\"2024-01-03\":{\"USD\":6.4e-05},\"2024-01-04\":{\"USD\":6.4e-05},\"2024-01-05\":{\"USD\":6.4e-05}}}")
                .addHeader("Content-Type", "application/json"));

        ExchangeHistoryResponse response =
                client.getExchangeHistory("2024-01-01..2024-01-05", "IDR", "USD");

        assertNotNull(response);
        assertEquals("IDR", response.getBase());
        assertEquals(Double.valueOf(6.4E-5), response.getRates().get("2024-01-05").get("USD"));
    }

    @Test
    void testGetExchangeHistory_fail_returnsEmpty() {

        mockServer.enqueue(new MockResponse().setResponseCode(400));

        ExchangeHistoryResponse response =
                client.getExchangeHistory("2024-01-01..2024-01-05", "IDR", "USD");

        assertNotNull(response);
        assertNull(response.getBase());
    }

    @Test
    void testGetSupportedCurrencies_success() {

        mockServer.enqueue(new MockResponse()
                .setBody("{\"AUD\":\"Australian Dollar\",\"BGN\":\"Bulgarian Lev\",\"BRL\":\"Brazilian Real\",\"CAD\":\"Canadian Dollar\",\"CHF\":\"Swiss Franc\",\"CNY\":\"Chinese Renminbi Yuan\",\"CZK\":\"Czech Koruna\",\"DKK\":\"Danish Krone\",\"EUR\":\"Euro\",\"GBP\":\"British Pound\",\"HKD\":\"Hong Kong Dollar\",\"HUF\":\"Hungarian Forint\",\"IDR\":\"Indonesian Rupiah\",\"ILS\":\"Israeli New Sheqel\",\"INR\":\"Indian Rupee\",\"ISK\":\"Icelandic Króna\",\"JPY\":\"Japanese Yen\",\"KRW\":\"South Korean Won\",\"MXN\":\"Mexican Peso\",\"MYR\":\"Malaysian Ringgit\",\"NOK\":\"Norwegian Krone\",\"NZD\":\"New Zealand Dollar\",\"PHP\":\"Philippine Peso\",\"PLN\":\"Polish Złoty\",\"RON\":\"Romanian Leu\",\"SEK\":\"Swedish Krona\",\"SGD\":\"Singapore Dollar\",\"THB\":\"Thai Baht\",\"TRY\":\"Turkish Lira\",\"USD\":\"United States Dollar\",\"ZAR\":\"South African Rand\"}")
                .addHeader("Content-Type", "application/json"));

        Map<String, String> result = client.getSupportedCurrencies();

        assertEquals(31, result.size());
        assertEquals("United States Dollar", result.get("USD"));
        assertEquals("Indonesian Rupiah", result.get("IDR"));
    }

    @Test
    void testGetSupportedCurrencies_fail_returnEmpty() {

        mockServer.enqueue(new MockResponse().setResponseCode(503));

        Map<String, String> result = client.getSupportedCurrencies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
