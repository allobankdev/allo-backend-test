# Allo Bank Finance Data Aggregation API

A production-ready Spring Boot REST API that aggregates exchange rate data from the Frankfurter API using advanced architectural patterns including the Strategy Design Pattern, Factory Bean pattern, and immutable in-memory caching.

## 📋 Features

- **Strategy Pattern Implementation** - Three polymorphic data fetching strategies for different resource types
- **Factory Bean Pattern** - Custom WebClient creation through FactoryBean interface
- **Startup Data Loading** - All data fetched exactly once on application startup
- **Thread-Safe Immutable Cache** - In-memory store with guaranteed thread safety and immutability
- **Graceful Error Handling** - Comprehensive exception handling for network failures
- **USD Buying Spread Calculation** - Personalized spread factor based on GitHub username

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Build & Run

```bash
# Clone and navigate to project
cd allo-backend-test

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Application starts on http://localhost:8080
```

### Run Tests

```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest=*Test

# Run only integration tests
mvn test -Dtest=*IntegrationTest
```

## 📡 API Endpoints

### 1. Latest IDR Exchange Rates with Spread Calculation

```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

**Response Example:**
```json
{
  "base": "IDR",
  "date": "2026-03-09",
  "rates": {
    "USD": 0.0000627,
    "EUR": 0.0000581,
    "USD_BuySpread_IDR": 0.0000629,
    ...
  }
}
```

**Note:** `USD_BuySpread_IDR` includes the personalized spread factor.

### 2. Historical IDR to USD Rates (2024-01-01 to 2024-01-05)

```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

**Response Example:**
```json
{
  "base": "IDR",
  "start_date": "2024-01-01",
  "end_date": "2024-01-05",
  "rates": {
    "2024-01-02": {
      "USD": 0.0000627
    },
    "2024-01-03": {
      "USD": 0.0000628
    },
    ...
  }
}
```

### 3. Supported Currencies

```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

**Response Example:**
```json
{
  "USD": "United States Dollar",
  "EUR": "Euro",
  "GBP": "British Pound",
  ...
}
```

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
# Application name
spring.application.name=allo-bank

# Frankfurter API Base URL
external.frankfurter.base-url=https://api.frankfurter.app

# GitHub username for spread factor calculation
app.github-username=aryaevan
```

## 📊 Personalization Note

**GitHub Username:** `aryaevan`

**Spread Factor Calculation:**

The spread factor is calculated from your GitHub username using Unicode values:

1. **Username (lowercase):** `aryaevan`
2. **Unicode Sum:** 
   - a(97) + r(114) + y(121) + a(97) + e(101) + v(118) + a(97) + n(110) = 855
3. **Formula:** `(855 % 1000) / 100000.0 = 0.00855`
4. **Spread Factor:** `0.00855`

**USD_BuySpread_IDR Calculation:**
- For USD rate = 0.0000627 from API
- USD_BuySpread_IDR = (1 / 0.0000627) × (1 + 0.00855)
- USD_BuySpread_IDR ≈ 0.0000629

## 🏗️ Architecture & Design Patterns

### Constraint A: Strategy Pattern

**Implementation:** Three concrete strategy classes implement the `IDRDataFetcher` interface:
- `LatestIDRRatesStrategy` - Fetches `/latest?base=IDR`
- `HistoricalIDRUSDStrategy` - Fetches time series data
- `SupportedCurrenciesStrategy` - Fetches `/currencies`

**Map-based Lookup:** The controller uses `strategyMap.get(resourceType)` to dynamically select the appropriate strategy, eliminating all conditional logic.

### Constraint B: Client Factory Bean

**Implementation:** `FrankfurterClientFactoryBean` implements Spring's `FactoryBean<WebClient>` interface to:
- Encapsulate WebClient creation logic
- Externalize configuration via `@Value`
- Apply consistent headers and settings
- Provide single point of maintenance

### Constraint C: Startup Data Runner & Immutability

**Implementation:**
- `DataLoaderRunner` implements `ApplicationRunner` to load all data exactly once on startup
- `FinanceDataStore` uses `volatile` fields and synchronized methods for thread safety
- `Collections.unmodifiableMap()` ensures immutability once initialized
- `FinanceController` serves data from the cache, not from the API

## 🛠️ Architectural Rationale

### 1. Polymorphism Justification: Strategy Pattern

**Why Strategy Pattern over conditional blocks?**

The Strategy Pattern was chosen because:

