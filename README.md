# Allo Bank IDR Rate Aggregator

A Spring Boot REST API that aggregates IDR exchange rate data from the [Frankfurter API](https://api.frankfurter.app/).

## Prerequisites

- Java 17+
- Maven 3.8+

## Setup & Run

```bash
git clone https://github.com/andhikakrstn3/allo-backend-test.git
cd allo-backend-test
mvn clean install
mvn spring-boot:run
```

### Run Tests Only

```bash
# Unit tests
mvn test

# Unit + Integration tests
mvn verify
```

## API Endpoints

### Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### Historical IDR/USD Rates
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

### Invalid Resource (404)
```bash
curl http://localhost:8080/api/finance/data/invalid
```

## Personalization Note

- **GitHub Username:** `andhikakrstn3`
- **ASCII Sum:** `1333`
- **Spread Factor:** `(1333 % 1000) / 100000.0 = 333 / 100000.0 = 0.00333`

The `USD_BuySpread_IDR` field is calculated using:

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00333)
```

---

## Architectural Rationale

### 1. Polymorphism Justification (Strategy Pattern)

The Strategy Pattern was chosen over a simpler `if/else` or `switch` block because each of the three data resources (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) requires fundamentally different API endpoints, response parsing, and transformation logic. By encapsulating each variation behind a common `IDRDataFetcher` interface, we achieve:

- **Extensibility (Open/Closed Principle):** Adding a new data resource requires only creating a new `IDRDataFetcher` implementation annotated with `@Component`. No existing code in the controller, runner, or other strategies needs to change. Spring automatically discovers and injects the new strategy.
- **Maintainability:** Each strategy class is self-contained and independently testable. A bug fix or logic change in one strategy (e.g., updating the spread calculation) has zero risk of impacting others.
- **Elimination of conditional logic:** The controller delegates to `DataStoreService` using a simple map lookup by `resourceType` string, with no manual branching. This keeps the controller thin and focused on HTTP concerns only.

### 2. Client Factory (FactoryBean)

`FrankfurterWebClientFactoryBean` implements `FactoryBean<WebClient>` to encapsulate the full construction and configuration of the `WebClient` instance (base URL, response timeout, default headers) within Spring's bean lifecycle. Benefits over a standard `@Bean` method:

- **Encapsulation of complex creation logic:** The `FactoryBean` pattern clearly separates the "how to build the client" concern into its own dedicated class, rather than embedding it in a generic `@Configuration` class alongside unrelated beans.
- **Transparent bean registration:** Spring treats the product of `getObject()` as the actual bean. Consumers inject `WebClient` directly without knowing a factory is involved, keeping client code clean.
- **Singleton management:** The `isSingleton()` method ensures Spring caches and reuses the same `WebClient` instance across all strategy classes, avoiding redundant allocations.

### 3. Startup Runner Choice (ApplicationRunner)

`DataInitializationRunner` implements `ApplicationRunner` to fetch and cache all exchange rate data at startup, before any HTTP request is served. Advantages over `@PostConstruct`:

- **Full Spring context guarantee:** `ApplicationRunner` executes after the entire application context is fully initialized, including all beans, `FactoryBean` products, and configuration properties. `@PostConstruct` runs during bean initialization, where dependent beans may not yet be ready.
- **Fail-fast behavior:** If the Frankfurter API is unreachable, the application fails immediately at startup with a clear error, rather than silently starting and serving empty or error responses.
- **Immediate responsiveness:** Since data is pre-loaded into an immutable in-memory store, every API request is served instantly from memory with no cold-start latency on the first request.
- **Access to `ApplicationArguments`:** Unlike `@PostConstruct`, the `ApplicationRunner` interface provides access to command-line arguments, enabling future flexibility (e.g., passing a custom date range or enabling/disabling specific fetchers).

## Project Structure

```
src/main/java/com/allobank/idrrates/
├── AlloIdrRatesApplication.java          # Main application entry point
├── config/
│   └── FrankfurterWebClientFactoryBean.java  # FactoryBean<WebClient>
├── controller/
│   └── FinanceController.java            # GET /api/finance/data/{resourceType}
├── dto/
│   ├── ApiResponse.java                  # Unified response wrapper
│   ├── LatestRateItem.java
│   ├── HistoricalRateItem.java
│   └── CurrencyItem.java
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java       # @ControllerAdvice (404, 503, 500)
├── runner/
│   └── DataInitializationRunner.java     # ApplicationRunner
├── service/
│   └── DataStoreService.java            # Thread-safe immutable store
├── strategy/
│   ├── IDRDataFetcher.java              # Strategy interface
│   ├── LatestIDRRatesFetcher.java
│   ├── HistoricalIDRUSDFetcher.java
│   └── SupportedCurrenciesFetcher.java
└── util/
    └── SpreadFactorCalculator.java
```
