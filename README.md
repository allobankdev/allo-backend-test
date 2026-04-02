# IDR Exchange Rate Aggregator API

A Spring Boot REST API that aggregates Indonesian Rupiah (IDR) exchange rate data from the public [Frankfurter API](https://api.frankfurter.app), demonstrating the Strategy Pattern, FactoryBean, and ApplicationRunner startup ingestion.

---

## 👤 Personalization Note

| Field | Value |
|---|---|
| **GitHub Username** | `johndoe47` |
| **ASCII Sum** | `j(106)+o(111)+h(104)+n(110)+d(100)+o(111)+e(101)+4(52)+7(55)` = **850** |
| **Spread Factor** | `(850 % 1000) / 100_000.0` = **0.00850** |
| **Formula** | `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00850)` |

> **To use your own username**: change `frankfurter.github-username` in `application.yml`. The spread factor is recalculated automatically on startup.

---

## 🏗️ Project Structure

```
src/main/java/com/example/idrapi/
├── IdrApiApplication.java              # Entry point
├── config/
│   ├── FrankfurterProperties.java      # @ConfigurationProperties
│   └── FrankfurterWebClientFactory.java# FactoryBean<WebClient> (Constraint B)
├── controller/
│   ├── FinanceDataController.java      # Single REST endpoint (zero if/else)
│   ├── GlobalExceptionHandler.java     # @RestControllerAdvice
│   └── ResourceNotFoundException.java  # Custom 404 exception
├── dto/
│   ├── LatestRatesResponse.java
│   └── HistoricalRatesResponse.java
├── model/
│   ├── FinanceDataResponse.java        # Immutable record (Java 16+)
│   └── ErrorResponse.java             # Immutable error envelope
├── runner/
│   └── FinanceDataStartupRunner.java   # ApplicationRunner (Constraint C)
├── service/
│   ├── FinanceDataService.java         # Strategy registry + orchestration
│   └── FinanceDataStore.java           # Thread-safe, sealable in-memory store
└── strategy/
    ├── IDRDataFetcher.java             # Strategy interface (Constraint A)
    └── impl/
        ├── LatestIDRRatesFetcher.java  # Strategy 1: /latest?base=IDR + spread
        ├── HistoricalIDRUSDFetcher.java# Strategy 2: time-series IDR/USD
        └── SupportedCurrenciesFetcher.java # Strategy 3: /currencies
```

---

## ⚙️ Prerequisites

- **Java 17+**
- **Maven 3.8+**
- Internet access to `api.frankfurter.app` (only needed at startup)

---

## 🚀 Setup & Run

### 1. Clone
```bash
git clone https://github.com/<your-username>/idr-api.git
cd idr-api
```

### 2. Configure (optional)
Edit `src/main/resources/application.yml` to change the GitHub username or date range:
```yaml
frankfurter:
  base-url: https://api.frankfurter.app
  github-username: johndoe47       # ← change to YOUR GitHub username
  historical:
    start-date: 2024-01-01
    end-date: 2024-01-05
```

### 3. Build
```bash
mvn clean package -DskipTests
```

### 4. Run
```bash
mvn spring-boot:run
# OR
java -jar target/idr-api-1.0.0.jar
```

The application starts on **http://localhost:8080**.  
On startup, all three resources are fetched from Frankfurter and loaded into memory. The API is ready to serve immediately after the `ApplicationRunner` completes.

---

## 🧪 Run Tests

```bash
# All tests
mvn test

# Unit tests only
mvn test -Dtest="*FetcherTest"

# Integration tests only
mvn test -Dtest="*IntegrationTest,*ControllerTest"
```

---

## 🌐 Endpoint Usage

```
GET /api/finance/data/{resourceType}
```

### Resource Types

| `{resourceType}` | Description |
|---|---|
| `latest_idr_rates` | Latest exchange rates with base=IDR + USD buy spread |
| `historical_idr_usd` | IDR→USD daily rates from 2024-01-01 to 2024-01-05 |
| `supported_currencies` | All currencies supported by Frankfurter API |

---

### cURL Examples

#### 1. Latest IDR Rates (includes `USD_BuySpread_IDR`)
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates \
  -H "Accept: application/json" | jq .
```

**Sample Response:**
```json
{
  "resourceType": "latest_idr_rates",
  "fetchedAt": "2024-01-05T08:00:00Z",
  "results": [
    {
      "base": "IDR",
      "date": "2024-01-05",
      "rates": {
        "USD": 0.000064,
        "EUR": 0.000059,
        "SGD": 0.000086
      },
      "spreadFactor": 0.0085,
      "USD_BuySpread_IDR": 15687.23
    }
  ]
}
```

---

#### 2. Historical IDR/USD Rates
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd \
  -H "Accept: application/json" | jq .
```

**Sample Response:**
```json
{
  "resourceType": "historical_idr_usd",
  "fetchedAt": "2024-01-05T08:00:00Z",
  "results": [
    { "date": "2024-01-02", "base": "IDR", "startDate": "2024-01-01", "endDate": "2024-01-05", "USD": 0.000064 },
    { "date": "2024-01-03", "base": "IDR", "startDate": "2024-01-01", "endDate": "2024-01-05", "USD": 0.000065 },
    { "date": "2024-01-04", "base": "IDR", "startDate": "2024-01-01", "endDate": "2024-01-05", "USD": 0.000063 },
    { "date": "2024-01-05", "base": "IDR", "startDate": "2024-01-01", "endDate": "2024-01-05", "USD": 0.000066 }
  ]
}
```

---

#### 3. Supported Currencies
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies \
  -H "Accept: application/json" | jq .
```

**Sample Response:**
```json
{
  "resourceType": "supported_currencies",
  "fetchedAt": "2024-01-05T08:00:00Z",
  "results": [
    { "code": "USD", "name": "US Dollar" },
    { "code": "EUR", "name": "Euro" },
    { "code": "IDR", "name": "Indonesian Rupiah" }
  ]
}
```

---

#### 4. Unknown Resource Type (404)
```bash
curl -X GET http://localhost:8080/api/finance/data/invalid_type \
  -H "Accept: application/json" | jq .
```

**Response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource type 'invalid_type' not found. Valid types: [latest_idr_rates, historical_idr_usd, supported_currencies]",
  "path": "/api/finance/data/invalid_type",
  "timestamp": "2024-01-05T08:00:01Z"
}
```

---

## 🛠️ Architectural Rationale

### Polymorphism: Why Strategy Pattern over if/else?

The Strategy Pattern was chosen over a conditional block (`if/else` or `switch`) in the service layer for the following reasons:

**Extensibility (Open/Closed Principle):** Adding a new `resourceType` (e.g., `idr_to_gbp`) requires only writing a new class that implements `IDRDataFetcher` and annotating it with `@Component`. The controller, service, and data store require **zero changes**. With a `switch` block, every new resource type means modifying existing, tested code — increasing regression risk.

**Maintainability:** Each concrete strategy class (`LatestIDRRatesFetcher`, `HistoricalIDRUSDFetcher`, `SupportedCurrenciesFetcher`) is a single-responsibility unit. It is independently readable, testable, and deployable. A monolithic conditional block mixes unrelated fetching and transformation logic in one place, making it harder to read and test.

**Spring's Auto-Discovery:** Spring automatically discovers all `IDRDataFetcher` beans and injects them as a `List<IDRDataFetcher>` into `FinanceDataService`. The service indexes them by `getResourceType()` key into a `Map`, enabling O(1) dispatch. This is idiomatic Spring — no manual registry maintenance required.

**Testability:** Each strategy can be unit-tested in complete isolation with a mocked `WebClient`, without starting a Spring context.

---

### Client Factory: Why FactoryBean over @Bean?

`FrankfurterWebClientFactory` implements `FactoryBean<WebClient>` rather than defining WebClient as a `@Bean` method in a `@Configuration` class. The key benefits:

**Encapsulation of Construction Logic:** The factory class is the sole owner of all WebClient construction concerns — base URL, timeouts, shared headers, logging filters. A `@Bean` method typically lives inside a broader `@Configuration` class, diluting separation of concerns. The factory is a self-contained, single-purpose class.

**Spring Lifecycle Hooks:** `FactoryBean` integrates with Spring's full lifecycle. `isSingleton()` guarantees a single shared `WebClient` instance. Future enhancements (e.g., `afterPropertiesSet()` validation, prototype scoping) are cleanly supported by the interface contract without retrofitting.

**Validation at Startup:** The factory can validate required properties (e.g., null `baseUrl`) in its constructor or `getObject()` method, causing a fast, clear startup failure rather than a cryptic NullPointerException at the first HTTP call.

**Clarity:** Any developer who sees `@Autowired WebClient webClient` in the strategies immediately knows there is a dedicated factory responsible for its construction — making the codebase easier to navigate.

---

### Startup Runner: Why ApplicationRunner over @PostConstruct?

`FinanceDataStartupRunner` implements `ApplicationRunner` rather than using `@PostConstruct` on a service method. The key justifications:

**Full Context Readiness:** `ApplicationRunner.run()` is invoked **after** the entire `ApplicationContext` is fully refreshed and all beans are wired and ready. `@PostConstruct` fires during the bean initialization phase — before the context is fully ready. If `WebClient` (or any transitive dependency) has not completed initialization, outbound HTTP calls inside `@PostConstruct` can fail non-deterministically.

**Clean Test Isolation:** `ApplicationRunner` can be excluded from specific test slices (e.g., `@WebMvcTest`) without special configuration. `@PostConstruct` fires unconditionally whenever the annotated bean is created, making it harder to avoid in tests that don't need network calls.

**ApplicationArguments Support:** `ApplicationRunner` receives parsed `ApplicationArguments`, enabling future CLI-driven behavior (e.g., `--dry-run`, `--skip-prefetch`) without changing the runner's internal logic.

**Failure Propagation:** Exceptions thrown from `ApplicationRunner.run()` propagate through Spring Boot's startup mechanism, causing a clean application exit with a visible error. This prevents the application from starting in a silently broken, data-less state.

---

## 📦 Technologies

| Technology | Version | Purpose |
|---|---|---|
| Spring Boot | 3.3.0 | Application framework |
| Spring WebFlux / WebClient | 6.x | Reactive HTTP client |
| Project Lombok | latest | Boilerplate reduction |
| JUnit 5 + Mockito | latest | Unit & integration testing |
| AssertJ | latest | Fluent test assertions |
| Java Records | Java 17 | Immutable response models |
