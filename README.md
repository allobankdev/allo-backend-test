# Finance Aggregator — Single Endpoint (Frankfurter API, IDR-focused)

Author / GitHub username: `primaputra`
Spread Factor: `0.00093`

## Overview
Single endpoint: `GET /api/finance/data/{resourceType}`
Valid `resourceType` values:
- `latest_idr_rates` — latest rates for base IDR (plus `USD_BuySpread_IDR` computed).
- `historical_idr_usd` — small historical time series `/2024-01-01..2024-01-05?from=IDR&to=USD`.
- `supported_currencies` — `/currencies`.

All data is fetched **once at application startup** and served from an immutable, thread-safe in-memory store.

## Build & Run
Requirements: Java 17+, Maven

```bash
./mvnw -v
./mvnw clean package
java -jar target/finance-aggregator-0.0.1-SNAPSHOT.jar
```

## Example cURL
```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq
```

## Notes
- The WebClient is provided via a `FactoryBean` implementation located at `com.example.finance.config.FrankfurterWebClientFactoryBean`.
- Strategy Pattern: the three fetchers are Spring components named `latest_idr_rates`, `historical_idr_usd`, `supported_currencies` and implement `IDRDataFetcher`. The controller uses map injection to avoid if/else.
- Startup Loader: `StartupDataLoader` implements `ApplicationRunner` and loads data once into `ImmutableDataStore`.

## Spread calculation
Username: `primaputra` -> sum of Unicode points = 1093
1093 % 1000 = 93
Spread Factor = 93 / 100000.0 = 0.00093
