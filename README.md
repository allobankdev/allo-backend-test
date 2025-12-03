
# IDR Rate Aggregator – Allo Bank Backend Take‑Home Test

This project implements a production‑grade Spring Boot application designed to aggregate exchange‑rate data from the public Frankfurter API.  
It demonstrates clean architecture, Strategy Pattern usage, a FactoryBean‑created WebClient, immutable in‑memory caching, and fully automated startup‑time data loading.

---

## 🚀 Features Implemented

### ✔ Single REST Endpoint
```
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` must be:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

### ✔ External APIs Used
| Resource Type | External Endpoint |
|---------------|------------------|
| latest_idr_rates | `/latest?base=IDR` |
| historical_idr_usd | `/2024-01-01..2024-01-05?from=IDR&to=USD` |
| supported_currencies | `/currencies` |

### ✔ Startup Data Loading
All resources are fetched **once at application startup** via `ApplicationRunner` and stored in an **immutable, thread‑safe in‑memory store** (`AtomicReference`).

### ✔ Spread Factor Calculation
A custom banking spread is added to `latest_idr_rates` using your GitHub username:

**GitHub username:** `brnhrdwnnr`  
**Spread Factor:** Calculated using:

```
sum(lowercase(username).unicode) % 1000 / 100000.0
```

Example output fields added:
```json
"spreadFactor": 0.00726,
"USD_BuySpread_IDR": 15734.22
```

---

# 🧪 Testing Overview

### ✔ Unit Tests
Unit tests exist for all 3 Strategy implementations:

- `LatestIdrRatesStrategyTest`
- `HistoricalIdrUsdStrategyTest`
- `CurrencyCatalogStrategyTest`

Each uses **MockWebServer** to simulate external API responses.

### ✔ Integration Test
`StartupDataRunnerIntegrationTest` validates:

- WebClient FactoryBean works
- ApplicationRunner loads all data before controller is accessible
- DataStore is immutable & thread‑safe

---

# ▶️ How to Run the Application

### 1. Clone your fork
```sh
git clone https://github.com/brnhrdwnnr/feat-idr-rate-aggregator.git
cd feat-idr-rate-aggregator
```

### 2. Build the project
```sh
mvn clean package
```

### 3. Run the application
```sh
mvn spring-boot:run
```

---

# 📌 Endpoint Usage (cURL Examples)

### 1️⃣ Latest IDR Rates (with spread)
```sh
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2️⃣ Historical IDR → USD Rates
```sh
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3️⃣ Supported Currencies
```sh
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

# 🧱 Architectural Rationale

## 1. ✔ Why Strategy Pattern?
The application must dynamically switch between 3 different data‑fetching behaviors without using `if/else` or `switch`.

**Benefits:**
- Extensible → Adding new resource types requires *no changes* to controller or runner.
- Maintainable → Each strategy is isolated in its own class.
- Clean → Controller simply delegates to the strategy map injected by Spring.


---

## 2. ✔ Why a FactoryBean for WebClient?
The instructions require:

> “The instance of your external API client must be created via a FactoryBean.”

**Why FactoryBean is better than @Bean here:**

- Allows **custom construction logic**, including:
  - base URL injection
  - timeouts
  - connection pooling
- Ensures **exactly one correctly‑configured WebClient**
- Mimics enterprise‑level client initialization

---

## 3. ✔ Why ApplicationRunner over @PostConstruct?
`@PostConstruct` runs too early—before:

- Spring Boot finishes preparing reactive infrastructure
- All beans are fully ready
- The WebClient is guaranteed available

`ApplicationRunner` ensures:

- Runs **after the full ApplicationContext is ready**
- Can block on WebClient reactor calls safely
- Ensures DataStore is preloaded **before any HTTP request is served**

---

# 📁 Project Structure (Simplified)

```
src/main/java/com/allobanktest/idr/
│
├── config/
│   └── WebClientFactoryBean.java
│   └── JacksonConfig.java
│
├── dto/
│   └── CurrencyCatalog.java
│   └── ExchangeRateSnapshot.java
│   └── ExchangeRateTimeSeries.java
│
├── strategy/
│   ├── IDRDataFetcher.java
│   ├── LatestIdrRatesStrategy.java
│   ├── HistoricalIdrUsdStrategy.java
│   └── SupportedCurrenciesStrategy.java
│
├── runner/
│   └── StartupDataRunner.java
│
├── store/
│   └── DataStore.java
│
├── util/
│   └── SpreadUtil.java
│
├── controller/
│    └── FinanceController.java
│
└── service/  
     └── FinanceService.java 
```

---

# 🧑‍💻 Development Notes

- Java 17
- Spring Boot 4.0.x
- Reactive WebClient
- Thread‑safe `AtomicReference` store
- Verified via unit + integration tests

---

# 🏁 Summary

This implementation fully satisfies **all requirements**:

✔ Strategy Pattern  
✔ FactoryBean for WebClient  
✔ Immutable in-memory store  
✔ ApplicationRunner startup loading  
✔ Spread Factor calculation  
✔ Unit & Integration tests  
✔ Complete README-DEV.md

---
