# Allo Bank Backend Take-Home Test

Production-ready Spring Boot service for the Allo Bank backend take-home test. The application preloads Indonesian Rupiah finance data from the public Frankfurter API at startup and exposes it through a single polymorphic endpoint.

## Tech Stack

- Java 17
- Spring Boot 3.3.5
- Spring WebFlux `WebClient`
- Maven
- JUnit 5 and Spring Boot Test

## Run Locally

```bash
mvn clean test
mvn spring-boot:run
```

The service starts on Spring Boot's default port, `8080`.

## Configuration

Runtime configuration lives in `src/main/resources/application.yml`.

```yaml
frankfurter:
  base-url: https://api.frankfurter.app/
  connect-timeout-ms: 5000
  read-timeout-ms: 5000

app:
  github-username: biascoder
```

## Endpoint Usage

Single endpoint:

```http
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

The endpoint serves data from an immutable in-memory store loaded once during application startup. It does not call the Frankfurter API during request handling.

## Spread Factor

Calculation:

- Lowercase username: `misterjoko`
- Unicode sum: `98 + 105 + 97 + 115 + 99 + 111 + 100 + 101 + 114 = 940`
- Spread factor: `(940 % 1000) / 100000.0 = 0.00940`

For `latest_idr_rates`, the service adds:

```text
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00940)
```

## Architecture

```text
Controller
  -> FinanceDataStore
  -> idrDataFetcherMap
       latest_idr_rates      -> LatestIdrRatesFetcher
       historical_idr_usd    -> HistoricalIdrUsdFetcher
       supported_currencies  -> SupportedCurrenciesFetcher

ApplicationRunner
  -> calls each strategy exactly once at startup
  -> publishes a defensive immutable snapshot into FinanceDataStore

FrankfurterClientFactoryBean
  -> creates the configured WebClient
```

## Architectural Rationale

### Strategy Pattern

The endpoint accepts a polymorphic `resourceType`, but each resource has different external paths and transformation rules. The Strategy pattern keeps each resource-specific flow isolated behind `IDRDataFetcher`, while `FetcherStrategyConfig` wires resource keys to implementations. This avoids controller-level `if/else` or `switch` branching and makes new resources easy to add by introducing another strategy and map entry.

### Client FactoryBean

The Frankfurter `WebClient` is produced by a custom `FactoryBean<WebClient>` instead of a regular `@Bean` method. That keeps construction details, base URL, timeout configuration, and shared headers in a dedicated factory component. It also directly satisfies the requirement that the external API client be created through Spring's `FactoryBean<T>` extension point.

### ApplicationRunner

`ApplicationRunner` is used for startup ingestion because it runs after the Spring application context is ready and all dependencies are wired. That is safer than `@PostConstruct` for orchestration work that depends on multiple beans, configuration properties, and external client setup. If any required resource fails to load, startup fails instead of exposing partially initialized finance data.

## Testing

The test suite covers:

- Latest IDR strategy transformation and personalized spread calculation.
- Historical IDR/USD strategy path and date-row transformation.
- Supported currencies strategy transformation.
- Startup runner loading behavior and store immutability.

