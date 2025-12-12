# Finance Service for Allobank - Palito
---
This project loads currency data (latest IDR rates, historical IDR to USD, and supported currencies) via the Frankfurter API and serves them from an in-memory cache.

## Prerequisites
- Java 21
- Maven 3.6+
- Internet access to call `https://api.frankfurter.app` (unless you mock the calls in tests)

## Clone, build and run

1. Clone the repo:
    ```
    git clone <your-repo-url>
    cd finance
    ```
2. Build:
    ```   
    mvn clean package
    ```
3. Run (development):
    ```
    mvn spring-boot:run
    ```
By default the app runs on port `8080`.

## Run tests

- To run all tests:
    ```
    mvn test
    ```
## Endpoints — example curl commands

- Latest IDR rates (with added calculated `USD_BuySpread_IDR` using github username `palitojeremy` as the spread factor):
    ```
    curl -sS -w "\nStatus: %{http_code}\n" http://localhost:8080/api/finance/data/latest_idr_rates
    ```

- Historical IDR → USD data:
    ```
    curl -sS -w "\nStatus: %{http_code}\n" http://localhost:8080/api/finance/data/historical_idr_usd
    ```

- Supported currencies:
    ```
    curl -sS -w "\nStatus: %{http_code}\n" http://localhost:8080/api/finance/data/supported_currencies
    ```
    This would also show the response status code
## Customization note (Spread Factor & Date Range)

- Configured GitHub username (from `application.properties`): `palitojeremy`.

  1. Take the username in lowercase: `palitojeremy`
  2. Sum Unicode code points (ASCII) of each character of `palitojeremy`: 1301

  3. Compute `Spread Factor = (Sum of Unicode Values % 1000) / 100000` -> `301 / 100000` = 0.00301

- This spread factor is applied when computing `USD_BuySpread_IDR` in `LatestRatesService`.
- Configured Historical Data (from `application.properties`): `2024-01-01..2024-01-05`.
## To Change the GitHub username and Historical Data

- Edit `src/main/resources/application.properties` and modify `app.github-username=[username]`
- Edit `src/main/resources/application.properties` and modify `frankfurter.historical-date-range=[YYYY-MM-DD..YYYY-MM-DD]`

## Architectural Rationale

**Polymorphism Justification:** The Strategy Pattern is used to represent each data-fetching behavior (latest rates, historical data, supported currencies) as a separate bean implementing a common `IDRDataFetcher` interface. This is preferred over a single conditional block because it:
- Separates concerns: each strategy encapsulates its own API interaction and transformation logic.
- Improves extensibility: adding a new resource requires adding a new strategy class and registering it as a bean — no changes to a central `if/else` block.
- Simplifies testing and maintenance: strategies are individually testable and reduce the risk of regressions when modifying one resource's behavior.

**Client Factory:** A ``FactoryBean`` is used to construct the external API client (the Frankfurter `WebClient`) so the creation logic (base URL, timeouts, codecs) is centralized and configurable. Using a ``FactoryBean`` instead of a plain `@Bean` method provides:
- Encapsulation of complex setup: the ``FactoryBean`` can build and tune the client instance based on properties and environment.
- Lazy and repeated instantiation control: ``FactoryBean`` semantics allow for finer control over whether a single shared or multiple instances are produced, and integrates cleanly with Spring's lifecycle.
- Clear separation for testing: the factory can be replaced or mocked in tests to provide test-friendly clients without coupling test code to bean initialization logic.

All of these could not be achieved by only a `Bean`

**Startup Runner Choice:** An `ApplicationRunner` is used to perform initial data ingestion at startup instead of `@PostConstruct` for these reasons:
- **Best Practice**: `PostConstruct` is a Spring Interceptor. It interrups the normal Bean's life cycle. So unless it is absolutely necessary, using `ApplicationRunner`, which is an interface, is better practice.
- **Ordering and lifecycle control**: `ApplicationRunner` runs after the Spring context is fully set up and all beans are available, which is important when the runner depends on multiple beans (services, store, web client).
- **Error handling and observability**: `@PostConstruct` cannot throw a checked exception. If something were to go wrong, we cannot track it as well as using `ApplicationRunner`, which can throw a check exception.
- **Testability**: the runner is a regular Spring bean that can be invoked directly in integration tests. In the other hand, `@PostConstruct` logic is harder to trigger in test scenarios.