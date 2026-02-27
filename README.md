# Finance API Backend

This Spring Boot application implements a polymorphic REST service for fetching and caching currency data from the public **Frankfurter Exchange Rate API**. It focuses on Indonesian Rupiah (IDR) rates and make sure of having clean architecture, the strategy pattern, and thread-safe startup data loading.

## What it does

- On application startup the service fetches three resources from the Frankfurter API:
  * **Latest IDR rates** (`/latest?base=IDR`)
  * **Historical IDR to USD rates** (example range `/2024-01-01..2024-01-05?from=IDR&to=USD`)
  * **Supported currencies list** (`/currencies`)

- Data is loaded exactly once via an `ApplicationRunner` and stored in-memory. After initialization, all HTTP requests return cached data—no further external calls are made during runtime.

- The `/api/finance/data/{resourceType}` endpoint serves the content. Valid `resourceType` values are `latest_idr_rates`, `historical_idr_usd`, and `supported_currencies`.

- The `latest_idr_rates` response includes a computed field `USD_BuySpread_IDR` which uses a personalized spread factor. This spread factor is generated based on my username, `far14c`.

## Architectural overview

1. **Strategy Pattern**: An `IDRDataFetcher` interface defines the contract. Three components implement it and are named after their resource type. A `Map<String, IDRDataFetcher>` is injected to select the appropriate strategy dynamically.

2. **FactoryBean for HTTP client**: `RestTemplateFactoryBean` constructs a configured `RestTemplate` using properties such as the base URL and timeouts. This satisfies the requirement to use a `FactoryBean` rather than a simple `@Bean`.

3. **In-memory store**: `FinanceDataService` holds fetched data in a `ConcurrentHashMap` and wraps lists in `Collections.unmodifiableList` to ensure immutability after loading.

4. **Startup runner**: `DataLoaderRunner` iterates the strategy map during application startup, populating the store and marking it initialized. The runner logs results and fails the start if any fetcher throws an exception.

5. **Controller**: `FinanceController` returns stored data or 404 for unknown types. No business logic resides in the controller.

6. **Spread calculation**: Encapsulated in `LatestIdrRatesFetcher`, the spread factor is computed from the GitHub username provided via configuration and applied to the USD rate.

## Setup & usage

```bash
# from backend_test directory
./mvnw clean package     # compile and run unit/integration tests
./mvnw spring-boot:run   # start the service
```

Set GitHub username correctly before running by editing `src/main/resources/application.properties`:

```properties
app.github.username=far14c
external.api.base-url=https://api.frankfurter.app/
```

The spread factor for `far14c` is calculated as:

```
(f + a + r + 1 + 4 + c) = 513
513 % 1000 = 513
spread factor = 513 / 100000.0 = 0.00513
```

This value will appear in each object of the `latest_idr_rates` response under the `USD_BuySpread_IDR` field.
Example requests:

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

The first command will show `USD_BuySpread_IDR` values reflecting your spread factor.

## Running tests

The project includes unit tests for each strategy and an integration test verifying data loading. Execute them via:

```bash
./mvnw test
```

All tests should pass, confirming correct behavior and architecture.

## Extending

To add a new resource type:
1. Create a new `IDRDataFetcher` implementation annotated with `@Component("new_type")`.
2. Ensure the `fetchData` method returns a `List<Map<String,Object>>`.

No changes to controller or runner are required; the new bean will be injected automatically.

