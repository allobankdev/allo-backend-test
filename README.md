# API Integration - Frankfurter (Spring Boot 4, Java 17)

## Setup and Run

1. Clone repository.
2. Ensure Java 17 is installed.
3. Build and run tests:

```bash
./mvnw test
```

4. Run app:

```bash
./mvnw spring-boot:run
```

## Configuration

Main config is in `src/main/resources/application.yaml`.

```yaml
external:
  frankfurter:
    base-url: https://api.frankfurter.app
    connect-timeout-millis: 3000
    read-timeout-millis: 5000
    user-agent: ApiIntegration/1.0

app:
  github-username: AmriGIT
```

## Endpoint Usage

Single exposed endpoint:

- `GET /api/finance/data/{resourceType}`

Allowed `resourceType` values:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Example cURL:

```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates
curl -s http://localhost:8080/api/finance/data/historical_idr_usd
curl -s http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note

Configured GitLab username: `Amri93`

Spread calculation:

- Lowercase username: `amri93`
- Unicode sum: `533`
- Spread Factor = `(533 % 1000) / 100000.0 = 0.00533`

Used formula for `latest_idr_rates`:

- `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)`

## Architectural Rationale

### Polymorphism Justification (Strategy Pattern)

`IDRDataFetcher` defines one contract (`resourceType`, `fetch`) and each resource has its own concrete strategy:

- `LatestIdrRatesFetcher`
- `HistoricalIdrUsdFetcher`
- `SupportedCurrenciesFetcher`

Controller uses Spring-injected `Map<String, IDRDataFetcher>` lookup, so adding a new resource only requires adding a new strategy bean. This removes branching complexity from controller/service and improves maintainability.

### Client Factory (FactoryBean)

External client is created through `FrankfurterRestClientFactoryBean implements FactoryBean<RestClient>`.

Benefits:

- Centralized client construction with base URL, timeout, and default header configuration.
- Clean lifecycle/singleton handling for one shared external client instance.
- Better separation than scattering client setup across services.

### Startup Runner Choice (ApplicationRunner)

`FinanceDataLoaderRunner` fetches all three resources exactly once at startup and stores them in `FinanceDataInMemoryStore`.

Why `ApplicationRunner` over `@PostConstruct`:

- Runs after the full application context is ready (all strategy beans available).
- Better control for startup orchestration and testability.
- Explicit startup ingestion phase, separated from bean initialization concerns.

## Data Flow Summary

1. Startup: `ApplicationRunner` executes all `IDRDataFetcher` strategies.
2. Data is stored once in immutable in-memory map (`Map.copyOf`) in `FinanceDataInMemoryStore`.
3. Request handling only reads from memory; no per-request external API call.

## Testing Coverage

- Unit tests:
  - `LatestIdrRatesFetcherTest`
  - `HistoricalIdrUsdFetcherTest`
  - `SupportedCurrenciesFetcherTest`
- Integration test:
  - `FinanceDataLoaderRunnerIntegrationTest` verifies startup preloads all three resources into the in-memory store.
