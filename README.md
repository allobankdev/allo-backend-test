# Allo Bank — IDR/USD Finance Data API

A Spring Boot REST API that fetches Indonesian Rupiah (IDR) exchange rate data from the [Frankfurter API](https://www.frankfurter.app/). It pulls **latest rates**, **historical data**, and **supported currencies** on startup, caches them in memory, and serves them through a single unified endpoint.

---

## Personalization

| Field            | Value                                                        |
| ---------------- | ------------------------------------------------------------ |
| GitHub Username  | `yoelngl`                                                    |
| Unicode Sum      | y(121) + o(111) + e(101) + l(108) + n(110) + g(103) + l(108) = **762** |
| Spread Factor    | (762 % 1000) / 100000.0 = **0.00762**                       |

---

## Prerequisites

Before you start, make sure you have the following installed on your machine:

- **Java 17** — this project requires Java 17 or later. You can check your version by running:

  ```bash
  java -version
  ```

  If it doesn't say version 17, you can install it using [SDKMAN](https://sdkman.io/):

  ```bash
  curl -s "https://get.sdkman.io" | bash
  source "$HOME/.sdkman/bin/sdkman-init.sh"
  sdk install java 17.0.0-tem
  sdk use java 17.0.0-tem
  ```

- **Git** — to clone the repository.

- **Postman** (optional) — to test the API endpoints interactively. You can also use `curl` from your terminal.

That's all you need. The project ships with Gradle Wrapper, so you don't need to install Gradle separately.

---

## Installation & Setup

**Step 1 — Clone the repository**

```bash
git clone https://github.com/yoelngl/allo-backend-test.git
cd allo-backend-test
```

**Step 2 — Make sure you're using Java 17**

If you installed Java through SDKMAN:

```bash
sdk use java 17.0.0-tem
```

Verify it:

```bash
java -version
# Should show: openjdk version "17..."
```

**Step 3 — Build the project**

On Linux or Mac:

```bash
./gradlew clean build
```

On Windows:

```bash
gradlew.bat clean build
```

This will compile the code, run all the tests, and produce a runnable JAR. You should see `BUILD SUCCESSFUL` at the end.

**Step 4 — Start the application**

```bash
./gradlew bootRun
```

Wait until you see something like:

```
Started Application in 2.x seconds
```

The app is now running at **http://localhost:8080**. On startup, it automatically fetches all three data resources from the Frankfurter API and caches them in memory.

---

## API Endpoint

There is one endpoint:

```
GET /api/finance/data/{resourceType}
```

Where `{resourceType}` is one of:

| Resource Type         | What it returns                                              |
| --------------------- | ------------------------------------------------------------ |
| `latest_idr_rates`    | Latest exchange rates with base IDR, including the calculated `USD_BuySpread_IDR` field |
| `historical_idr_usd`  | Historical IDR to USD rates (default range: 2024-01-01 to 2024-01-05) |
| `supported_currencies`| Full list of currencies supported by the Frankfurter API     |

The `historical_idr_usd` endpoint also accepts optional query parameters to fetch a custom date range:

```
GET /api/finance/data/historical_idr_usd?startDate=2024-06-01&endDate=2024-06-30
```

---

## Testing with Postman

Here's how to test each endpoint step by step using Postman.

### 1. Get Latest IDR Rates

1. Open Postman and click **New Request**
2. Set the method to **GET**
3. Enter this URL:
   ```
   http://localhost:8080/api/finance/data/latest_idr_rates
   ```
4. Click **Send**
5. You should get a `200 OK` response with a JSON body like this:

```json
[
  {
    "resourceType": "latest_idr_rates",
    "data": {
      "amount": 1,
      "base": "IDR",
      "date": "2026-03-06",
      "rates": {
        "AUD": 0.000097,
        "USD": 0.000061,
        "EUR": 0.000057
      },
      "USD_BuySpread_IDR": 16502.46,
      "spread_factor": 0.00762,
      "github_username": "yoelngl"
    }
  }
]
```

Notice that `USD_BuySpread_IDR` is the calculated selling rate using the personal spread factor.

### 2. Get Historical IDR/USD Rates (Default Range)

1. Create a new GET request in Postman
2. Enter this URL:
   ```
   http://localhost:8080/api/finance/data/historical_idr_usd
   ```
3. Click **Send**
4. You should get a response like:

```json
[
  {
    "resourceType": "historical_idr_usd",
    "data": {
      "amount": 1,
      "base": "IDR",
      "start_date": "2024-01-01",
      "end_date": "2024-01-05",
      "rates": {
        "2024-01-02": { "USD": 0.000065 },
        "2024-01-03": { "USD": 0.000064 },
        "2024-01-04": { "USD": 0.000064 },
        "2024-01-05": { "USD": 0.000064 }
      }
    }
  }
]
```

### 3. Get Historical IDR/USD Rates (Custom Range)

1. Create a new GET request in Postman
2. Enter this URL:
   ```
   http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-06-01&endDate=2024-06-30
   ```
   Or, in Postman, go to the **Params** tab and add:
   - Key: `startDate`, Value: `2024-06-01`
   - Key: `endDate`, Value: `2024-06-30`
3. Click **Send**
4. You'll get historical rates for June 2024 (weekdays only — markets are closed on weekends/holidays)

### 4. Get Supported Currencies

1. Create a new GET request in Postman
2. Enter this URL:
   ```
   http://localhost:8080/api/finance/data/supported_currencies
   ```
3. Click **Send**
4. You'll see a list of all available currency codes:

```json
[
  {
    "resourceType": "supported_currencies",
    "data": {
      "AUD": "Australian Dollar",
      "BGN": "Bulgarian Lev",
      "BRL": "Brazilian Real",
      "IDR": "Indonesian Rupiah",
      "USD": "United States Dollar"
    }
  }
]
```

### 5. Unknown Resource Type (Error Case)

1. Try:
   ```
   http://localhost:8080/api/finance/data/something_invalid
   ```
2. You'll get a `404 Not Found`:

```json
{
  "error": "Unknown resource type: something_invalid"
}
```

---

## Testing with cURL

If you prefer the terminal over Postman:

```bash
# Latest IDR rates
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Historical IDR/USD (default range)
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Historical IDR/USD (custom range)
curl "http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-06-01&endDate=2024-06-30"

# Supported currencies
curl http://localhost:8080/api/finance/data/supported_currencies

# Unknown type (should return 404)
curl http://localhost:8080/api/finance/data/unknown
```

To get nicely formatted output, pipe through `jq`:

```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq .
```

---

## Running the Tests

This project has both **unit tests** and **integration tests**. All of them run automatically during the build, but you can also run them separately.

### Run All Tests

```bash
./gradlew test
```

This will execute every test and show `BUILD SUCCESSFUL` if they all pass.

### What Each Test File Does

| Test File | Type | What It Tests |
| --------- | ---- | ------------- |
| `LatestIDRRatesFetcherTest` | Unit | Spread factor calculation, enriched data with `USD_BuySpread_IDR`, error handling for null/failed responses |
| `HistoricalIDRUSDFetcherTest` | Unit | Default date range fetch, custom date range via `fetchByRange()`, error handling |
| `SupportedCurrenciesFetcherTest` | Unit | Currency map parsing, error handling |
| `FinanceDataControllerTest` | Unit | Controller returns correct HTTP status for valid types (200), unknown types (404), uninitialized store (503), and date range parameters |
| `DataLoadIntegrationTest` | Integration | Verifies the `ApplicationRunner` loads all three resources on startup, store is initialized, and all endpoints return 200 |
| `ApplicationTests` | Integration | Spring context loads successfully |

### Run a Specific Test Class

If you want to run just one test file:

```bash
./gradlew test --tests "com.allo.strategy.LatestIDRRatesFetcherTest"
./gradlew test --tests "com.allo.strategy.HistoricalIDRUSDFetcherTest"
./gradlew test --tests "com.allo.strategy.SupportedCurrenciesFetcherTest"
./gradlew test --tests "com.allo.controller.FinanceDataControllerTest"
./gradlew test --tests "com.allo.integration.DataLoadIntegrationTest"
```

### View the Test Report

After running tests, Gradle generates an HTML report at:

```
build/reports/tests/test/index.html
```

Open it in your browser to see a detailed breakdown of passed/failed tests.

---

## Project Structure

```
src/main/java/com/allo/
├── Application.java                  → Entry point
├── client/
│   └── RestTemplateFactoryBean.java  → Creates RestTemplate via FactoryBean
├── config/
│   └── FrankfurterApiProperties.java → Externalised API config (URL, timeouts)
├── controller/
│   └── FinanceDataController.java    → Single REST endpoint
├── dto/
│   └── FinanceResourceResponse.java  → Unified response record
├── exception/
│   ├── DataNotLoadedException.java   → 503 when store not ready
│   ├── ExternalApiException.java     → Wraps Frankfurter API failures
│   ├── GlobalExceptionHandler.java   → Maps exceptions to HTTP statuses
│   └── ResourceNotFoundException.java→ 404 for unknown resource types
├── runner/
│   └── DataLoadRunner.java           → ApplicationRunner that loads data on startup
├── service/
│   └── FinanceDataService.java       → Routes requests to cached store or live fetch
├── store/
│   └── FinanceDataStore.java         → Thread-safe, immutable in-memory cache
└── strategy/
    ├── IDRFetcher.java               → Strategy interface
    ├── LatestIDRRatesFetcher.java    → Fetches /latest?base=IDR + spread calc
    ├── HistoricalIDRUSDFetcher.java  → Fetches historical IDR/USD time series
    └── SupportedCurrenciesFetcher.java → Fetches /currencies

src/test/java/com/allo/
├── ApplicationTests.java
├── controller/
│   └── FinanceDataControllerTest.java
├── integration/
│   └── DataLoadIntegrationTest.java
└── strategy/
    ├── LatestIDRRatesFetcherTest.java
    ├── HistoricalIDRUSDFetcherTest.java
    └── SupportedCurrenciesFetcherTest.java
```

---

## Configuration

All external settings live in `src/main/resources/application.yml`:

```yaml
frankfurter:
  api:
    base-url: https://api.frankfurter.app
    connect-timeout: 5s
    read-timeout: 10s
```

You can override these via environment variables:

```bash
FRANKFURTER_API_BASE_URL=https://api.frankfurter.app ./gradlew bootRun
```

---

## Troubleshooting

| Problem | Solution |
| ------- | -------- |
| `Gradle requires JVM 17 or later` | Switch to Java 17: `sdk use java 17.0.0-tem` |
| `Connection refused` on localhost:8080 | Make sure the app is running (`./gradlew bootRun`) |
| Response shows empty data | The Frankfurter API might be temporarily down — try again in a minute |
| Historical dates return no rates | Weekends and holidays have no market data — use weekday ranges |
| `BUILD FAILED` during `./gradlew build` | Check `java -version` is 17, and that you have internet access (needed to download dependencies) |

---

## Architectural Rationale

### 1. Why the Strategy Pattern?

Instead of writing `if/else` or `switch` blocks in the service layer to handle different resource types, each resource type gets its own dedicated fetcher class that implements the `IDRFetcher` interface. This means:

- **Adding a new resource** is as simple as creating a new class that implements `IDRFetcher` — no existing code needs to change.
- **Each strategy is independently testable** with its own unit test file and mocked dependencies.
- **The controller stays clean** — it just does a map lookup by name, with zero conditional logic.

Spring automatically collects all `IDRFetcher` beans into a map keyed by their component name, so wiring is entirely handled by the framework.

### 2. Why FactoryBean for RestTemplate?

A `FactoryBean<RestTemplate>` gives us full control over how the `RestTemplate` instance is built — injecting the base URL, connect timeout, and read timeout from `@ConfigurationProperties` — while still registering the result as a regular Spring bean. This is preferable to a plain `@Bean` method because:

- The **creation logic is encapsulated** in its own component, separate from configuration classes.
- It naturally supports the single-responsibility principle — the factory only cares about building the HTTP client.
- Spring treats the produced `RestTemplate` as a first-class bean, injectable anywhere without extra wiring.

### 3. Why ApplicationRunner over @PostConstruct?

`ApplicationRunner` runs **after the entire Spring context is fully initialized**, which means all beans (including the `RestTemplate` from the `FactoryBean`) are guaranteed to be ready. In contrast, `@PostConstruct` runs during bean initialization, when other beans may not yet be fully wired. Additionally:

- `ApplicationRunner` gives access to application arguments if needed.
- It provides a clear, single entry point for startup logic that's easy to find and debug.
- Failure in the runner prevents the application from starting, which is the desired behavior — if we can't load data, the app shouldn't serve stale or empty responses.
