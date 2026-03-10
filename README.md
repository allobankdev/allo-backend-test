# Polymorphic Finance API

This project is a Spring Boot application that demonstrates the use of several advanced architectural patterns to create a robust and extensible API. It integrates with the Frankfurter API to fetch financial data, processes it, and exposes it through a single, polymorphic endpoint.

## Setup and Run

1.  **Prerequisites**:
    *   Java 17 or higher
    *   Maven or Gradle

2.  **Build the project**:
    ```bash
    # Using Maven
    mvn clean install

    # Using Gradle
    gradle clean build
    ```

3.  **Run the application**:
    ```bash
    # Using Maven
    mvn spring-boot:run

    # Using Gradle
    gradle bootRun
    ```

The application will start on port 8080.

## Endpoint Usage

The single endpoint `GET /api/finance/data/{resourceType}` serves all data. Replace `{resourceType}` with one of the following values:

*   `latest_idr_rates`
*   `historical_idr_usd`
*   `supported_currencies`

### Example cURL Commands

**1. Get Latest Rates (base IDR) with Spread Calculation**
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

**2. Get Historical Rates (IDR to USD)**
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

**3. Get Supported Currencies**
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note

*   **GitHub Username**: `rakuszz0`
*   **Calculated Spread Factor**: `0.00842`

This factor is used in the calculation of the `USD_BuySpread_IDR` field for the `latest_idr_rates` resource.

## Architectural Rationale

### Polymorphism Justification: The Strategy Pattern

The Strategy Pattern was chosen to handle the different data resources (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) for several key reasons:

*   **Extensibility**: Adding a new data resource is as simple as creating a new class that implements the `DataFetcherStrategy` interface. The core logic of the application does not need to be modified. This adheres to the Open/Closed Principle.
*   **Maintainability**: Each strategy is self-contained and responsible for a single piece of functionality. This makes the code easier to read, understand, and test.
*   **Decoupling**: The `FinanceController` is completely decoupled from the implementation details of how the data is fetched. It simply delegates the task to the appropriate strategy, which is resolved at runtime by Spring's dependency injection mechanism. This avoids a chain of `if/else` or `switch` statements, which would become unwieldy as more resources are added.

### Client Factory: The `FactoryBean`

A `FactoryBean` was used to create the `WebClient` instance for the following reasons:

*   **Encapsulation of Complex Initialization**: The `FactoryBean` encapsulates the logic for creating and configuring the `WebClient`. This is particularly useful if the client requires more complex setup, such as setting default headers, timeouts, or filters. By centralizing this logic, we avoid cluttering our `@Configuration` classes.
*   **Separation of Concerns**: The `FactoryBean`'s sole responsibility is to create the `WebClient`. This is a cleaner approach than defining a `@Bean` method directly in a configuration class, especially when the creation logic is non-trivial.
*   **Lazy Initialization**: While not explicitly configured here, a `FactoryBean` can provide more control over the bean's lifecycle, including lazy initialization.

### Startup Runner Choice: `ApplicationRunner`

An `ApplicationRunner` was used for the initial data ingestion for these reasons:

*   **Guaranteed Execution Order**: `ApplicationRunner` (and `CommandLineRunner`) beans are executed after the application context has been fully initialized but before the application is ready to accept requests. This ensures that the in-memory data store is populated before any client can access the API.
*   **Separation from Bean Lifecycle**: Using `@PostConstruct` on a service method would tie the data loading logic to the lifecycle of that specific bean. An `ApplicationRunner` is a more explicit and dedicated component for startup tasks, making the application's bootstrap process clearer.
*   **Access to Application Arguments**: `ApplicationRunner` provides access to command-line arguments, which can be useful for more advanced configuration scenarios.
