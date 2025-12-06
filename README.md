# Allo Bank IDR Aggregator

A production–ready Spring Boot application implementing polymorphic data fetching, immutability guarantees, and external API aggregation using the Frankfurter Exchange Rate API.

This project fulfills **Allo Bank Backend Developer Take‑Home Test** requirements by integrating three external resources, applying Strategy Pattern, loading all data at startup, and serving them via a unified endpoint.

---

## 📁 ROOT STRUCTURE

```
ALLO-BANK-IDR-AGGREGATOR
├── .mvn/
├── .vscode/
│
├── app/
├── core/
├── data/
├── presenter/
│
├── .flattened-pom.xml
├── .gitattributes
├── .gitignore
│
├── HELP.md
│
├── mvnw
├── mvnw.cmd
│
├── pom.xml
└── README.md
```

---

## 📁 FULL PROJECT STRUCTURE

```
ALLO-BANK-IDR-AGGREGATOR
app
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── bank
    │   │           └── allo
    │   │               ├── config
    │   │               │   ├── CoreConfig.java
    │   │               │   ├── DataConfig.java
    │   │               │   ├── RunnerConfig.java
    │   │               │   └── StrategyConfig.java
    │   │               │
    │   │               ├── properties
    │   │               │   ├── AppProperties.java
    │   │               │   └── FrankfurterProperties.java
    │   │               │
    │   │               ├── runner
    │   │               │   └── DataLoadRunner.java
    │   │               │
    │   │               ├── AlloBankIdrAggregatorApplication.java
    │   │               └── LoggerConfig.java
    │   │
    │   └── resources
    │
    └── test
        └── java
            └── com
                └── bank
                    └── allo
                        ├── ApplicationStartupIntegrationTest.java
                        ├── DataLoadRunnerTest.java
                        └── FinanceEndpointIntegrationTest.java

core
└── src
    └── main
        └── java
            └── com
                └── bank
                    └── allo
                        ├── domain
                        │   └── idr
                        │       ├── LatestRates.java
                        │       ├── SupportedCurrencies.java
                        │       └── HistoricalRates.java
                        │
                        ├── exception
                        │   ├── BadRequestException.java
                        │   ├── DomainException.java
                        │   ├── NotFoundException.java
                        │   └── UnauthorizedException.java
                        │
                        ├── repository
                        │   ├── inbound
                        │   │   └── DataStore.java
                        │   │
                        │   └── outbound
                        │       └── FrankfurterClientRepository.java
                        │
                        ├── usecase
                        │   ├── idr
                        │   │   ├── FetchIdrDataUseCase.java
                        │   │   └── IdrDataFetcher.java
                        │   │
                        │   ├── UseCase.java
                        │   └── UseCaseExecutor.java
                        │
                        ├── utils
                        │   ├── IdrRateMapperUtils.java
                        │   └── SpreadCalculator.java
                        │
                        └── resources
                            
    └── test
        └── java
            └── com
                └── bank
                    └── allo
                        ├── usecase
                        │   └── idr
                        │       └── FetchIdrDataUseCaseTest.java
                        │
                        └── utils
                            ├── SpreadCalculatorTest.java
                            └── IdrRateMapperUtilsTest.java

data
└── src
    └── main
        └── java
            └── com
                └── bank
                    └── allo
                        └── client
                            ├── fetcher
                            │   ├── HistoricalIdrUsdFetcher.java
                            │   ├── LatestIdrRatesFetcher.java
                            │   ├── SupportedCurrenciesFetcher.java
                            │
                            ├── FrankfurterClientRepositoryImpl.java
                            ├── FrankfurterWebClientFactoryBean.java
                            │
                            └── store
                                └── InMemoryDataStoreImpl.java
        └── resources
    └── test
        └── java
            └── com
                └── bank
                    └── allo
                        ├── client
                        │   ├── fetcher
                        │   │   ├── HistoricalIdrUsdFetcherTest.java
                        │   │   ├── LatestIdrRatesFetcherTest.java
                        │   │   └── SupportedCurrenciesFetcherTest.java
                        │   │
                        │   ├── FrankfurterClientRepositoryImplTest.java
                        │   └── FrankfurterWebClientFactoryBeanTest.java
                        │
                        └── store
                            └── InMemoryDataStoreImplTest.java

presenter
└── src
    └── main
        └── java
            └── com
                └── bank
                    └── allo
                        └── rest
                            ├── controller
                            │   ├── idr
                            │   │   ├── FinanceController.java
                            │   │   └── FinanceResource.java
                            │   │
                            │   └── GlobalExceptionHandler.java
                            │
                            ├── entity
                            │   ├── historical
                            │   │   └── HistoricalRatesResponse.java
                            │   │
                            │   ├── latest
                            │   │   └── LatestRatesResponse.java
                            │   │
                            │   ├── supported
                            │   │   └── SupportedCurrenciesResponse.java
                            │   │
                            │   └── ApiResponse.java
                            │
                            ├── mapper
                            │   └── FinanceMapper.java
                            │
                            └── usecase
                                └── UseCaseExecutorImpl.java
        └── resources
    └── test
        └── java
            └── com
                └── bank
                    └── allo
                        ├── rest
                        |   ├── controller
                        |   │   ├── idr
                        |   │   │   ├── FinanceControllerTest.java
                        |   │   │   └── FinanceResourceTest.java
                        |   │   │
                        |   │   └── GlobalExceptionHandlerTest.java
                        └── usecase
                            └── UseCaseExecutorImplTest.java
```

