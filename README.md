# 💰 IDR Finance Data Aggregator API

Spring Boot backend service that aggregates Indonesian Rupiah (IDR) exchange rate data from the public Frankfurter Exchange Rate API and exposes it via a single polymorphic REST endpoint.

Developed as part of the **Allo Bank Backend Developer Take-Home Test**.

---

# 📌 Objective

Build a production-ready Spring Boot REST API that demonstrates:

* Strategy Design Pattern
* FactoryBean client creation
* Startup data ingestion
* Thread-safe in-memory storage
* Immutable data serving
* External API integration
* Clean architecture & testing

---

# 🛠️ Tech Stack

* Java 17
* Spring Boot 3
* Gradle (Kotlin DSL)
* Spring WebFlux (WebClient)
* JUnit 5 + Mockito
* Lombok
* Mapper Pattern
* FactoryBean
* Strategy Pattern

---

# 📦 Project Structure

```
src
├── main
│   └── java
│       └── co.id.allobank.finance
│
│           ├── config
│           │   ├── FinanceDataStartupRunner
│           │   ├── FrankfurterClientFactoryBean
│           │   │
│           │   ├── mapper
│           │   │   ├── HistoricalRatesMapper
│           │   │   ├── LatestRatesMapper
│           │   │   └── SupportedCurrenciesMapper
│           │   │
│           │   └── strategy
│           │       ├── IDRDataFetcher
│           │       ├── LatestIDRRatesFetcher
│           │       ├── HistoricalIDRUSDFetcher
│           │       └── SupportedCurrenciesFetcher
│
│           ├── controller
│           │   └── FinanceDataController
│
│           ├── exception
│           │   ├── ErrorHandlingProperties
│           │   ├── ErrorResponse
│           │   ├── GlobalExceptionHandler
│           │   └── ServiceException
│
│           ├── model.response
│           │   ├── HistoricalIDRUsdRateResponse
│           │   ├── HistoricalRatesRawResponse
│           │   ├── LatestIDRRateResponse
│           │   ├── LatestRatesRawResponse
│           │   └── SupportedCurrencyResponse
│
│           ├── service
│           │   ├── FinanceDataService
│           │   └── InMemoryFinanceStore
│
│           ├── utils
│           │   ├── ErrorCode
│           │   └── SpreadCalculator
│
│           └── FinanceServiceApplication
│
└── test
    └── java
        └── co.id.allobank.finance
            ├── config.strategy
            │   └── FinanceDataStartupRunnerTest
            ├── service
            │   └── FinanceDataServiceTest
            ├── utils
            │   └── SpreadCalculatorTest
            └── FinanceServiceApplicationTests
```

---

# 🏗️ Architecture Overview

```
                ┌──────────────────────────┐
                │ Frankfurter Public API  │
                │ https://api.frankfurter │
                └─────────────┬───────────┘
                              │
                              │ WebClient (FactoryBean)
                              ▼
                    ┌────────────────────┐
                    │ Strategy Pattern  │
                    │-------------------│
                    │ Latest Fetcher    │
                    │ Historical        │
                    │ Currencies        │
                    └─────────┬─────────┘
                              │
                              ▼
                 ┌────────────────────────┐
                 │ Startup Runner Loader │
                 │ (ApplicationRunner)   │
                 └─────────┬─────────────┘
                           │
                           ▼
               ┌──────────────────────────┐
               │ In-Memory Data Store    │
               │ Concurrent + Immutable │
               └─────────┬──────────────┘
                         │
                         ▼
               ┌──────────────────────────┐
               │ REST Controller          │
               │ /api/finance/data/{t}   │
               └──────────────────────────┘
```

---

# 🔌 External API Integration

Base URL:

```
https://api.frankfurter.app
```

Integrated resources:

| Resource             | Endpoint                                  |
| -------------------- | ----------------------------------------- |
| Latest IDR Rates     | `/latest?base=IDR`                        |
| Historical IDR → USD | `/2024-01-01..2024-01-05?from=IDR&to=USD` |
| Supported Currencies | `/currencies`                             |

---

# 🚀 Application Flow

## 1️⃣ Startup Data Ingestion

Executed via:

```
FinanceDataStartupRunner
```

Type:

```
ApplicationRunner
```

Responsibilities:

* Execute all fetch strategies
* Retrieve external data
* Aggregate responses
* Store results in memory

Data is fetched **exactly once** during startup.

---

## 2️⃣ In-Memory Store

Handled by:

```
InMemoryFinanceStore
```

Characteristics:

* Thread-safe
* Immutable after initialization
* Read-only at runtime
* No external API calls per request

---

## 3️⃣ Strategy Pattern (Polymorphic Fetching)

Strategy Interface:

