# IDR Rate Aggregator - Submission Documentation

## Setup & Run Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Configuration
1. Update `src/main/resources/application.yml` with your GitHub username:
```yaml
github:
  username: YOUR_GITHUB_USERNAME
```

### Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run tests only
mvn test
```

## Endpoint Usage

### 1. Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2. Historical IDR to USD
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note

**GitHub Username:** `1wasilah1`

**Spread Factor Calculation:**
- Sum of Unicode values: 843
- Spread Factor: 0.00843

Calculation breakdown for username "1wasilah1":
```
1(49) + w(119) + a(97) + s(115) + i(105) + l(108) + a(97) + h(104) + 1(49) = 843
Spread Factor = (843 % 1000) / 100000.0 = 0.00843
```

---

## 🛠️ Architectural Rationale

### 1. Polymorphism Justification: Strategy Pattern

**Why Strategy Pattern over conditional blocks?**

The Strategy Pattern was chosen to handle multiple resource types because it provides:

- **Extensibility**: Adding new resource types (e.g., `weekly_idr_eur`) requires only creating a new strategy class implementing `IDRDataFetcher` interface. No modification to existing controller or service code is needed, adhering to the Open/Closed Principle.

- **Maintainability**: Each resource's logic is encapsulated in its own class, making the codebase easier to understand, test, and debug. Changes to one resource type don't affect others.

- **Testability**: Each strategy can be unit tested independently with mocked dependencies, ensuring isolated test coverage.

- **Scalability**: The map-based lookup (`Map<String, IDRDataFetcher>`) automatically registers all strategies via Spring's dependency injection, eliminating manual conditional logic and reducing cyclomatic complexity in the controller.

A simple `if/else` or `switch` statement would create a monolithic service method that violates Single Responsibility Principle and becomes harder to maintain as the number of resources grows.

### 2. Client Factory: FactoryBean Benefit

**Why FactoryBean over standard @Bean method?**

Using `FactoryBean<WebClient>` provides several advantages:

- **Complex Initialization Logic**: FactoryBean allows encapsulating complex object creation logic (timeouts, headers, codecs configuration) in a dedicated class rather than cluttering a configuration class.

- **Lifecycle Control**: FactoryBean provides explicit control over singleton/prototype scope and lazy initialization through its interface methods (`isSingleton()`, `getObjectType()`).

- **Separation of Concerns**: The factory class is solely responsible for WebClient creation, making it easier to modify client configuration without touching other configuration beans.

- **Reusability**: The factory pattern makes it easier to create multiple configured clients if needed in the future (e.g., different clients for different external APIs).

- **Testability**: FactoryBean can be easily mocked or replaced in test contexts, providing better test isolation.

A standard `@Bean` method would work but lacks the structural benefits and explicit lifecycle management that FactoryBean provides for complex object creation scenarios.

### 3. Startup Runner Choice: ApplicationRunner vs @PostConstruct

**Why ApplicationRunner over @PostConstruct?**

`ApplicationRunner` was chosen for initial data ingestion because:

- **Application Context Readiness**: ApplicationRunner executes after the entire Spring application context is fully initialized and all beans are ready. This ensures all dependencies (WebClient, strategies, services) are properly wired before data fetching begins.

- **Command-Line Arguments Access**: ApplicationRunner provides access to application arguments, allowing future flexibility for startup configuration (e.g., `--skip-data-load` flag).

- **Explicit Startup Phase**: ApplicationRunner clearly signals that this is an application-level initialization task, not a bean-level post-construction step, improving code readability.

- **Error Handling**: Exceptions thrown in ApplicationRunner prevent the application from starting, which is the desired behavior for critical data initialization. With `@PostConstruct`, the bean would still be created even if initialization fails.

- **Execution Order Control**: Multiple ApplicationRunners can be ordered using `@Order` annotation, providing better control over startup sequence if multiple initialization tasks exist.

`@PostConstruct` is suitable for bean-level initialization but lacks the application-wide context and control that ApplicationRunner provides for critical startup operations.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/allobank/idr/
│   │   ├── IdrRateAggregatorApplication.java
│   │   ├── config/
│   │   │   ├── FrankfurterApiProperties.java
│   │   │   └── StrategyConfig.java
│   │   ├── controller/
│   │   │   └── FinanceDataController.java
│   │   ├── factory/
│   │   │   └── WebClientFactoryBean.java
│   │   ├── runner/
│   │   │   └── DataInitializationRunner.java
│   │   ├── service/
│   │   │   └── DataStoreService.java
│   │   └── strategy/
│   │       ├── IDRDataFetcher.java
│   │       ├── LatestIDRRatesStrategy.java
│   │       ├── HistoricalIDRUSDStrategy.java
│   │       └── SupportedCurrenciesStrategy.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/allobank/idr/
        ├── runner/
        │   └── DataInitializationRunnerIntegrationTest.java
        └── strategy/
            ├── LatestIDRRatesStrategyTest.java
            ├── HistoricalIDRUSDStrategyTest.java
            └── SupportedCurrenciesStrategyTest.java
```

## Key Implementation Details

### Thread-Safety
- `ConcurrentHashMap` for thread-safe data storage
- `Collections.unmodifiableMap()` for immutable data after initialization
- `volatile` flag for initialization state

### Error Handling
- Graceful exception handling in ApplicationRunner
- Validation in controller for invalid resource types
- Proper HTTP status codes in responses

### Configuration Externalization
- API base URL in `application.yml`
- GitHub username configurable via properties
- Timeout settings externalized
