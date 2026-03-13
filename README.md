# Allo Backend Test – Finance Data Service

This project is a Spring Boot application that integrates with the Frankfurter Exchange Rate API and exposes a unified internal endpoint for financial data.

The implementation satisfies the technical requirements defined in the Allo Backend Test repository.

The system demonstrates the use of:

- Spring FactoryBean
- Strategy Pattern
- Dynamic endpoint routing
- Startup data loading
- Custom financial spread calculation
- Unified API response format

---

# Architecture Overview

The application uses a layered architecture:

Controller
↓
Service
↓
Strategy Factory
↓
Strategy Pattern
↓
In-Memory Data Store

External API data is fetched **only once during application startup** and then cached in memory.

Subsequent requests do not call the external API again.

---

# External API Integration

The application consumes data from:

Frankfurter API  
https://www.frankfurter.app

The following endpoints are used:

Latest Rates  
/ latest?base=IDR

Historical IDR to USD  
/2024-01-01..2024-12-31?from=IDR&to=USD

Supported Currencies  
/ currencies

These endpoints are called through a custom API client.

---

# Constraint A — Startup Data Loading

All external API data is fetched during application startup.

Implementation uses:

ApplicationRunner

Class:
DataLoader

When the application starts, the following resources are fetched:

• Latest IDR Rates  
• Historical IDR → USD rates  
• Supported currencies

These are stored in a singleton in-memory storage:

DataStore

This ensures:

• API is called only once  
• Faster response times  
• No repeated external API calls

---

# Constraint B — Client FactoryBean

The Frankfurter API client is created using a custom Spring FactoryBean.

Class:
FrankfurterClientFactoryBean

Responsibilities:

• Create RestTemplate instance  
• Inject API configuration  
• Provide FrankfurterClient instance  
• Ensure singleton lifecycle

This satisfies the test constraint that the API client must NOT be defined as a simple @Bean.

The client implementation:

FrankfurterClient

This client performs the actual REST API calls.

---

# Configuration

API configuration is externalized using:

@ConfigurationProperties

Class:
FrankfurterApiProperties

Example configuration:

frankfurter.api.base-url=https://api.frankfurter.app
frankfurter.api.latest=/latest?base=IDR
frankfurter.api.historical=/2024-01-01..2024-12-31?from=IDR&to=USD
frankfurter.api.currencies=/currencies

---

# Internal API Endpoint

The application exposes one single endpoint as required:

GET /api/finance/data/{resourceType}

Where resourceType can be:

latest_idr_rates  
historical_idr_usd  
supported_currencies

Example requests:

GET /api/finance/data/latest_idr_rates

GET /api/finance/data/historical_idr_usd

GET /api/finance/data/supported_currencies

Controller:

FinanceController

---

# Strategy Pattern

The application uses the Strategy Pattern to dynamically handle resource types.

Strategy Interface:
DataStrategy

Strategy Implementations:

LatestRatesStrategy  
HistoricalRatesStrategy  
CurrenciesStrategy

Each strategy retrieves the correct data from the in-memory DataStore.

---

# Strategy Factory

Strategy selection is handled by:

StrategyFactory

Spring automatically injects strategies using:

Map<String, DataStrategy>

Each strategy is registered with a Spring component name:

@Component("latest_idr_rates")
@Component("historical_idr_usd")
@Component("supported_currencies")

The factory dynamically resolves the correct strategy based on the requested resourceType.

---

# Service Layer

FinanceService is responsible for:

• retrieving the correct strategy  
• executing the strategy  
• returning unified results

Output format is always returned as:

List<ApiResult>

This guarantees a consistent API response.

---

# Unified API Response Format

All responses follow this structure:

[
{
"resource": "latest_idr_rates",
"data": { ... }
}
]

Model:

ApiResult

Fields:

resource → resource type requested  
data → the resulting dataset

---

# Unique Spread Calculation

For the resource:

latest_idr_rates

The system calculates an additional field:

usdBuySpreadIdr

This represents the Rupiah selling rate to USD after applying a unique banking spread.

---

# Spread Factor Calculation

The spread factor is derived from the GitHub username.

GitHub username used:

danielsinaga

Step 1 — Sum Unicode values of all characters in the username.

Step 2 — Apply formula:

SpreadFactor = (SumOfUnicode % 1000) / 100000

This produces a unique spread between:

0.00000 – 0.00999

Step 3 — Final calculation:

USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)

Where:

Rate_USD is taken from the Frankfurter API when base = IDR.

The calculation is implemented in:

SpreadCalculator

and used inside:

FrankfurterService

---

# Data Storage

All API responses are stored in:

DataStore

This component acts as an in-memory cache containing:

LatestRatesResponse  
HistoricalRatesResponse  
CurrenciesResponse

Strategies retrieve data from this store instead of calling the external API again.

---

# Testing

The project includes unit and integration tests.

Unit Test:

FrankfurterServiceTest

Verifies that:

• USD spread calculation works correctly

Integration Test:

DataLoaderIntegrationTest

Verifies that:

• DataStore is populated correctly during application startup.

---

# Technologies Used

Java 17  
Spring Boot  
Spring Web  
Spring FactoryBean  
Lombok  
Mockito

---

# Running the Application

Run using Maven:

mvn spring-boot:run

or

./mvnw spring-boot:run

---

# Example Response

Example request:

GET /api/finance/data/latest_idr_rates

Example response:

[
{
"resource": "latest_idr_rates",
"data": {
"amount": 1,
"base": "IDR",
"date": "2026-03-11",
"rates": {
"USD": 0.000059
},
"usdBuySpreadIdr": 16991.18
}
}
]

---

# Design Patterns Used

FactoryBean Pattern  
Strategy Pattern  
Factory Pattern  
Singleton Pattern

---

# Summary

This application fulfills all required constraints:

✓ Startup API data loading  
✓ Custom FactoryBean API client  
✓ Strategy Pattern resource handling  
✓ Dynamic endpoint routing  
✓ Unified API response  
✓ Unique spread calculation based on GitHub username  
✓ Unit and integration testing