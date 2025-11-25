package com.example.financedata.integration;

import com.example.financedata.store.ImmutableFinanceStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StartupDataLoaderIntegrationTest {

    @Autowired
    ImmutableFinanceStore store;

    @Test
    public void testDataLoadedOnStartup() {
        assertTrue(store.isLoaded(), "Store should be loaded at startup");
        assertNotNull(store.get("latest_idr_rates"));
        assertNotNull(store.get("historical_idr_usd"));
        assertNotNull(store.get("supported_currencies"));
    }
}
In CI you can mock Frankfurter endpoints using WireMock or by injecting a test-specific FrankfurterProperties.baseUrl pointing to a WireMock server. For brevity, I show the structure and approach.

README.md (essential parts)
markdown
Copy code
# IDR Rate Aggregator (Spring Boot)

A Spring Boot application exposing a single polymorphic endpoint:

`GET /api/finance/data/{resourceType}`

Supported `{resourceType}` values:
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

## Features
- Strategy Pattern for each resource
- WebClient created by a custom FactoryBean
- Startup ApplicationRunner loads all resources exactly once into an immutable thread-safe in-memory store
- `latest_idr_rates` contains `USD_BuySpread_IDR` computed using a personalized spread factor

## Personalization
- GitHub username used: `aswindew`
- Spread Factor: `0.00866` (derived from sum of Unicode values of the username)

## Setup & Run

Requirements: Java 17+, Maven or Gradle, internet access (to `api.frankfurter.app`).