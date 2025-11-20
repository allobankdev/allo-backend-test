# Allo Bank – IDR Rate Aggregator (Take-Home Test)

This project is an implementation of the **Allo Bank Backend Developer Take-Home Test**.

It is a Spring Boot application that exposes **one polymorphic REST endpoint** which aggregates data from the public, keyless **Frankfurter Exchange Rate API**, with a focus on **Indonesian Rupiah (IDR)**:

- `/latest?base=IDR`
- `/2024-01-01..2024-01-05?from=IDR&to=USD`
- `/currencies`

The endpoint is implemented using the **Strategy Pattern**, a **WebClient FactoryBean**, and an **ApplicationRunner-based startup loader** that populates an immutable, in-memory cache.

---

## 1. Setup & Run Instructions

### 1.1. Prerequisites

- **Java 17**
- **Maven 3.9+** (or use `mvnw` wrapper if available)
- Git (to clone the repository)

### 1.2. Clone Repository & Checkout Branch

```bash
git clone https://github.com/hanifnfl097/allo-backend-test.git
cd allo-backend-test

# checkout implementation branch
git checkout feat/idr-rate-aggregator
```

### 1.3. Configuration

The application uses a configuration property for the Frankfurter API base URL and GitHub username:

```yaml
frankfurter:
  base-url: https://api.frankfurter.app

github:
  username: hanifnfl097
```

By default, this is defined in `src/main/resources/application.yml`.  
You can override it via environment variable or command line if needed, for example:

```bash
# Example: override base URL (if needed)
export FRANKFURTER_BASE_URL=https://api.frankfurter.app
```

or:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--frankfurter.base-url=https://api.frankfurter.app"
```

### 1.4. Build the Application

```bash
mvn clean package
```

### 1.5. Run the Application

```bash
mvn spring-boot:run
```

By default the service will start on:

```text
http://localhost:8080
```

On startup, an `ApplicationRunner` will:

1. Call all three external resources exactly once.
2. Transform and aggregate the responses.
3. Store them in an in-memory, thread-safe and immutable cache.
4. After that, the REST endpoint will always read from this cache (no further external calls).

### 1.6. Run Tests

```bash
mvn test
```

Tests included:

- **Unit tests** for:
    - Strategy classes (e.g. `LatestIdrRatesFetcher`, `HistoricalIdrUsdFetcher`, `SupportedCurrenciesFetcher`)
    - Spread factor calculation utility
- **Integration test**:
    - Bootstraps the Spring context.
    - Uses MockWebServer to simulate Frankfurter API.
    - Verifies that:
        - The `ApplicationRunner` loads data at startup.
        - The API endpoint serves data from the in-memory cache.

---

## 2. Endpoint Usage

### 2.1. Overview

Single polymorphic endpoint:

```http
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` is one of:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

The endpoint always returns a **JSON array** of unified response objects.

> **Note:** Data is always served from the in-memory cache that is loaded once at startup.

---

### 2.2. `latest_idr_rates`

Fetches latest exchange rates with **base IDR**, calculates a personalized **USD_BuySpread_IDR** based on the GitHub username–derived spread factor, and returns a unified response.

**Request:**

```bash
curl "http://localhost:8080/api/finance/data/latest_idr_rates"
```

**Example response (simplified):**

```json
[
  {
    "resourceType": "latest_idr_rates",
    "base": "IDR",
    "date": "2024-02-09",
    "payload": {
      "amount": 1.0,
      "rates": {
        "USD": 0.000064,
        "EUR": 0.000059
      }
    },
    "usdBuySpreadIdr": 15780.9375,
    "spreadFactor": 0.00998
  }
]
```

---

### 2.3. `historical_idr_usd`

Fetches historical rates for a fixed period:

```text
/2024-01-01..2024-01-05?from=IDR&to=USD
```

Transforms the time series into a unified list of entries.

**Request:**

```bash
curl "http://localhost:8080/api/finance/data/historical_idr_usd"
```

**Example response (simplified):**

```json
[
  {
    "date": "2023-12-29",
    "usdRate": 0.000065
  },
  {
    "date": "2024-01-02",
    "usdRate": 0.000064
  }
]
```

---

### 2.4. `supported_currencies`

Fetches the full list of supported currencies from `/currencies` and transforms the map into an array of unified objects.

**Request:**

```bash
curl "http://localhost:8080/api/finance/data/supported_currencies"
```

**Example response (simplified):**

```json
[
  {
    "symbol": "AUD",
    "description": "Australian Dollar"
  },
  {
    "symbol": "BGN",
    "description": "Bulgarian Lev"
  },
  {
    "symbol": "BRL",
    "description": "Brazilian Real"
  }
]
```

---

## 3. Personalization Note – Spread Factor

- **GitHub username:** `hanifnfl097`
- Lowercase username string: `"hanifnfl097"`

### 3.1. Sum of Unicode (ASCII) Values

Character breakdown:

| Char | ASCII |
|------|-------|
| h    | 104   |
| a    | 97    |
| n    | 110   |
| i    | 105   |
| f    | 102   |
| n    | 110   |
| f    | 102   |
| l    | 108   |
| 0    | 48    |
| 9    | 57    |
| 7    | 55    |

**Total Sum:**

```text
104 + 97 + 110 + 105 + 102 + 110 + 102 + 108 + 48 + 57 + 55 = 998
```

### 3.2. Spread Factor Derivation

Formula from the assignment:

```text
Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
              = (998 % 1000) / 100000.0
              = 998 / 100000.0
              = 0.00998
```

So the **exact Spread Factor** used in this application is:

```text
Spread Factor = 0.00998
```

### 3.3. USD_BuySpread_IDR Calculation

Given:

```text
Rate_USD = rate from /latest?base=IDR (e.g. 0.000064)
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
                  = (1 / Rate_USD) * (1 + 0.00998)
```

This value is pre-computed at startup inside the `latest_idr_rates` strategy and exposed as field:

```json
"usdBuySpreadIdr": 15780.9375         // example value depending on Rate_USD
```

---

## 4. 🛠️ Architectural Rationale

### 4.1. Polymorphism Justification – Strategy Pattern

The application must handle three different resource types via a **single endpoint**:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Instead of using `if/else` or `switch` blocks in the controller or service, the project uses the **Strategy Pattern** with:

- A shared interface (e.g. `IdrDataFetcher`) that exposes `loadData()` and `getResourceType()`.
- Three concrete implementations:
    - `LatestIdrRatesFetcher`
    - `HistoricalIdrUsdFetcher`
    - `SupportedCurrenciesFetcher`
- A map-based registry: `Map<String, IdrDataFetcher>` injected by Spring.

**Why Strategy Pattern instead of `if/else`?**

1. **Extensibility**  
   If a new resource type is added (e.g. `idr_to_jpy_timeseries`), we just create a new `IdrDataFetcher` implementation and register it.  
   The controller code does not need to change — it simply selects the strategy by key.

2. **Separation of concerns**  
   Each strategy encapsulates:
    - The external endpoint it calls.
    - How it transforms and normalizes the data.
    - Any resource-specific business logic.  
      This keeps each class smaller, clearer, and easier to test.

3. **Maintainability & testability**  
   Unit tests can focus on individual strategies without needing to set up combinations of condition branches.  
   The strategy map lookup is declarative and avoids a long `switch` statement that tends to grow and become fragile over time.

In short, the Strategy Pattern provides **polymorphism** for resource-type–specific behavior, improving clarity, scalability, and testability of the API.

---

### 4.2. Client Factory – Why `FactoryBean` for WebClient?

The application uses **Spring’s `FactoryBean<WebClient>`** to construct the HTTP client for Frankfurter.

**Responsibilities of the FactoryBean:**

- Read configuration (`frankfurter.base-url`) via `@Value` or `@ConfigurationProperties`.
- Build a shared `WebClient` instance with:
    - Base URL preconfigured.
    - Common settings (e.g. timeouts, codecs, headers) in one place.
- Expose the created `WebClient` as a normal bean for injection into strategies.

**Why use `FactoryBean` instead of a simple `@Bean` method?**

1. **Encapsulated construction logic**  
   `FactoryBean` is designed specifically to encapsulate complex construction logic for a bean.  
   If the client creation grows (e.g., conditional configuration, environment-specific setup, advanced customization), that complexity stays inside the factory class and does not spill into general configuration.

2. **Clear ownership & single responsibility**  
   The factory class becomes the **single owner** of the client configuration for the Frankfurter API.  
   Strategies and services simply depend on `WebClient`, not on how it is built.

3. **Better alignment with the assignment’s intention**  
   The requirement explicitly asks for `FactoryBean<T>` to verify understanding of:
    - Spring bean lifecycle.
    - Advanced configuration mechanisms beyond simple `@Bean` definitions.
      Using `FactoryBean` demonstrates that the client is a **first-class, configurable component** with its own lifecycle concerns.

Overall, the `FactoryBean` provides a clean, flexible, and testable way to manage the external HTTP client, especially in a finance/production context where client configuration often evolves over time.

---

### 4.3. Startup Runner Choice – Why `ApplicationRunner` Instead of `@PostConstruct`?

The initial data ingestion (loading all three resources and caching them) is done using **`ApplicationRunner`**.

**Why `ApplicationRunner` is preferred over `@PostConstruct`:**

1. **Correct startup order**  
   `ApplicationRunner` runs **after** the full Spring context is started and all beans are fully initialized.  
   This ensures:
    - The `WebClient` bean from the FactoryBean is ready.
    - All Strategy beans and the in-memory cache/store bean are available.

   `@PostConstruct` runs earlier in the bean lifecycle and can easily be triggered before the full context is ready, leading to subtle initialization issues.

2. **Alignment with application lifecycle**  
   With `ApplicationRunner`, data loading is clearly tied to the application startup phase.  
   It is easy to reason about and easy to adjust (e.g. disable, delay, or parallelize) if in the future the loading phase becomes more complex.

3. **Testability**  
   Integration tests can:
    - Boot the context.
    - Assert that the `ApplicationRunner` has populated the cache before serving any HTTP requests.

   This pattern is easier to confirm and control compared to `@PostConstruct`, which fires automatically on bean construction.

4. **Clean separation of responsibilities**
    - Beans remain focused on business logic and state holding.
    - The `ApplicationRunner` class explicitly owns the startup loading workflow.

In short, `ApplicationRunner` gives **better control, clarity, and reliability** for initialization of an immutable in-memory cache in a production-style Spring Boot application.

---

## 5. Summary

- External calls use `WebClient` with:
    - Timeouts
    - Error handling to map 4xx/5xx or network errors into controlled exceptions.
- The `ApplicationRunner` logs failures when fetching data and prevents partial/uncontrolled state.
- All responses are served from an in-memory cache, ensuring:
    - Thread-safe reads.
    - No external dependency latency in the request path.
- Strategy classes and utility classes are unit-tested to validate:
    - Transformation logic.
    - Spread factor calculation.

---
