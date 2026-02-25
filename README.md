# Allo Bank Finance API

Spring Boot application for aggregating Indonesian Rupiah (IDR) exchange rate data from Frankfurter API.

## Quick Start

### Prerequisites

- Java 17+ (Java 21 recommended)
- Maven 3.6+

### Build & Run

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test
```

Application starts on `http://localhost:8080` and fetches all data on startup.

## API Usage

Single endpoint: `GET /api/finance/data/{resourceType}`

### 1. Latest IDR Rates

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

Response includes `USD_BuySpread_IDR` calculated with unique spread factor.

### 2. Historical IDR-USD (2024-01-01 to 2024-01-05)

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization

**GitHub Username:** `manzoy`

**Spread Factor Calculation:**

```
m(109) + a(97) + n(110) + z(122) + o(111) + y(121) = 670
Spread Factor = (670 % 1000) / 100000.0 = 0.00670
```

Formula: `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00670)`

## Architecture

```
com.allobank.finance/
├── cache/         - Thread-safe immutable data cache (AtomicBoolean)
├── client/model/  - External API DTOs
├── config/        - FactoryBean for RestClient + Properties
├── controller/    - REST endpoint
├── exception/     - ErrorCode enum + BaseException
├── handler/       - GlobalExceptionHandler
├── model/         - Sealed interface + Records (Java 21)
├── registry/      - Strategy registry
├── runner/        - ApplicationRunner for startup init
├── strategy/      - Strategy Pattern (3 implementations)
└── support/       - SpreadCalculator utility
```

### Key Patterns

**Strategy Pattern:** `IDRDataFetcher` interface with 3 implementations

- `LatestIDRRatesFetcher` - Latest rates + spread calculation
- `HistoricalIDRUSDFetcher` - Historical IDR-USD data
- `SupportedCurrenciesFetcher` - Currency list

**FactoryBean:** `FrankfurterRestClient` creates RestClient with timeout config

**ApplicationRunner:** `DataInitializationRunner` loads all data once at startup (fail-fast)

**Immutable Cache:** Thread-safe with `ConcurrentHashMap` + `AtomicBoolean`

## Configuration

Edit `src/main/resources/application.yml`:

```yaml
finance:
  api:
    base-url: https://api.frankfurter.app
    connect-timeout: 5000
    read-timeout: 600000
  github:
    username: manzoy
```

## Architectural Rationale

### 1. Polymorphism Justification: Strategy Pattern vs Conditional Logic

The Strategy Pattern was chosen for extensibility and maintainability:

- Open/Closed Principle: Adding new resource types only requires creating a new strategy class—no modification to
  existing code
- Separation of Concerns: Each strategy encapsulates its own logic (API calls, transformations, spread calculations) in
  isolated classes
- Testability: Strategies can be unit tested independently without complex conditional mocking
- Scalability: The controller remains clean with map-based lookup. With conditionals, adding more resource types would
  lead to unmaintainable nested if/else blocks

Example: Adding a fourth resource type requires only a new @Component implementing IDRDataFetcher, automatically
registered via Spring DI.

### 2. Client Factory: FactoryBean vs Standard @Bean

FactoryBean provides better encapsulation and lifecycle control:

- Configuration Isolation: All client construction logic (base URL, timeouts, request factory) lives in a dedicated
  component
- Lifecycle Hooks: Explicit control via `getObject()`, `isSingleton()`, and `getObjectType()`
- Single Responsibility: Configuration classes focus on property binding; client construction has its own component
- Reusability: Easy to create additional FactoryBeans for other external APIs without cluttering configuration classes

A standard `@Bean` method would mix client construction with configuration, violating SRP and making the code harder to
maintain as the application grows.

### 3. Startup Runner Choice: ApplicationRunner vs @PostConstruct

ApplicationRunner ensures proper initialization timing and fail-fast behavior:

- Lifecycle Guarantee: Runs after the full Spring context is initialized, ensuring all beans (registry, cache,
  RestClient) are ready
- Fail-Fast: Throws checked exceptions that prevent application startup if data fetching fails—avoiding invalid runtime
  state
- Application-Level Scope: Clearly signals this is an app initialization task, not bean construction
- Extensibility: Access to ApplicationArguments allows future CLI flags (e.g., --skip-init for testing)

With `@PostConstruct,` initialization happens during bean creation, risking partially initialized state if dependencies
aren't ready or if fetching fails silently.

---

## Tech Stack

- Spring Boot 3.5.9
- Java 21 (sealed interfaces, records)
- RestClient (Spring 6.1+)
- Lombok
- JUnit 5 + Mockito + AssertJ

## Testing

```bash
# All tests
./mvnw test

# Specific test
./mvnw test -Dtest=SpreadCalculatorTest
```

**Coverage:**

- Unit tests for all strategies
- Cache thread-safety tests
- Integration test for ApplicationRunner + full context

---

**Architectural rationale and detailed explanations:** See Pull Request description
