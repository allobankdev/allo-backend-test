# IDR Rate Aggregator (Allo Bank Take-Home)

## Summary
Spring Boot application that fetches three resources from the public Frankfurter API at startup and exposes them via a single polymorphic endpoint:

`GET /api/finance/data/{resourceType}`

where `{resourceType}` is one of:
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

### Key features
- Strategy Pattern for resource handling (no if/else in controller)
- `FactoryBean` provides configured `WebClient` to talk to Frankfurter API
- Data is fetched **once** at application startup (ApplicationRunner) and stored immutably in memory
- `latest_idr_rates` includes computed `USD_BuySpread_IDR` using personalized spread factor

## Personalization
- GitHub username used: `vivinessa`
- Spread Factor calculation:
  - Sum of Unicode values of `vivinessa` = 984
  - `984 % 1000 = 984`
  - Spread Factor = `984 / 100000.0 = 0.00984`

This Spread Factor is applied as:


## Requirements
- Java 21, Maven 3.8+, Internet access (to call frankfurter.app at startup)

## Running locally
1. Clone
2. `mvn clean package`
3. `java -jar target/idr-rate-aggregator-0.0.1-SNAPSHOT.jar`

Or run via IDE.

## Example cURL
Fetch latest IDR rates with computed USD_BuySpread_IDR:
```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq .

