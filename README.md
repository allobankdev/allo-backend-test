# Finance Service – IDR Exchange Data API

Spring Boot application that fetches, processes, and exposes IDR-related financial data using a **Strategy Pattern**, **FactoryBean**, and **startup preloading mechanism**.

---

## Architectural Overview

The application is structured into clearly separated layers:

Controller
↓
Service
↓
In-Memory Data Store (Immutable Cache)
↓
Strategy Registry
↓
Fetcher Strategy (Strategy Pattern)
↓
External API Client (FactoryBean)

---

### Key Design Decisions

- **Strategy Pattern** for handling multiple resource types without `if/else`
- **FactoryBean** for external API client creation
- **ApplicationRunner** to preload all data once at startup
- **Immutable in-memory cache** for thread safety and performance
- **Testable units** with mocked external dependencies

---

## Project Structure

src/main/java
└── allobankdev.test.finance
├── controller
│ └── FinanceController.java
├── service
│ └── FinanceService.java
├── store
│ └── FinanceDataStore.java
├── registry
│ └── StrategyRegistry.java
├── strategy
│ ├── IDRDataFetcher.java
│ ├── LatestIdrRatesFetcher.java
│ ├── HistoricalIdrUsdFetcher.java
│ └── SupportedCurrenciesFetcher.java
├── client
│ └── FrankfurterClient.java
├── config
│ ├── FrankfurterProperties.java
│ └── FrankfurterClientFactoryBean.java
├── startup
│ └── FinanceStartupRunner.java
└── exception
└── InvalidResourceTypeException.java

src/test/java
└── allobankdev.test.finance
├── strategy
│ ├── LatestIdrRatesFetcherTest.java
│ ├── HistoricalIdrUsdFetcherTest.java
│ └── SupportedCurrenciesFetcherTest.java
└── integration
└── FinanceStartupIntegrationTest.java

### application.yml
frankfurter:
base-url: https://api.frankfurter.app

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Strategy Pattern
- FactoryBean
- JUnit 5 & Mockito
- JaCoCo (Code Coverage)
- Maven

---

## Setup & Run Instructions

### Clone Repository
git clone https://github.com/hamramaputra17/allo-backend-test

### Build Application
mvn clean package

### Run Tests (with Coverage)
mvn clean test

JaCoCo coverage report : target/site/jacoco/index.html

### Run Application
mvn spring-boot:run

### API Endpoint
GET /api/finance/data/{resourceType}

### Example cURL
- {hostname}/api/finance/data/latest_idr_rates
- {hostname}/api/finance/data/historical_idr_usd
- {hostname}/api/finance/data/supported_currencies

---

## Spread Factor
Git Username  : hamramaputra17

Unicode Sum   : (sum of Unicode values % 1000) / 100000.0

Spread Factor : 0.00387