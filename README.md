# Allo Bank Backend Developer Take-Home Test

Spring Boot REST API for preloading and serving IDR-related exchange data from Frankfurter API using Strategy Pattern, custom `FactoryBean`, and startup data runner with immutable in-memory storage.

## Tech Stack

- Java 17
- Spring Boot 3.3.x
- Maven
- JUnit 5 + Mockito

## Setup and Run

1. Clone repository:

```bash
git clone <your-repo-url>
cd allobank-backend-test
```

2. Build application:

```bash
mvn clean package
```

3. Run application:

```bash
mvn spring-boot:run
```

4. Run tests:

```bash
mvn test
```

Application default port: `8080`

## API Endpoint

Single endpoint:

`GET /api/finance/data/{resourceType}`

Supported `resourceType` values:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Example cURL:

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

If unsupported `resourceType` is used, API returns `400 Bad Request`.

## Personalization Note

- GitHub username used: `joniheri`
- ASCII sum (lowercase username): `856`
- Spread factor formula:
  - `Spread Factor = (ASCII_SUM % 1000) / 100000.0`
  - `Spread Factor = (856 % 1000) / 100000.0 = 0.00856`

For `latest_idr_rates`, custom field is calculated as:

`USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread_Factor)`

## Architecture Summary

### Strategy Pattern

- Strategy interface: `IDRDataFetcher`
- Concrete strategies:
  - `LatestIdrRatesFetcher`
  - `HistoricalIdrUsdFetcher`
  - `SupportedCurrenciesFetcher`
- Strategy lookup uses Spring-injected map through `IDRDataFetcherRegistry`.
- Controller and service do not use manual `if/else` or `switch` branching for resource dispatch.

### Client Factory Bean

- External API client (`RestTemplate`) is created via custom `FactoryBean`:
  - `RestTemplateFactoryBean`
- Base URL and client settings are externalized in `application.yml` via `FrankfurterApiProperties`.

### Startup Runner and Immutability

- Data preload happens once on startup using `ApplicationRunner`:
  - `FinanceDataPreloadRunner`
- All three resources are fetched and saved into `FinanceDataStore`.
- `FinanceDataStore` uses `AtomicReference` and deep immutable copy to enforce thread safety and immutability after initialization.
- API serves only from in-memory store after startup preload.

## Architectural Rationale

1. Polymorphism Justification (Strategy Pattern)
The endpoint must serve three resource types with different upstream resources and transformation rules. Using `IDRDataFetcher` plus three concrete strategies keeps each behavior isolated and small. The controller/service layer only delegates by key through `IDRDataFetcherRegistry`, so adding a new resource type later only needs a new strategy class without touching existing branching logic. This improves extensibility and reduces regression risk when requirements grow.

2. Client Factory Justification (`FactoryBean`)
`RestTemplateFactoryBean` centralizes external client construction concerns: timeout setup and base URL wiring from `frankfurter.api.base-url`. The `RestTemplate` is configured with `DefaultUriBuilderFactory(baseUrl)`, so client code only uses relative paths (`/latest`, `/{range}`, `/currencies`). This keeps endpoint composition consistent and removes repeated base URL concatenation in business/client code.

3. Startup Runner Choice (`ApplicationRunner`)
`ApplicationRunner` is used so initial preload runs after Spring context and dependencies are fully ready, with clear startup lifecycle semantics and error handling options (`fail-fast`). Compared to `@PostConstruct`, runner-based initialization is easier to test, easier to control with properties, and cleaner for production startup orchestration.

## Error Handling

- Unsupported resource type -> `400`
- Data not initialized -> `503`
- Invalid numeric upstream values -> `400`
- Optional fallback payload on preload failure when `finance.preload.fail-fast=false`

## Test Coverage

- Unit tests for each strategy:
  - `LatestIdrRatesFetcherTest`
  - `HistoricalIdrUsdFetcherTest`
  - `SupportedCurrenciesFetcherTest`
- Service and store tests:
  - `FinanceDataServiceTest`
  - `FinanceDataStoreTest`
- Integration test for startup preloading:
  - `FinanceDataPreloadRunnerIntegrationTest`
