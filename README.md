# Allo Bank Backend Developer Take-Home Test Solution

This repository contains a Spring Boot implementation for the take-home test using Frankfurter API data and a single polymorphic endpoint.

## Stack

- Java 17
- Spring Boot 3.3.5
- Maven
- JUnit 5 + Mockito

## Setup and Run

1. Clone repository

```bash
git clone <your-fork-url>
cd allo-backend-test
```

2. Run application

```bash
mvn spring-boot:run
```

3. Run tests

```bash
mvn test
```

## Endpoint Usage

Base endpoint:

```text
GET /api/finance/data/{resourceType}
```

Supported `resourceType` values:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Example cURL commands:

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note

- GitHub username used: `intikom`
- ASCII sum: `763`
- Spread Factor formula: `(763 % 1000) / 100000.0`
- Spread Factor value: `0.00763`

`latest_idr_rates` includes additional field:

```text
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```

## Architecture Summary

- `IDRDataFetcher` strategy interface with 3 concrete implementations:
  - `LatestIdrRatesFetcher`
  - `HistoricalIdrUsdFetcher`
  - `SupportedCurrenciesFetcher`
- `IDRDataFetcherRegistry` builds a Spring-injected map for strategy lookup.
- `FinanceDataPreloadRunner` (`ApplicationRunner`) fetches all three resources exactly once at startup.
- `FinanceDataStore` keeps immutable in-memory data and is initialized once using an `AtomicReference`.
- `RestTemplateFactoryBean` creates the Frankfurter `RestTemplate` client via `FactoryBean<T>`.
- `FinanceDataController` serves only in-memory data from `GET /api/finance/data/{resourceType}`.

## Architectural Rationale

1. Polymorphism Justification

The Strategy Pattern isolates each resource-fetching behavior into its own class and avoids conditional growth in the controller/service. When a new resource type is added, we only add a new strategy class and register it via Spring, which improves extensibility and keeps existing code stable and maintainable.

2. Client Factory

`FactoryBean<RestTemplate>` centralizes creation/configuration (root URL, timeouts) in one dedicated component. This gives explicit lifecycle/creation control and cleanly separates client construction concern from business logic classes, while satisfying the requirement to avoid simple `@Bean` client creation.

3. Startup Runner Choice

`ApplicationRunner` is a better fit than `@PostConstruct` because it executes after full context initialization and dependency wiring. This is safer for startup orchestration, clearer for operational intent, and easier to test in integration tests.

## Configuration

`src/main/resources/application.yml`:

```yaml
frankfurter:
  api:
    base-url: https://api.frankfurter.app
    historical-range: 2024-01-01..2024-01-05
    github-username: joniheri
    connect-timeout-millis: 5000
    read-timeout-millis: 5000
```

Update `github-username` if needed; spread factor will automatically follow this value.
