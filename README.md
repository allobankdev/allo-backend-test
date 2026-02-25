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
