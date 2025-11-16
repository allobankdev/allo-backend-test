# IDR Rate Aggregator - Allo Bank Backend Take-Home Test

A production-ready Spring Boot REST API that aggregates Indonesian Rupiah (IDR) exchange rate data from the Frankfurter API, demonstrating advanced architectural patterns and thread-safe design.

## 📋 Table of Contents

- [Features](#features)
- [Technical Stack](#technical-stack)
- [Architecture](#architecture)
- [Setup & Installation](#setup--installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Personalization](#personalization)
- [Architectural Rationale](#architectural-rationale)

## ✨ Features

- **Single Polymorphic Endpoint**: One endpoint serving three distinct data sources
- **Strategy Pattern Implementation**: Clean, extensible resource handling
- **Thread-Safe In-Memory Caching**: Data loaded once at startup
- **Custom FactoryBean**: Externalized API client configuration
- **Startup Data Initialization**: ApplicationRunner ensures data availability
- **Personalized Spread Calculation**: Unique banking spread based on GitHub username
- **Production-Ready**: Comprehensive error handling, logging, and testing

## 🛠 Technical Stack

- **Java 21**
- **Spring Boot 3.X.X**
- **Spring WebFlux** (WebClient for reactive HTTP calls)
- **Maven** (Build tool)
- **JUnit 5 & Mockito** (Testing)
- **Spring Actuator** (Health Check)
- **SpringDoc OpenAPI** (Swagger OpenAPI)
- **Lombok** (Boilerplate reduction)

## 🏗 Architecture

### Project Structure

```
com.allobank/
├── config/
│   ├── FrankfurterApiProperties.java (Externalized endpoint URLs)
│   ├── AppProperties.java
│   └── FrankfurterWebClientFactory.java
├── controller/
│   └── FinanceDataController.java (Uses Strategy map - no if/else)
├── dto/
│   ├── LatestRatesResponse.java
│   ├── LatestRatesWithSpreadResponse.java
│   ├── HistoricalRatesResponse.java
│   └── CurrenciesResponse.java
├── enums/
│   └── ResourceType.java
├── runner/
│   └── DataInitializationRunner.java (Calls fetchFromExternalApi())
├── service/
│   ├── IDRDataFetcher.java (interface)
│   ├── HistoricalIDRUSDStrategy.java
│   ├── LatestIDRRatesStrategy.java
│   └── SupportedCurrenciesStrategy.java
│   
├── store/
│   ├── DataStoreService.java
│ 
└── utility/
    └── SpreadCalculator.java
```

### Request Flow

1. **Startup**: ApplicationRunner → Strategy.fetchFromExternalApi() → DataStoreService.storeData()
2. **Runtime**: Controller → Strategy.getData() → DataStoreService.getData() → Return cached data

### Key Design Patterns

1. **Strategy Pattern**: Different data fetching strategies for each resource type
2. **Factory Pattern**: FactoryBean for WebClient creation
3. **Singleton Pattern**: Thread-safe data store with immutable data
4. **Dependency Injection**: Spring-managed beans with constructor injection

## 🚀 Setup & Installation

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Internet connection (for Frankfurter API access)

### Clone the Repository

```bash
git clone https://github.com/hakimamarullah/allo-backend-test
cd allo-backend-test
```

### Configure GitHub Username

Set your GitHub username as an environment variable:

```bash
export GITHUB_USERNAME=your_github_username
```

Or update `application.yml`:

```yaml
app:
  github-username: <your_github_username>
```

### Build the Project

```bash
mvn clean install
```

## ▶️ Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using JAR

```bash
java -jar target/allo-bank-frankfurter-0.0.1.jar
```

### Using Maven with GitHub Username

```bash
GITHUB_USERNAME=your_username mvn spring-boot:run
```

The application will:
1. Start on port 8080
2. Fetch data from Frankfurter API on startup
3. Load data into in-memory store
4. Begin accepting requests
5. Access Swagger UI at `http://localhost:8080/swagger-ui/index.html`

Look for this log message:
```
=== Data Initialization Complete ===
Application ready to serve requests
```

## 📡 API Endpoints

### Base URL
```
http://localhost:8080
```

### 1. Latest IDR Rates (with USD Buy Spread)

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

**Response Example:**
```json
{
  "amount": 1.0,
  "base": "IDR",
  "date": "2024-11-15",
  "rates": {
    "USD": 0.000064,
    "EUR": 0.000059,
    "GBP": 0.000051
  },
  "USD_BuySpread_IDR": 15744.79688,
  "spread_factor": 0.00765
}
```

### 2. Historical IDR-USD Rates

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

**Response Example:**
```json
{
  "amount": 1.0,
  "base": "IDR",
  "start_date": "2024-01-01",
  "end_date": "2024-01-05",
  "rates": {
    "2024-01-01": { "USD": 0.000064 },
    "2024-01-02": { "USD": 0.000065 },
    "2024-01-03": { "USD": 0.000066 }
  }
}
```

### 3. Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

**Response Example:**
```json
{
    "USD": "United States Dollar",
    "EUR": "Euro",
    "IDR": "Indonesian Rupiah",
    "JPY": "Japanese Yen"
}
```

### 4. Health Check

```bash
curl http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=LatestIDRRatesStrategyTest
```

### Test Coverage

- **Unit Tests**: All three strategy implementations
- **Integration Tests**: ApplicationRunner initialization
- **Utility Tests**: Spread calculation logic

### Test Structure

```
src/test/java/com/allobank/
├── service/
│   ├── LatestIDRRatesStrategyTest.java
│   ├── HistoricalIDRUSDStrategyTest.java
│   └── SupportedCurrenciesStrategyTest.java
├── runner/
│   └── DataInitializationIntegrationTest.java
└── utililty/
    └── SpreadCalculatorTest.java
```

## 🔐 Personalization

### GitHub Username: `hakimamarullah`
### Spread Factor: `0.00473`

### Spread Factor Calculation

The spread factor is calculated using the following algorithm:

1. Convert GitHub username to lowercase
2. Sum Unicode values of all characters
3. Apply formula: `(Sum % 1000) / 100000.0`

### Example Calculation

For username `johndoe47`:
- Characters: j(106) + o(111) + h(104) + n(110) + d(100) + o(111) + e(101) + 4(52) + 7(55)
- Sum: 850
- Spread Factor: `(850 % 1000) / 100000.0 = 0.00850`

### USD Buy Spread Formula

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```

**Example:**
- Rate_USD = 0.000064
- Spread Factor = 0.00950
- USD_BuySpread_IDR = (1 / 0.000064) * (1 + 0.00950) = **15,744.79688 IDR**

---

## 🛠️ Architectural Rationale

### 1. Polymorphism Justification: Why Strategy Pattern?

**Question:** Why use the Strategy Pattern over a simple conditional block (if/else or switch) in the service layer?

**Answer:**

The Strategy Pattern was chosen for several critical reasons that directly impact **extensibility** and **maintainability**:

#### Extensibility Benefits:
- **Open/Closed Principle**: Adding a new resource type (e.g., `gold_prices_idr`) requires only creating a new strategy class. No existing code needs modification.
- **Zero Controller Changes**: The controller uses map-based strategy lookup injected by Spring. New strategies are automatically discovered via component scanning.
- **Independent Evolution**: Each strategy can evolve independently without affecting others. For example, the latest rates strategy can add complex caching logic without impacting historical data fetching.

#### Maintainability Benefits:
- **Single Responsibility**: Each strategy class has one job—handle its specific resource type. This makes code easier to understand and debug.
- **Testability**: Strategies can be unit tested in isolation with mocked dependencies. A conditional block would require testing the entire service with all branches.
- **Reduced Complexity**: Without the pattern, the service would contain nested conditionals handling three different API endpoints, response transformations, and calculations. This violates SRP and creates a maintenance nightmare.

#### Real-World Impact:
In a conditional approach:
```java
if (resourceType.equals("latest_idr_rates")) {
    // 50 lines of latest rates logic
} else if (resourceType.equals("historical_idr_usd")) {
    // 40 lines of historical logic
} else if (resourceType.equals("supported_currencies")) {
    // 30 lines of currencies logic
}
```

This creates a 120+ line method that's difficult to test and modify. With Strategy Pattern, we have three 30-50 line classes that are independently testable and maintainable.

---

### 2. Client Factory: Why FactoryBean?

**Question:** Explain the role and benefit of using a `FactoryBean` to construct the external API client. Why is this preferable to a standard `@Bean` method?

**Answer:**

The `FactoryBean` approach provides several advantages over a simple `@Bean` method:

#### Advanced Configuration Capabilities:
- **Complex Initialization Logic**: The `FrankfurterWebClientFactoryBean` encapsulates sophisticated WebClient setup including timeout configuration, HTTP client tuning, and header defaults.
- **Lazy Initialization Control**: `FactoryBean` provides explicit control over when and how the client is created, allowing for conditional creation logic.
- **Post-Construction Validation**: We can validate configuration properties and fail fast during bean creation rather than at first use.

#### Separation of Concerns:
- **Configuration Encapsulation**: All WebClient configuration logic lives in one dedicated class, not scattered across multiple `@Configuration` classes.
- **Testability**: The factory itself can be unit tested to ensure proper client configuration without requiring a full Spring context.
- **Type Safety**: The `FactoryBean<WebClient>` interface provides compile-time guarantees about what type of object is created.

#### Real Production Benefits:
In a standard `@Bean` approach:
```java
@Bean
public WebClient webClient(@Value("${api.url}") String url) {
    return WebClient.builder().baseUrl(url).build();
}
```

This lacks:
- Timeout configuration (production clients need connection and read timeouts)
- Retry logic configuration
- Custom error handling
- Centralized client behavior

The `FactoryBean` approach allows us to:
1. Externalize ALL configuration via `@ConfigurationProperties`
2. Apply consistent timeout policies across all API calls
3. Add logging, metrics, or circuit breakers in one place
4. Modify client behavior without touching multiple configuration classes

#### Financial Domain Relevance:
In banking applications, HTTP client configuration is critical:
- Timeouts prevent hanging transactions
- Retry policies handle transient failures
- Connection pooling optimizes resource usage
- The `FactoryBean` pattern makes these concerns explicit and maintainable

---

### 3. Startup Runner Choice: Why ApplicationRunner?

**Question:** Justify using an `ApplicationRunner` for initial data ingestion over a simpler `@PostConstruct` method.

**Answer:**

The `ApplicationRunner` was chosen over `@PostConstruct` for several architectural and operational reasons:

#### Application Lifecycle Guarantees:
- **Full Context Initialization**: `ApplicationRunner` executes AFTER the entire Spring application context is fully initialized. This guarantees all beans, including WebClient, strategies, and data store, are ready.
- **@PostConstruct Risks**: `@PostConstruct` runs during bean initialization, which can lead to circular dependencies or accessing beans that aren't fully initialized yet.
- **Command-Line Access**: `ApplicationRunner` provides access to application arguments, enabling dynamic behavior based on startup parameters.

#### Error Handling and Reliability:
- **Startup Failure Control**: If data initialization fails in `ApplicationRunner`, we can fail the entire application startup with clear error messages. With `@PostConstruct`, failures might be swallowed or lead to partially initialized states.
- **Transaction Context**: `ApplicationRunner` executes within a proper transaction context (if needed), while `@PostConstruct` methods don't have transactional guarantees.
- **Exception Propagation**: Exceptions in `ApplicationRunner` are clearly propagated and logged, preventing the application from starting in an invalid state.

#### Operational Excellence:
- **Health Check Integration**: The application won't pass health checks until `ApplicationRunner` completes, ensuring no requests are served with incomplete data.
- **Ordered Execution**: Multiple runners can be ordered via `@Order` annotation, allowing complex initialization sequences (e.g., fetch data, then validate, then warm caches).
- **Monitoring and Logging**: `ApplicationRunner` execution is clearly visible in startup logs, making it easy to debug initialization issues in production.

#### Production Scenario:
Consider a production deployment:

**With @PostConstruct:**
```java
@PostConstruct
public void init() {
    fetchData(); // Might run before WebClient is fully configured
    // Application starts serving requests immediately
    // Some requests might hit null data
}
```

**With ApplicationRunner:**
```java
@Override
public void run(ApplicationArguments args) {
    log.info("=== Starting Data Initialization ===");
    fetchAllData(); // All beans guaranteed ready
    dataStore.markInitialized();
    log.info("=== Application Ready ===");
    // Load balancer can now route traffic
}
```

#### Financial Domain Implications:
In banking systems:
- **Data Consistency**: Exchange rate data MUST be available before serving any requests. `ApplicationRunner` guarantees this.
- **Audit Trail**: Clear startup logs show exactly when rates were fetched and what spread factors are in use.
- **Zero Downtime Deploys**: Health checks can verify data initialization before routing production traffic.

The `ApplicationRunner` approach transforms data initialization from a technical implementation detail into a first-class operational concern with proper observability and failure handling.

---

## 📝 Implementation Checklist

- [x] Strategy Pattern for resource handling
- [x] Custom FactoryBean for WebClient
- [x] ApplicationRunner for startup data loading
- [x] Thread-safe in-memory data store
- [x] Personalized spread calculation
- [x] Comprehensive unit tests
- [x] Integration tests
- [x] Error handling and logging
- [x] Configuration externalization
- [x] Complete documentation


## Disclaimer
This README.md has been generated using Claude AI.