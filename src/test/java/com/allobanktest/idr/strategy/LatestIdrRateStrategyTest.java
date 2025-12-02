package com.allobanktest.idr.strategy;

import com.allobanktest.idr.util.SpreadUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRateStrategyTest {

    private static MockWebServer mockWebServer;
    private LatestIdrRatesStrategy strategy;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        strategy = new LatestIdrRatesStrategy(webClient);

        ReflectionTestUtils.setField(strategy, "githubUsername", "brnhrdwnnr");
    }

    @Test
    void fetchData_calculatesUsdBuySpreadCorrectly() {
        // sample /latest?base=IDR JSON (USD: 0.00006)
        String json = """
                {
                  "amount": 1,
                  "base": "IDR",
                  "date": "2025-12-01",
                  "rates": { "USD": 0.00006 }
                }
                """;

        mockWebServer.enqueue(new MockResponse().setBody(json).addHeader("Content-Type", "application/json"));

        Mono<Map<String, Object>> mono = strategy.fetchData();
        Map<String, Object> result = mono.block(); // OK for unit test

        assertNotNull(result);
        assertEquals("IDR", result.get("base"));
        assertEquals("2025-12-01", result.get("date"));

        // rates map
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> rates = (Map<String, BigDecimal>) result.get("rates");
        assertNotNull(rates);
        assertEquals(new BigDecimal("0.00006"), rates.get("USD"));

        Object sfObj = result.get("spreadFactor");
        assertNotNull(sfObj);

        BigDecimal spreadFactor = (sfObj instanceof BigDecimal) ? (BigDecimal) sfObj :
                new BigDecimal(String.valueOf(sfObj));

        // expected spread using SpreadUtil for username brnhrdwnnr
        double spreadDouble = SpreadUtil.computeSpreadFactor("brnhrdwnnr");
        BigDecimal expectedSpread = BigDecimal.valueOf(spreadDouble);

        assertEquals(0, expectedSpread.compareTo(spreadFactor));

        // USD_BuySpread_IDR computed via BigDecimal arithmetic in strategy (scale 6)
        Object buyObj = result.get("USD_BuySpread_IDR");
        assertNotNull(buyObj);
        assertInstanceOf(BigDecimal.class, buyObj);

        BigDecimal buyRate = (BigDecimal) buyObj;

        // compute expected with same math as strategy
        BigDecimal usdRate = rates.get("USD");
        MathContext mc = new MathContext(20, RoundingMode.HALF_EVEN);
        BigDecimal inv = BigDecimal.ONE.divide(usdRate, mc);
        BigDecimal multiplier = BigDecimal.ONE.add(expectedSpread);
        BigDecimal expected = inv.multiply(multiplier, mc).setScale(6, RoundingMode.HALF_EVEN);

        assertEquals(0, expected.compareTo(buyRate));
    }
}