- **Extensibility:** Adding a new resource type requires only creating a new strategy class and registering it. No changes to existing controller or service code.
- **Maintainability:** Each strategy encapsulates its logic independently, making code easier to understand, test, and modify.
- **SOLID Principles:** Adheres to Open/Closed Principle (open for extension, closed for modification) and Single Responsibility Principle.
- **Testability:** Each strategy can be unit tested in isolation with mocked WebClients.
- **Scalability:** As the number of resource types grows, the strategy map dynamically handles them without conditional branching.

A simpler if/else approach would couple resource logic to the controller, creating tight coupling and making the code harder to extend and maintain.

### 2. Client Factory: FactoryBean Pattern

**Why FactoryBean over a simple @Bean method?**

The `FactoryBean<WebClient>` pattern was chosen because:

- **Encapsulation:** Complex object creation logic (base URL injection, header configuration) is contained in a single, dedicated class.
- **Reusability:** The factory can be reused in tests or other parts of the application with different configurations.
- **Flexibility:** `FactoryBean` provides hooks for object type checking (`getObjectType()`) and singleton management (`isSingleton()`), enabling Spring to optimize bean creation.
- **Explicit Intent:** Using `FactoryBean` explicitly declares that this bean has special creation semantics, making the code's intent clearer to maintainers.
- **Testability:** In tests, the factory can be mocked or extended to provide alternative configurations without modifying production code.

A simple `@Bean` method would mix configuration concerns with bean registration, reducing clarity and flexibility.

### 3. Startup Runner: ApplicationRunner vs @PostConstruct

**Why ApplicationRunner over @PostConstruct?**

`ApplicationRunner` was chosen because:

- **Lifecycle Guarantees:** `ApplicationRunner` executes after the entire Spring context is initialized, ensuring all dependencies are available and properly configured.
- **Dependency Readiness:** Unlike `@PostConstruct` (which runs during bean construction), `ApplicationRunner` runs when all beans are ready, preventing premature initialization errors.
- **Error Handling:** If data loading fails in `ApplicationRunner`, the application can fail fast and cleanly. With `@PostConstruct`, failures are harder to detect and handle.
- **Ordering Control:** Multiple `ApplicationRunner` beans can be ordered with `@Order`, providing explicit control over startup sequence.
- **Logging & Observability:** `ApplicationRunner` integrates better with Spring Boot's startup logging and can be easily monitored.
- **Testing:** Integration tests can hook into the startup sequence more easily with `ApplicationRunner`.

Using `@PostConstruct` would risk accessing uninitialized dependencies and would lack the explicit lifecycle control that `ApplicationRunner` provides.

## 🧪 Testing

### Unit Tests

Unit tests for each strategy verify:
- Correct data fetching from mocked WebClient
- Proper data transformation
- Spread factor calculation accuracy
- Edge cases and null handling

Run: `mvn test -Dtest=*StrategyTest`

### Integration Tests

Integration tests verify:
- ApplicationRunner loads data on startup
- FinanceDataStore is properly initialized
- Data is served from cache (not from external API)
- Thread safety and immutability guarantees

Run: `mvn test -Dtest=*IntegrationTest`

## 📦 Project Structure

```
src/main/java/com/aryaevan/allo/
├── AlloBankApplication.java          # Spring Boot entry point
├── config/
│   ├── DataStoreConfig.java          # FinanceDataStore bean
│   └── StrategyConfig.java           # Strategy map and WebClient beans
├── controller/
│   └── FinanceController.java        # REST endpoint
├── client/
│   └── FrankfurterClientFactoryBean.java  # WebClient factory
├── strategy/
│   ├── IDRDataFetcher.java           # Strategy interface
│   ├── LatestIDRRatesStrategy.java
│   ├── HistoricalIDRUSDStrategy.java
│   └── SupportedCurrenciesStrategy.java
├── service/
│   └── FinanceDataService.java       # Business logic (deprecated in favor of direct store access)
├── store/
│   └── FinanceDataStore.java         # Thread-safe immutable cache
├── runner/
│   └── DataLoaderRunner.java         # ApplicationRunner for startup loading
├── util/
│   └── SpreadCalculationUtil.java    # Spread factor calculation
├── dto/
│   ├── LatestRatesResponse.java
│   └── HistoricalRatesResponse.java
└── exception/
    └── GlobalExceptionHandler.java   # Centralized error handling
```

## 🔒 Error Handling

The application handles:
- **Network Failures:** Graceful fallback for connection timeouts
- **4xx Errors:** Invalid requests to external API
- **5xx Errors:** Server errors from Frankfurter API
- **State Errors:** Data store not initialized
- **Unknown Errors:** Generic error response for unexpected exceptions

All errors return structured JSON responses with timestamps and HTTP status codes.

## 📝 License

This project is part of Allo Bank's technical assessment program.
