# Allo Bank Backend Developer Take-Home Test

A Spring Boot 4.0.5 REST API that aggregates Indonesian Rupiah (IDR) exchange rate data from the [Frankfurter Exchange Rate API](https://api.frankfurter.app/). Data is fetched once at startup and served from an immutable, thread-safe in-memory store.

---

## 📋 Table of Contents

- [Setup & Run Instructions](#-setup--run-instructions)
- [Endpoint Usage](#-endpoint-usage)
- [Personalization Note](#-personalization-note)
- [Architectural Rationale](#️-architectural-rationale)
- [Project Structure](#-project-structure)

---

## 🚀 Setup & Run Instructions

### Prerequisites

- **Java 17+** (JDK)
- **Maven 3.9+** (or use the included Maven Wrapper)

### Clone & Build

```bash
git clone <repository-url>
cd backendtest
```

### Run the Application

```bash
# Using Maven Wrapper (recommended)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`. On startup, it fetches all three data resources from the Frankfurter API and loads them into memory.

### Run Tests

```bash
# Run all tests (unit + integration)
./mvnw test

# Run only unit tests
./mvnw test -Dtest="*FetcherTest"

# Run only integration tests
./mvnw test -Dtest="DataLoaderRunnerIntegrationTest"
```

---

## 📡 Endpoint Usage

### Single Endpoint

```
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` is one of: `latest_idr_rates`, `historical_idr_usd`, or `supported_currencies`.

### cURL Examples

#### 1. Latest IDR Rates (with USD Buy Spread)

```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq .
```

Response includes the enriched `USD_BuySpread_IDR` field:

```json
{
  "resourceType": "latest_idr_rates",
  "data": {
    "amount": 1.0,
    "base": "IDR",
    "date": "2026-04-17",
    "rates": {
      "AUD": 0.000081,
      "USD": 0.000058,
      "EUR": 0.000049
    },
    "githubUsername": "erlanggariansyah",
    "spreadFactor": 0.00696,
    "USD_BuySpread_IDR": 17361.38
  }
}
```

#### 2. Historical IDR/USD Data

```bash
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq .
```

```json
{
  "resourceType": "historical_idr_usd",
  "data": {
    "amount": 1.0,
    "base": "IDR",
    "startDate": "2023-12-29",
    "endDate": "2024-01-05",
    "rates": {
      "2023-12-29": { "USD": 0.000065 },
      "2024-01-02": { "USD": 0.000064 },
      "2024-01-03": { "USD": 0.000064 },
      "2024-01-04": { "USD": 0.000064 },
      "2024-01-05": { "USD": 0.000064 }
    }
  }
}
```

#### 3. Supported Currencies

```bash
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq .
```

```json
{
  "resourceType": "supported_currencies",
  "data": {
    "AUD": "Australian Dollar",
    "IDR": "Indonesian Rupiah",
    "USD": "United States Dollar"
  }
}
```

#### 4. Invalid Resource Type (Error Response)

```bash
curl -s http://localhost:8080/api/finance/data/invalid_type | jq .
```

```json
{
  "timestamp": "2026-04-17T08:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Unknown resource type: 'invalid_type'. Supported types: [latest_idr_rates, historical_idr_usd, supported_currencies]"
}
```

---

## 🔑 Personalization Note

| Field             | Value                |
|-------------------|----------------------|
| **GitHub Username** | `erlanggariansyah` |
| **Unicode Sum**     | 1696               |
| **Sum % 1000**      | 696                |
| **Spread Factor**   | **0.00696**        |

### Calculation Breakdown

```
Username (lowercase): erlanggariansyah

e=101  r=114  l=108  a=97   n=110  g=103  g=103  a=97
r=114  i=105  a=97   n=110  s=115  y=121  a=97   h=104

Sum of Unicode values = 1696
Spread Factor = (1696 % 1000) / 100000.0 = 696 / 100000.0 = 0.00696

USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00696)
```

---

## 🏛️ Architectural Rationale

### 1. Polymorphism Justification: Why the Strategy Pattern?

The Strategy Pattern was chosen over a simpler conditional block (`if/else` or `switch`) for handling the multi-resource endpoint for the following reasons:

**Extensibility (Open/Closed Principle):** Adding a new resource type (e.g., `currency_conversion`) requires only creating a new `IDRDataFetcher` implementation annotated with `@Component`. The service, controller, and existing strategies require zero modifications. With a conditional block, every new resource would require modifying the existing service method — violating the Open/Closed Principle.

**Maintainability & Separation of Concerns:** Each strategy class is self-contained — it owns its API endpoint path, deserialization logic, and any data transformations. In a conditional approach, all three resource handlers would be interleaved in a single method, making it harder to test, review, and modify independently.

**Spring-Native Registration:** By leveraging Spring's `List<IDRDataFetcher>` injection and converting it to a `Map<String, IDRDataFetcher>`, the controller achieves O(1) strategy lookup with no manual wiring. The framework itself handles strategy discovery and registration.

**Testability:** Each strategy is independently unit-testable with its own mock WebClient, without needing to set up the context for unrelated resource types.

### 2. Client Factory: Why FactoryBean over @Bean?

The `FactoryBean<WebClient>` approach provides several advantages over a standard `@Bean` method:

**Encapsulated Lifecycle Control:** `FactoryBean` is a first-class Spring contract for complex bean creation. It clearly communicates that the `WebClient` instance requires non-trivial configuration (timeouts, base URL, default headers, HTTP client connector) — making it a dedicated "factory" rather than a one-liner in a `@Configuration` class.

**Singleton Semantics:** The `isSingleton()` method explicitly declares caching behavior, making it clear to Spring (and reviewers) that only one `WebClient` instance is created and shared across all strategies.

**Framework Integration:** `FactoryBean` participates in Spring's bean lifecycle more deeply than `@Bean` — it can leverage `BeanFactoryAware`, `InitializingBean`, and other lifecycle interfaces if the factory logic grows in complexity (e.g., dynamic URL resolution, credential injection from a vault).

**Self-Documenting:** A dedicated `WebClientFactoryBean` class is easier to discover in the codebase than a `@Bean` method buried in a `@Configuration` class, especially as the application grows.

### 3. Startup Runner: Why ApplicationRunner over @PostConstruct?

`ApplicationRunner` was chosen over `@PostConstruct` for the initial data ingestion for these reasons:

**Full Context Guarantee:** `ApplicationRunner.run()` is invoked after the entire Spring application context is fully initialized — all beans, including the `FactoryBean`-created `WebClient` and all strategy implementations, are guaranteed to be available. `@PostConstruct` runs during bean initialization, which means dependencies may not be fully constructed yet, particularly when `FactoryBean` beans are involved.

**Startup Isolation:** `ApplicationRunner` runs after the context is ready but before the application starts accepting HTTP traffic. This guarantees that the in-memory store is fully populated before any client request hits the endpoint.

**Application Arguments Access:** `ApplicationRunner` provides access to `ApplicationArguments`, enabling future enhancements like `--refresh-data` flags or environment-specific overrides without refactoring the initialization mechanism.

**Error Handling:** Exceptions thrown from `ApplicationRunner.run()` cleanly abort the application startup with a clear error message, preventing the server from starting with empty data. `@PostConstruct` errors can sometimes lead to partially initialized contexts that are harder to diagnose.

---

## 📁 Project Structure

```
src/main/java/com/allobank/backendtest/
├── BackendtestApplication.java              # Spring Boot entry point
├── config/
│   └── WebClientFactoryBean.java            # FactoryBean<WebClient> (Constraint B)
├── model/
│   ├── LatestRatesResponse.java             # DTO for /latest response
│   ├── HistoricalRatesResponse.java         # DTO for historical time-series
│   └── ResourceResult.java                  # Unified API response envelope
├── strategy/
│   ├── IDRDataFetcher.java                  # Strategy interface
│   ├── LatestIdrRatesFetcher.java           # Strategy: latest rates + spread
│   ├── HistoricalIdrUsdFetcher.java         # Strategy: historical IDR/USD
│   └── SupportedCurrenciesFetcher.java      # Strategy: currency symbols
├── service/
│   └── FinanceDataService.java              # In-memory store + strategy map
├── runner/
│   └── DataLoaderRunner.java                # ApplicationRunner (Constraint C)
├── controller/
│   └── FinanceDataController.java           # REST controller (single endpoint)
└── exception/
    ├── ExternalApiException.java            # Custom exception for API errors
    └── GlobalExceptionHandler.java          # @ControllerAdvice error handler

src/test/java/com/allobank/backendtest/
├── strategy/
│   ├── LatestIdrRatesFetcherTest.java       # Unit test (spread calculation)
│   ├── HistoricalIdrUsdFetcherTest.java     # Unit test (deserialization)
│   └── SupportedCurrenciesFetcherTest.java  # Unit test (currency map)
└── runner/
    └── DataLoaderRunnerIntegrationTest.java  # Integration test (startup loader)
```
