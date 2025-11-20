# IDR Rate Aggregator - Allo Bank Backend Developer Take Home Test

This project is an implementation of Allo Bank’s backend take-home test.  
The service fetches and aggregates IDR exchange rate data from the Frankfurter API,  
and exposes 3 public API endpoints.

## 1. FEATURES IMPLEMENTED

1) Latest IDR Rates
   - Fetches real-time exchange rates from Frankfurter API
   - Base currency: IDR
   - Includes custom calculation for USD buy spread
   - Cached on application startup for faster response

2) Historical IDR → USD Rates
   - Retrieves historical rates within a configurable date range
   - Date range is fully dynamic and controlled from application.yaml
   - Example: 2024-01-01..2024-01-05

3) Supported Currencies List
   - Fetches all supported currencies from Frankfurter API
   - Returned as a map: { "USD": "United States Dollar", ... }
   - Cached on startup to avoid repeated external calls

## 2. ARCHITECTURE & DESIGN

- Pattern used: Strategy Pattern + Preloading Cache on Startup
- All data fetchers implement: IDRDataFetcher
- Fetchers:
    - latest_idr_rates → LatestIdrRatesFetcher
    - historical_idr_usd → HistoricalIdrUsdFetcher
    - supported_currencies → SupportedCurrenciesFetcher

- AggregatedDataStore:
    - Acts as in-memory cache
    - Stores preloaded API data
    - Ensures fast response with no runtime remote calls

- StartupDataLoader:
    - Runs at app startup
    - Calls all strategies and caches all data

## 3. TECH STACK

- Java 17
- Spring Boot 3
- Spring WebFlux (WebClient)
- Lombok
- Maven

## 4. RUN INSTRUCTIONS

1) Clean build:
   mvn clean install

2) Run application:
   mvn spring-boot:run

Application runs on:
   http://localhost:8080

## 5. API ENDPOINTS

1) Latest IDR Rates
   GET /api/finance/data/latest_idr_rates

   curl command:
   curl --location "http://localhost:8080/api/finance/data/latest_idr_rates"

----------------------------------------

2) Historical Rates (IDR → USD)
   GET /api/finance/data/historical_idr_usd

   curl command:
   curl --location "http://localhost:8080/api/finance/data/historical_idr_usd"

----------------------------------------

3) Supported Currencies
   GET /api/finance/data/supported_currencies

   curl command:
   curl --location "http://localhost:8080/api/finance/data/supported_currencies"

## 6. CONFIGURATION

application.yaml:

spring:
  frankfurter:
    base-url: https://api.frankfurter.app/
    historical-range: 2024-01-01..2024-01-05

You can freely change the date range above to fetch any historical period.

## 7. NOTES FOR REVIEWERS (Allo Bank)

- All endpoints respond instantly since data is cached on startup.
- WebClient is used instead of RestTemplate, following modern reactive best practices.
- The Strategy Pattern ensures the solution is easily extendable (simply add new fetchers).
- Code is structured to maximize clarity, scalability, and testability.

END OF README
