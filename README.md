# IDR Rate Aggregator – Frankfurter API (Spring Boot)

This project implements a polymorphic, strategy-based Spring Boot service
that aggregates data from the public **Frankfurter Exchange Rate API**.

It integrates three resource types:

1. **Latest IDR Rates** (`/latest?base=IDR`)
2. **Historical Time Series (IDR → USD)** (`/2024-01-01..2024-01-05?from=IDR&to=USD`)
3. **Supported Currencies** (`/currencies`)

All data is fetched **once at application startup** using an `ApplicationRunner`
and stored in an **immutable in-memory store** for high-performance reads.

A single endpoint is exposed:

```http
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` is one of:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

---

# 🔧 1. Setup & Run Instructions

## Prerequisites

- Java **17**
- Maven **3.9+**
- Internet access (Frankfurter API is public / no API key required)

## Clone the Repository

```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```

## Build the Application

```bash
mvn clean package
```

## Run Application

```bash
mvn spring-boot:run
```

The API will start at:

```text
http://localhost:8080
```

---

# 🧪 2. Running Tests (Unit + Integration)

### Run ALL tests

```bash
mvn clean test
```

### Run only tests without rebuilding

```bash
mvn test
```

### Run a single test class

```bash
mvn -Dtest=LatestIdrRatesFetcherTest test
```

Mocking of external API calls is implemented using **Mockito**, ensuring tests
do not hit the real Frankfurter API.

---

# 🌐 3. API Usage — Example cURL Commands

### 1. Latest IDR Rates (with personalized spread)

```bash
curl ``http://localhost:8080/api/finance/data/latest_idr_rates``
```

### 2. Historical IDR → USD Time Series

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Supported Currency List

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

# 👤 4. Personalization Note (Spread Factor)

The assignment requires personalization using your **GitHub username**.

Your GitHub username:

```text
thasyalarasuci
```

## Spread Factor Calculation

### Step 1 — Sum of Unicode values

```text
t  h  a  s  y  a  l  a  r  a  s  u  c  i
116 104 97 115 121 97 108 97 114 97 115 117 99 105
```

### Step 2 — Apply formula

```text
spreadFactor = (1502 % 1000) / 100000.0
             = 502 / 100000
             = 0.00502
```

### Step 3 — Final computed value

This factor is injected into:

```text
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00502)
```

---

# 🏗 5. Architectural Rationale (Required Section)

## A. Polymorphism Justification — Strategy Pattern

The `Strategy Pattern` is used to handle the three resource types:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Instead of using a large `if/else` or `switch` block, each resource is handled by a
dedicated class implementing a shared interface:

```text
IDRDataFetcher
 ├── LatestIdrRatesFetcher
 ├── HistoricalIdrUsdFetcher
 └── SupportedCurrenciesFetcher
```

**Benefits:**

- High **extensibility** — adding a new resource only requires a new strategy class.
- Improved **maintainability** — each class handles one job.
- Cleaner testing — each strategy is unit-tested independently with mocked clients.
- Controller stays clean: no conditional branching, uses a **map-based strategy registry**.

This makes the codebase more modular and production-ready.

---

## B. Client Factory — Why FactoryBean Instead of @Bean

A custom `FactoryBean<WebClient>` is used to construct the external API client.

**Reasons:**

- **Centralized configuration**
  - Base URL comes from `application.properties`.
  - Future configuration (timeouts, headers, interceptors) stays in one place.
- **Dynamic instantiation logic**
  - `FactoryBean` allows fine-grained control over `WebClient` creation.
  - Prevents duplication and inconsistent configurations.
- **Better encapsulation**
  - Hides construction logic from other components.
  - Complies with the assignment’s architectural requirement:
    > “You may not define the client using a simple @Bean method.”

Using `FactoryBean` ensures a clean separation between configuration and logic.

---

## C. Startup Runner Choice — Why ApplicationRunner Over @PostConstruct

`ApplicationRunner` is used to fetch all 3 external resources on startup.

**Reasons:**

- **Guaranteed execution order**  
  Runs *after* the Spring context has fully initialized, ensuring dependencies are ready.

- **Testability**
  - Unlike `@PostConstruct`, `ApplicationRunner.run()` can be directly invoked in tests.
  - Enables mocking of external calls before the store is initialized.

- **Cleaner architecture**
  - Startup logic is isolated in a dedicated runner, not mixed into random bean lifecycle methods.

- **Supports assignment requirement:**

  > “Data must be loaded exactly once at startup into an immutable in-memory store.”

---

# 📦 6. Project Structure (Summary)

```text
src/main/java
 ├── config/
 ├── client/
 ├── strategy/
 ├── store/
 ├── runner/
 ├── controller/

src/test/java
 ├── strategy/   (3 unit tests)
 └── runner/     (1 integration-style test)
```

---

# 🎉 7. Conclusion

This project follows all required architectural constraints:

- Strategy Pattern for polymorphic behavior  
- FactoryBean for WebClient creation  
- ApplicationRunner for startup data loading  
- Immutable, thread-safe in-memory store  
- Comprehensive test coverage using Mockito  

And the single endpoint delivers consistent, production-grade aggregated data.

If you have any questions or want improvements (e.g., diagrams, formatting updates), feel free to ask! 😊💙
