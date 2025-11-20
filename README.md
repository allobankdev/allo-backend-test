# IDR Rate Aggregator

Spring Boot 3.5 service that preloads key Indonesian Rupiah datasets from the Frankfurter public API and serves them through a single polymorphic endpoint.

---

## Table of Contents
1. [Architecture & Patterns](#architecture--patterns)
2. [Setup & Run](#setup--run)
3. [Testing](#testing)
4. [Endpoint Usage](#endpoint-usage)
5. [Personalization Note](#personalization-note)
6. [API Documentation](#api-documentation)

---

## Architecture & Patterns

| Concern | Implementation |
| --- | --- |
| **Polymorphism (Strategy Pattern)** | `IdrDataFetchStrategy` defines the contract, while `LatestIdrRatesStrategy`, `HistoricalIdrUsdStrategy`, and `SupportedCurrenciesStrategy` provide resource-specific behavior. `IdrDataStrategyRegistry` offers constant-time lookup without `if/else` chains and enables easy extension. |
| **Client Provisioning (FactoryBean)** | `FrankfurterWebClientFactoryBean` builds a single, preconfigured `WebClient` using properties for base URL and timeouts. This centralizes client creation, avoids duplicated configuration, and allows Spring to manage lifecycle as a singleton bean. |
| **Immutable, Thread-Safe Cache** | `FinanceDataCache` stores startup results in an immutable `EnumMap` protected by `AtomicReference`, guaranteeing single initialization and read-only access for concurrent requests. |
| **Startup Loading Template** | `FinanceDataInitializer` (an `ApplicationRunner`) invokes every strategy once at boot, ensuring the REST API serves cached data only. Using the runner guarantees ordering and avoids lifecycle pitfalls of `@PostConstruct`. |
| **Separation of Concerns** | `FinanceDataService` shields the controller from caching details, while `RestExceptionHandler` normalizes error responses. |

---

## Setup & Run

### Prerequisites
- JDK 17+
- Internet access for Maven dependency download

### Installation & Startup
```bash
# Clone
git clone <your-fork-or-repo-url>
cd idr-rate-aggregator

# Build (compiles + packages jar)
./mvnw clean package

# Run the Spring Boot application
./mvnw spring-boot:run
```

The service listens on `http://localhost:8080` by default. During startup it fetches all three Frankfurter resources exactly once.

---

## Testing

Execute the full unit + integration test suite:
```bash
./mvnw test
```

This validates strategy calculations (including the personalized spread) and the ApplicationRunner bootstrap flow.

---

## Endpoint Usage

Endpoint: `GET /api/finance/data/{resourceType}`

Replace `{resourceType}` with:
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Example cURL commands:

```bash
# Latest IDR-based exchange rates with USD buy spread
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq

# Historical IDR→USD rates (2024-01-01 .. 2024-01-05)
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq

# Supported currency symbol map
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq
```

Each response is an array containing the cached payload for the requested resource, along with the timestamp it was fetched at startup.

---

## Personalization Note

- **GitHub Username:** `praydwi24`
- **Spread Factor Calculation:**
    - Sum of ASCII codes = 870
    - `Spread Factor = (870 % 1000) / 100000 = 0.00870`
- This spread factor feeds the formula `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00870)`

---

## API Documentation

- **Interactive UI:** `http://localhost:8080/swagger/index.html`
- **OpenAPI Spec:** `http://localhost:8080/openapi.yaml`

The Swagger page is a static asset (served without Springdoc) and mirrors the live endpoint definitions.