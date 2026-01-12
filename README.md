# Simple Overview
## Requirements
   * Java 17 or Higher
   * Use Gradle as is in the application provided

### How To run
Clone the Repository
```bash
  git clone <repository-url>
  cd finance-aggregator-service
   
  1. ./gradlew clean build
  2. ./gradlew bootRun
  3. ./gradlew test
```
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd' \
--header 'x-api-key: for_internal_test'
Curl API
```
   curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd' \--header 'x-api-key: for_internal_test'
   curl --location 'http://localhost:8080/api/finance/data/supported_currencies' \--header 'x-api-key: for_internal_test'
   curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates' \--header 'x-api-key: for_internal_test'

```

* **GitHub Username**: evanahmad
* **Calculated Spread Factor**: `0.00933` 

# Finance Aggregator Service

## 1. DETAIL OVERVIEW

    Finance Aggregator Service is a Spring Boot (Java 17, WebFlux) application that aggregates exchange rate data from the Frankfurter API and exposes it through a unified internal API.

### The service:

    1. Fetches multiple finance resources once at startup
    2. Stores them as an immutable, in-memory snapshot
    3. Serves data through a single unified endpoint
    3. Avoids conditional logic by using map-based strategy resolution

### Supported Resources
The service aggregates the following resources:

| Resource Type          | Description                                                     |
| ---------------------- | --------------------------------------------------------------- |
| `latest_idr_rates`     | Latest IDR exchange rate data with computed USD buy spread      |
| `historical_idr_usd`   | Historical IDR → USD exchange rates for a configured date range |
| `supported_currencies` | List of supported currencies from the upstream API              |

### External API

Data is retrieved from the Frankfurter public API.
    
    Base URL is configurable via application.yml
    All external calls are executed using a centralized WebClient factory
    Timeouts and connection settings are externally configurable

### Internal API
Path Variable has a constraints -> latest_idr_rates, historical_idr_usd

    GET /api/finance/data/{resourceType}

## 2. STARTUP & DATA LOADING BEHAVIOUR 
    
1. All resources are fetched exactly once during application startup.
2. Fetching is orchestrated via a Spring-injected Map<String, IDRDataFetcher> keyed by resourceType.
3. Retry behavior (count and delay) is configurable.
4. After successful loading, the data store is sealed and becomes immutable.
5. No further external API calls are made after startup.
6. This guarantees deterministic behavior and prevents partial or inconsistent data access.

## 3. Architecture & Designs Decision
Strategy Pattern with Map-Based Lookup
Each finance resource is implemented as a dedicated strategy (IDRDataFetcher).
    
1. Strategies are registered as Spring beans using resourceType as the bean name.
2. A Map<String, IDRDataFetcher> is injected by Spring.
3. Resource resolution is performed via map lookup (no if, switch, or conditional branching).

This ensures extensibility and clear separation of responsibilities.

## 4. Immutable In-Memory Store

1. Data is initially written to a concurrent mutable store during startup.
2. Once loading is complete, an immutable snapshot is published atomically.
3. After sealing, all write operations are rejected.
4. All API reads operate on the immutable snapshot only.
5. This design provides thread safety and consistent read behavior.
   Configuration Management

## 5. All configuration values are externalized into application.yml, including:

1. External API base URL and timeouts
2. Retry configuration
3. Supported resource definitions
4. Historical date ranges
5. GitHub username used for spread factor calculation
6. No magic strings or hardcoded values are used in business logic.

## 6. Error Handling

1. Centralized global exception handling via @RestControllerAdvice
3. Clear HTTP status mapping for different error categories
3. Unified error response format

### Error Classification & Mapping
1. **Resource Not Found (409 Conflict)**: Terjadi jika `{resourceType}` yang diminta tidak terdaftar pada pattern.
2. **Invalid Input (409 Conflict)**: Terjadi jika terdapat pelanggaran kontrak request (Constraint Violation).
3. **Security/Unauthorized (409 Conflict)**: Jika `x-api-key` salah atau tidak ada, sistem akan menangkapnya melalui `WebFilter` dan mengalihkannya ke format error standar demi keamanan (*obscurity*).
4. **General/Internal Error (409 Conflict)**: *Fallback* untuk semua error tak terduga (seperti API external down saat startup).

## 7. Thread Safety Considerations

1. External calls are executed concurrently using Reactor Netty.
2. Data mutation is strictly limited to startup phase.
3. Immutable snapshots are safely published using atomic references.
4. No blocking operations are performed in request-handling paths.