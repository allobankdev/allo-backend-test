# Allo Bank Backend Take-Home Test — Solution

A Spring Boot REST API that aggregates IDR exchange rate data from the [Frankfurter API](https://api.frankfurter.app), built with clean architecture and advanced Spring patterns.

---

## Personalization Note

- **GitHub Username:** `DoniOctopus`
- **Lowercase username:** `donioctopus`
- **ASCII sum:** `d(100)+o(111)+n(110)+i(105)+o(111)+c(99)+t(116)+o(111)+p(112)+u(117)+s(115)` = **1207**
- **Spread Factor:** `(1207 % 1000) / 100000.0` = **0.00207**
- **Formula:** `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00207)`

---

## Setup & Run Instructions

### Prerequisites
- Java 17+
- Maven 3.6+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/DoniOctopus/allo-backend-test.git
cd allo-backend-test

# Switch to feature branch
git checkout feat/idr-rate-aggregator

# Build
./mvnw clean package -DskipTests

# Run
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.
On startup, all three resources are fetched **once** from the Frankfurter API and stored in-memory. All subsequent requests are served from this immutable in-memory store.

### Run Tests

```bash
# Unit tests only
./mvnw test -Dtest="LatestIdrRatesStrategyTest,HistoricalIdrUsdStrategyTest,SupportedCurrenciesStrategyTest"

# All tests (integration tests require internet connection)
./mvnw test
```

---

## Endpoint Usage

### `GET /api/finance/data/{resourceType}`

#### 1. Latest IDR Rates (with USD Buy Spread)

```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

**Response:**
```json
[
  {
    "amount": 1.0,
    "base": "IDR",
    "date": "2024-01-05",
    "rates": {
      "AED": 0.00024,
      "USD": 0.000064,
      "EUR": 0.000059
    },
    "USD_BuySpread_IDR": 15657.34
  }
]
```

#### 2. Historical IDR/USD Rates

```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

**Response:**
```json
[
  { "date": "2024-01-01", "USD": 0.000064 },
  { "date": "2024-01-02", "USD": 0.000064 },
  { "date": "2024-01-03", "USD": 0.000064 },
  { "date": "2024-01-04", "USD": 0.000064 },
  { "date": "2024-01-05", "USD": 0.000064 }
]
```

#### 3. Supported Currencies

```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

**Response:**
```json
[
  { "code": "AED", "name": "United Arab Emirates Dirham" },
  { "code": "AFN", "name": "Afghan Afghani" },
  { "code": "IDR", "name": "Indonesian Rupiah" },
  { "code": "USD", "name": "United States Dollar" }
]
```

#### Error — Unknown Resource Type

```bash
curl -X GET http://localhost:8080/api/finance/data/unknown_type
```

**Response (404):**
```json
{
  "timestamp": "2024-01-05T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Unknown resource type: 'unknown_type'. Valid types are: latest_idr_rates, historical_idr_usd, supported_currencies"
}
```

---

## Architectural Rationale

### 1. Polymorphism Justification: Why Strategy Pattern?

The Strategy Pattern was chosen to handle the three distinct resource types instead of a conditional `if/else` or `switch` block for the following reasons:

**Extensibility (Open/Closed Principle):** Adding a new resource type (e.g., `forecast_idr_rates`) only requires creating a new class implementing `IDRDataFetcher` and annotating it with `@Component("new_resource_type")`. The controller and runner require **zero modification** — they are closed for modification but open for extension.

**Maintainability:** Each strategy is a self-contained unit with a single responsibility. The spread calculation logic for `latest_idr_rates` is fully encapsulated in `LatestIdrRatesStrategy`, completely isolated from the other two strategies. A conditional approach would bundle all three concerns into one bloated method.

**Testability:** Each strategy can be unit-tested independently with a mocked `WebClient`, producing fast, focused, and reliable tests.

**Spring Map Injection:** Spring automatically builds a `Map<String, IDRDataFetcher>` where the key is the bean name. This allows the controller to perform a clean O(1) map lookup, eliminating all conditional branching from the controller layer entirely.

---

### 2. Client Factory: Why FactoryBean over @Bean?

The `FrankfurterWebClientFactoryBean` implements Spring's `FactoryBean<WebClient>` interface for these reasons:

**Programmatic lifecycle control:** `FactoryBean` provides explicit lifecycle hooks (`getObject()`, `getObjectType()`, `isSingleton()`) for fine-grained control over how and when the object is created. `isSingleton()` returning `true` guarantees only one `WebClient` instance is ever created, which is critical for shared connection pool management.

**Separation of construction concerns:** The factory encapsulates all configuration details (base URL, headers, codec limits) in one dedicated class. A `@Bean` method in a generic `@Configuration` class is a passive definition holder — a `FactoryBean` is an active, identifiable factory with its own lifecycle.

**Spring SPI compliance:** `FactoryBean` is a first-class Spring SPI designed precisely for cases where bean creation requires complex initialization logic. Using it signals clearly to other developers that this is a non-trivial construction process.

**Externalizable configuration:** The factory injects `FrankfurterProperties` (bound via `@ConfigurationProperties`) to source the base URL, ensuring all external service configuration is centralized in `application.yml` and easily overridable per environment profile.

---

### 3. Startup Runner Choice: Why ApplicationRunner over @PostConstruct?

**Application readiness guarantee:** `ApplicationRunner.run()` is invoked after the entire application context is fully initialized and all beans are wired. `@PostConstruct` runs during bean initialization — the context may not be fully started, making it unsuitable for operations that depend on complete multi-bean wiring.

**Architectural clarity:** Placing startup logic in a dedicated `DataStartupRunner` component communicates explicit intent — this is application-level initialization, not bean-level construction. It keeps `DataStoreService` clean and focused on its storage responsibility.

**Ordering:** Multiple `ApplicationRunner` implementations can be ordered via `@Order`, enabling an explicit and controlled initialization sequence. `@PostConstruct` ordering across beans is implicit and harder to manage.

**Access to application arguments:** `ApplicationRunner` receives `ApplicationArguments`, allowing the startup logic to be parameterized at runtime (e.g., forcing a data refresh via CLI flag) without code changes.

---

## Project Structure

```
src/main/java/com/allobank/finace/
├── AlloBackendTestApplication.java
├── config/
│   └── FrankfurterWebClientFactoryBean.java   # FactoryBean<WebClient> — Constraint B
├── controller/
│   └── FinanceController.java                  # Map-based strategy lookup — Constraint A
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── properties/
│   └── FrankfurterProperties.java              # @ConfigurationProperties
├── runner/
│   └── DataStartupRunner.java                  # ApplicationRunner — Constraint C
└── service/
    ├── DataStoreService.java                   # Thread-safe + immutable in-memory store
    └── strategy/
        ├── IDRDataFetcher.java                 # Strategy interface
        ├── LatestIdrRatesStrategy.java         # Calculates USD_BuySpread_IDR
        ├── HistoricalIdrUsdStrategy.java
        └── SupportedCurrenciesStrategy.java
```