---

# I. OBJECTIVE

Create a single REST API endpoint:

```
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` ∈:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

The application aggregates data from the public Frankfurter API:

- `/latest?base=IDR`
- `/2024-01-01..2024-01-05?from=IDR&to=USD`
- `/currencies`

---

# II. FEATURES & BEHAVIOR

### ✔ Polymorphic resource handling using **Strategy Pattern**

Each resource is handled by a specific strategy implementing:

```
IdrDataFetcher
```

Strategies:

- `LatestIdrRatesFetcher`
- `HistoricalIdrUsdFetcher`
- `SupportedCurrenciesFetcher`

All strategies are auto‑registered via map lookup—no if/switch blocks.

---

### ✔ Custom Spread Calculation (Latest Rates Only)

Using GitHub username: **saputramb**

```
sum(unicode chars) % 1000 / 100000.0 → spreadFactor
USD_BuySpread_IDR = (1 / rateUSD) * (1 + spreadFactor)
```

Value is included in the API response.

---

### ✔ Data loaded once at startup (immutably)

A `DataLoadRunner` fetches **all three resources exactly once** at startup and stores them in a **thread‑safe immutable** in‑memory store.

The API endpoint always serves startup-loaded data.

---

### ✔ External API Client from FactoryBean

The WebClient is created using:

```
FrankfurterWebClientFactoryBean
```

Not a regular @Bean method.

It externalizes base URL and configures:

- timeouts  
- connection settings  
- reusable client instance  

---

# III. RUNNING THE APPLICATION

## 1. Clone repository

```
git clone -b feat/idr-rate-aggregator https://github.com/allobankdev/allo-backend-test
cd allo-backend-test
git checkout feat/idr-rate-aggregator

```

## 2. Run tests

```
mvn clean test
```

## 3. Run application

```
mvn spring-boot:run -pl app
```

No Docker is required.

> **Docker image intentionally not provided**, since the project requires no external dependencies and runs identically across environments via Maven boot‑run.

---

# IV. ENDPOINT USAGE

### **1. Latest IDR Rates**

```
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### **2. Historical IDR → USD (2024-01-01..05)**

```
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### **3. Supported Currencies**

```
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

# V. TESTING

### ✔ Unit Tests

- All fetcher strategies
- Mapper utilities
- Spread calculation
- Repository implementations
- Runner logic (DataLoadRunner)

### ✔ Integration Tests

- Application startup loads data correctly
- Startup data is immutable
- Controller returns unified responses
- Spread factor is injected correctly

---

# VI. ARCHITECTURAL RATIONALE

## 1. Why Strategy Pattern?

A single endpoint supports multiple resource types.  
Using Strategy provides:

### Benefits:
- More extensible (simply add new strategies)
- Eliminates nested conditionals
- Cleaner controller layer
- Open/Closed Principle compliant
- Auto-wired map-based dispatching

This is superior to `if/else` or `switch` branching.

---

## 2. Why FactoryBean for WebClient?

Using a `FactoryBean` allows:

- Controlled, centralized instantiation
- Externalized config (`application.yml`)
- Ability to build complex WebClient settings (timeouts, headers)
- Keeps client creation out of @Configuration

This adheres to inversion-of-control principles and avoids misconfiguration.

---

## 3. Why ApplicationRunner over @PostConstruct?

### ApplicationRunner Advantages:
- Runs after full Spring context is ready
- Allows async/thread-safe initialization
- Plays well with tests and conditional startup logic
- Avoids @PostConstruct side effects (e.g., premature bean access)

The aggregator requires a fully initialized context, making ApplicationRunner ideal.

---

# VII. PERSONAL SPREAD FACTOR

GitHub username: **saputramb**

Using algorithm:

```
SpreadFactor = (sum(unicode chars) % 1000) / 100000.0
```

Example:

```
SpreadFactor = 0.00765
```

---

# VIII. PULL REQUEST REQUIREMENTS

Your PR should include:

- Clean commit history  
- A clear PR description summarizing implementation  
- Full answers to architectural rationale  
- Notes about spread factor  
- Example curl usage  

Branch name example:

```
feat/idr-rate-aggregator
```

---

# IX. FINAL NOTES

This project is designed as a production-quality demonstration:

- Clean architecture (app/core/data/presenter)
- Immutable startup-loaded data
- Strategy-based polymorphism
- Unit + integration test coverage
- External API client using FactoryBean
- Unified endpoint for all data

If this were deployed in real infrastructure, caching, circuit breaker, and monitoring would be natural extensions.

---

**Author:** Putra  
**GitHub:** https://github.com/saputramb  