```
IDRDataFetcher
```

Implementations:

| Resource Type        | Strategy Class             |
| -------------------- | -------------------------- |
| latest_idr_rates     | LatestIDRRatesFetcher      |
| historical_idr_usd   | HistoricalIDRUSDFetcher    |
| supported_currencies | SupportedCurrenciesFetcher |

Spring injects strategies as:

```
Map<String, IDRDataFetcher>
```

This removes the need for `if/else` or `switch` logic.

---

# 📡 API Endpoint

## GET /api/finance/data/{resourceType}

### Path Values

| Value                | Description                                |
| -------------------- | ------------------------------------------ |
| latest_idr_rates     | Latest exchange rates + spread calculation |
| historical_idr_usd   | Historical IDR → USD rates                 |
| supported_currencies | Currency symbol list                       |

---

# 🧪 cURL Examples

## 1️⃣ Latest IDR Rates

```bash
curl --location \
'http://localhost:8080/api/finance/data/latest_idr_rates'
```

---

## 2️⃣ Historical IDR → USD

```bash
curl --location \
'http://localhost:8080/api/finance/data/historical_idr_usd'
```

---

## 3️⃣ Supported Currencies

```bash
curl --location \
'http://localhost:8080/api/finance/data/supported_currencies'
```

---

# 🧮 Spread Factor Personalization

GitHub Username:

```
dhimaspanji
```

### Spread Calculation

```
Spread Factor =
(sum of ASCII username % 1000) / 100000.0
```

### Final Formula

```
USD_BuySpread_IDR =
(1 / Rate_USD) * (1 + SpreadFactor)
```

Implemented in:

```
SpreadCalculator.java
```

Unit tested via:

```
SpreadCalculatorTest.java
```

---

# 🏭 FactoryBean Client Design

External API client is created via:

```
FrankfurterClientFactoryBean
```

Responsibilities:

* Construct WebClient
* Inject Base URL
* Configure timeout & headers
* Centralize client configuration

### Why FactoryBean?

* Custom instantiation lifecycle
* Encapsulation of client setup
* Cleaner external integration
* More flexible than standard `@Bean`

---

# 🧠 Architectural Rationale

## 1️⃣ Strategy Pattern

The Strategy Pattern was used to dynamically handle multiple resource types without conditional logic.

### Benefits

* Eliminates `if/else` blocks.
* Supports Open/Closed Principle.
* Improves extensibility.
* Encapsulates logic per resource.
* Enhances testability.

Adding new resources only requires implementing a new strategy.

---

## 2️⃣ FactoryBean

A custom `FactoryBean<WebClient>` was implemented to centralize external client construction.

### Benefits

* Externalized base URL configuration.
* Shared headers & timeouts.
* Decoupled creation logic.
* Advanced Spring bean lifecycle usage.

Preferred over simple `@Bean` for flexibility and maintainability.

---

## 3️⃣ ApplicationRunner vs @PostConstruct

`ApplicationRunner` was selected for startup ingestion.

| Capability               | ApplicationRunner | @PostConstruct |
| ------------------------ | ----------------- | -------------- |
| Runs after context ready | ✔️                | ❌              |
| Access to args           | ✔️                | ❌              |
| Better lifecycle control | ✔️                | ❌              |

Ensures all beans are initialized before ingestion.

---

# 🧪 Testing

## Unit Tests

Covered components:

* All strategy fetchers
* FinanceDataService
* SpreadCalculator

External calls mocked.

---

## Integration Test

```
FinanceDataStartupRunnerTest
```

Validates:

* Runner execution
* Successful data ingestion
* Store initialization before API ready

---

# ⚙️ Configuration

`application.yaml`

```yaml
external:
  frankfurter:
    base-url: https://api.frankfurter.app
```

---

# ▶️ How To Run

## Prerequisites

* Java 17
* Gradle 8+

Check version:

```bash
java -version
```

---

## Build

```bash
./gradlew clean build
```

---

## Run

```bash
./gradlew bootRun
```

Application URL:

```
http://localhost:8080
```

---

# ✅ Production Readiness Checklist

* ✔ Strategy Pattern
* ✔ FactoryBean client
* ✔ Startup ingestion
* ✔ Immutable in-memory store
* ✔ Thread-safe design
* ✔ Externalized configuration
* ✔ Unit tests
* ✔ Integration test
* ✔ Global exception handling

---

# 👨‍💻 Author

**Dhimas Panji**
Backend Engineer — Spring Boot

---

# 📌 Notes

This solution demonstrates:

* Polymorphic API architecture
* Production-grade layering
* Startup data aggregation
* Thread-safe caching
* Extensible strategy design

Built to reflect real-world financial backend service standards.

---
