# IDR Rate Aggregator

A Spring Boot REST API that fetches and serves IDR exchange rate data from the [Frankfurter API](https://api.frankfurter.dev).

## Setup & Run

### Prerequisites
- Java 21+
- Maven

### Run the application
```bash
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

## Endpoint Usage

Base URL: `http://localhost:8080`
```bash
# Latest IDR rates
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Historical IDR/USD rates
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Supported currencies
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note

**GitHub Username:** `FarizMR`  
**Spread Factor:** `(763 % 1000) / 100_000.0` = **0.00763**  
**Formula:** `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00763)`

## Architectural Rationale

**1. Strategy Pattern** : Each resource type is handled by its own class. Adding a new resource only needs a new class, no changes to existing code. The controller picks the right handler from a map, no if/else needed.

**2. FactoryBean** : Keeps all WebClient setup (base URL, timeout) in one dedicated class instead of scattered across config.

**3. ApplicationRunner** : Runs after everything is ready, so all beans are available when data fetching starts.