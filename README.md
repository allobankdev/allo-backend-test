# Finance API - Documentation

## 📋 Table of Contents
- [Setup & Run Instructions](#setup--run-instructions)
- [Endpoint Usage](#endpoint-usage)
- [Personalization Note](#personalization-note)
- [Architectural Rationale](#architectural-rationale)

---

## 🚀 Setup & Run Instructions

### Prerequisites
- **Java 21** or higher
- **Maven 3.8+**
- **Git**

### Clone the Repository
```bash
git clone <repository-url>
cd allo-backend-test
```

### Build the Application
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=FinanceDataControllerIntegrationTest

# Run with coverage
mvn clean test jacoco:report
```

### Configuration
The application can be configured via `src/main/resources/application.yaml`:

```yaml
client:
  frankfurter:
    base-url: https://api.frankfurter.app
    connect-timeout: 2s
    read-timeout: 2s
    write-timeout: 2s
github:
  username: wiratama-p  # Github username for spread calculation
```

---

## 📡 Endpoint Usage

### Base URL
```
http://localhost:8080/api/finance/data
```

### 1. Latest IDR Rates
Fetches the latest exchange rates relative to Indonesian Rupiah (IDR) with calculated USD buy spread.

**Endpoint:** `GET /api/finance/data/latest_idr_rates`

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates" \
  -H "Accept: application/json"
```

**Response Example:**
```json
{
  "status": "success",
  "resourceType": "latest_idr_rates",
  "data": [
    {
      "code": "USD",
      "rate": 0.000063,
      "usdBuySpreadIdr": 15874.761905
    },
    {
      "code": "EUR",
      "rate": 0.000058
    },
    {
      "code": "GBP",
      "rate": 0.000050
    }
  ]
}
```

### 2. Historical IDR to USD Rates
Fetches historical exchange rate data for IDR to USD conversion over a specific date range (2024-01-01 to 2024-01-05).

**Endpoint:** `GET /api/finance/data/historical_idr_usd`

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd" \
  -H "Accept: application/json"
```

**Response Example:**
```json
{
  "status": "success",
  "resourceType": "historical_idr_usd",
  "data": [
    {
      "date": "2024-01-01",
      "rate": 0.000062
    },
    {
      "date": "2024-01-02",
      "rate": 0.000063
    },
    {
      "date": "2024-01-03",
      "rate": 0.000064
    }
  ]
}
```

### 3. Supported Currencies
Fetches the list of all supported currency codes and names.

**Endpoint:** `GET /api/finance/data/supported_currencies`

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/finance/data/supported_currencies" \
  -H "Accept: application/json"
```

**Response Example:**
```json
{
  "status": "success",
  "resourceType": "supported_currencies",
  "data": [
    {
      "code": "USD",
      "name": "United States Dollar"
    },
    {
      "code": "EUR",
      "name": "Euro"
    },
    {
      "code": "IDR",
      "name": "Indonesian Rupiah"
    }
  ]
}
```

### Error Response
When an unsupported resource type is requested:

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/finance/data/invalid_type" \
  -H "Accept: application/json"
```

**Response (400 Bad Request):**
```json
{
  "status": "failed",
  "resourceType": "invalid_type",
  "message": "resourceType invalid_type not supported",
  "data": []
}
```

---

## 🔑 Personalization Note

### GitHub Username
**Username:** `wiratama-p`

### Spread Factor Calculation

The spread factor is calculated based on the GitHub username using the following algorithm:

1. **Convert username to lowercase:** `wiratama-p`
2. **Calculate Unicode (ASCII) sum of all characters:**
   ```
   w = 119
   i = 105
   r = 114
   a = 97
   t = 116
   a = 97
   m = 109
   a = 97
   - = 45
   p = 112
   ───────
   Sum = 1011
   ```

3. **Apply the spread factor formula:**
   ```
   Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
   Spread Factor = (1011 % 1000) / 100000.0
   Spread Factor = 11 / 100000.0
   Spread Factor = 0.00011
   ```

### USD Buy Spread IDR Calculation

For the latest IDR rates endpoint, the `USD_BuySpread_IDR` is calculated as:

```
USD_BuySpread_IDR = (1 / Rate_USD) × (1 + Spread Factor)
```

**Example with Rate_USD = 0.000063:**
```
USD_BuySpread_IDR = (1 / 0.000063) × (1 + 0.00011)
USD_BuySpread_IDR = 15873.015873 × 1.00011
USD_BuySpread_IDR = 15874.761905
```

This personalized spread factor ensures each developer gets a unique result based on their GitHub username.

---

## 🛠️ Architectural Rationale

### 1. Polymorphism Justification: Strategy Pattern

**Why Strategy Pattern over simple conditional blocks?**

The Strategy Pattern was chosen for handling the three different resource types (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) instead of using `if/else` or `switch` statements for several compelling reasons:

#### **Extensibility Benefits:**

1. **Open/Closed Principle:** New resource types can be added by simply creating a new strategy class without modifying existing code. Adding a fourth resource type (e.g., `weekly_trends`) requires:
   - Creating a new `WeeklyTrendsStrategy` implementing `IDRDataFetcher`
   - Spring automatically registers it in the strategy map
   - Zero changes to the controller or other strategies

2. **Independent Development:** Each strategy can be developed, tested, and maintained independently. Teams can work on different strategies in parallel without merge conflicts.

3. **Easy Configuration:** Strategies can be enabled/disabled via configuration without code changes. For example, feature flags can control which strategies are available.

#### **Maintainability Benefits:**

1. **Single Responsibility:** Each strategy class handles only one resource type, making the codebase easier to understand and debug. Compare:
   ```java
   // ❌ Without Strategy Pattern
   public Response fetchData(String resourceType) {
     if (resourceType.equals("latest_idr_rates")) {
       // 50 lines of latest rates logic
     } else if (resourceType.equals("historical_idr_usd")) {
       // 40 lines of historical logic
     } else if (resourceType.equals("supported_currencies")) {
       // 30 lines of currencies logic
     }
     // 120+ lines in one method!
   }
   
   // ✅ With Strategy Pattern
   public Response fetch() {
     // 20 lines per strategy class
     // Each class is focused and cohesive
   }
   ```

2. **Testability:** Each strategy can be unit tested in isolation with focused test cases. Mock dependencies are minimal and specific to each strategy.

3. **Reduced Complexity:** The controller remains simple and delegates to strategies via map lookup. No complex conditional logic clutters the controller layer.

4. **Type Safety:** Compile-time checking ensures all strategies implement the required interface correctly. The IDE can assist with refactoring across all strategies.

#### **Implementation Excellence:**

The strategy map is auto-populated by Spring:
```java
@Bean
public Map<String, IDRDataFetcher> idrDataFetcherStrategies(List<IDRDataFetcher> strategies) {
  return strategies.stream()
    .collect(Collectors.toMap(
      IDRDataFetcher::getResourceType,
      Function.identity()
    ));
}
```

The controller performs zero conditional logic:
```java
IDRDataFetcher strategy = idrDataFetcherStrategies.get(resourceType);
return strategy.fetch();
```

**Conclusion:** The Strategy Pattern provides superior extensibility, maintainability, and testability compared to conditional blocks, making it the ideal choice for this multi-resource API.

---

### 2. Client Factory: FactoryBean Pattern

**Why use FactoryBean instead of a standard @Bean method?**

The `FrankfurterClientFactoryBean` implements Spring's `FactoryBean<WebClient>` interface to create the external API client. This approach offers specific advantages over a simple `@Bean` method:

#### **Benefits of FactoryBean:**

1. **Complex Initialization Logic:**
   - The `WebClient` requires complex configuration: timeouts, connection settings, codec customization, and base URL setup
   - FactoryBean encapsulates this complexity in a dedicated, reusable component
   - The factory can perform multi-step initialization that would clutter a simple `@Bean` method

2. **Lazy Initialization Control:**
   - FactoryBean provides explicit control over when the WebClient is created via `getObject()`
   - The `isSingleton()` method explicitly declares the bean scope
   - Spring can optimize bean creation based on FactoryBean metadata

3. **Configuration Externalization:**
   ```java
   @Component
   public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {
     private final FrankfurterClientProperties properties;
     
     @Override
     public WebClient getObject() {
       return WebClient.builder()
         .clientConnector(createClientConnector(
           properties.getReadTimeout(),
           properties.getWriteTimeout(),
           properties.getConnectTimeout()))
         .baseUrl(properties.getBaseUrl())
         .exchangeStrategies(...)
         .build();
     }
   }
   ```
   The factory bean cleanly separates configuration management from bean creation logic.

4. **Reusability & Testing:**
   - The factory can be reused to create multiple client instances if needed
   - Static factory methods (e.g., `createClientConnector`) can be tested independently
   - Mock factories can be created for testing without affecting the actual implementation

5. **Type Safety:**
   - `FactoryBean<WebClient>` explicitly declares what type of bean is produced
   - The `getObjectType()` method provides runtime type information
   - Spring's dependency injection can validate types at context initialization

6. **Advanced Scenarios:**
   - FactoryBean supports conditional bean creation based on runtime conditions
   - Can implement custom destruction logic via `DisposableBean`
   - Allows for proxy creation or wrapper objects if needed in the future

#### **Why NOT just use @Bean?**

A simple `@Bean` method would work but lacks:
- Explicit separation of concerns (configuration vs. creation)
- Reusability of factory logic across different contexts
- Type metadata that Spring can leverage for advanced scenarios
- The ability to cleanly unit test factory logic independently

**Example comparison:**
```java
// ❌ Simple @Bean - cluttered configuration class
@Configuration
public class WebClientConfig {
  @Bean
  public WebClient frankfurterClient(FrankfurterClientProperties props) {
    // 40+ lines of configuration logic here
    // Mixes configuration concerns with bean definition
  }
}

// ✅ FactoryBean - clean separation
@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {
  // Dedicated component for client creation
  // Testable, reusable, clear purpose
}
```

**Conclusion:** FactoryBean provides superior encapsulation, testability, and maintainability for complex bean creation scenarios like WebClient configuration.

---

### 3. Startup Runner Choice: ApplicationRunner

**Why ApplicationRunner over @PostConstruct?**

The `InitialFinanceDataRunner` implements `ApplicationRunner` to fetch and cache data on startup. This choice was deliberate and superior to using `@PostConstruct` for several reasons:

#### **Advantages of ApplicationRunner:**

1. **Guaranteed Context Initialization:**
   - ApplicationRunner executes **after** the entire Spring context is fully initialized
   - All beans are created, all dependencies are injected, all `@PostConstruct` methods have completed
   - Ensures the WebClient, repository, and all strategies are ready
   
   ```
   Spring Lifecycle Order:
   1. Constructor injection
   2. @PostConstruct methods
   3. afterPropertiesSet() (InitializingBean)
   4. ApplicationRunner.run() ✅ <- Safest point
   ```

2. **Access to Command Line Arguments:**
   ```java
   @Override
   public void run(ApplicationArguments args) {
     // Can access command line arguments if needed
     if (args.containsOption("skip-cache")) {
       return; // Skip initialization for certain profiles
     }
   }
   ```
   This enables flexible startup behavior based on deployment scenarios.

3. **Ordered Execution:**
   - Multiple ApplicationRunner beans can be ordered using `@Order`
   - Allows orchestrating complex initialization sequences
   - Example: Load currencies first, then fetch rates
   
   ```java
   @Component
   @Order(1)
   public class CurrencyLoaderRunner implements ApplicationRunner { }
   
   @Component
   @Order(2)
   public class RatesLoaderRunner implements ApplicationRunner { }
   ```

4. **Error Handling & Application Lifecycle:**
   - Exceptions in ApplicationRunner prevent the application from starting (fail-fast)
   - With `@PostConstruct`, the application might start with incomplete data
   - Better aligns with the requirement that data must be available before serving requests

5. **Reactive Operations Support:**
   ```java
   @Override
   public void run(ApplicationArguments args) {
     Mono.when(
       fetchLatestRates(),
       fetchHistoricalRates(),
       fetchCurrencies()
     )
     .then(repository.lock())
     .block(); // Explicitly block during startup
   }
   ```
   The runner provides a clear point where blocking is acceptable and expected.

#### **Why NOT @PostConstruct?**

`@PostConstruct` has limitations:

1. **Timing Uncertainty:**
   - Executes during bean initialization, not after full context startup
   - Other beans might not be fully ready
   - Can cause subtle initialization order bugs

2. **No Access to Arguments:**
   - Cannot respond to command-line flags or deployment configurations
   - Less flexible for different environments (dev/staging/prod)

3. **Silent Failures:**
   - Application might start even if `@PostConstruct` fails (depending on configuration)
   - Data might be incomplete, leading to runtime errors later

4. **Not Designed for Heavy Operations:**
   - `@PostConstruct` is meant for lightweight initialization
   - Fetching external API data and populating caches is a heavy operation
   - ApplicationRunner is explicitly designed for such startup tasks

#### **Real-World Scenario:**

Consider a production deployment:
- **With @PostConstruct:** App starts, health checks pass, but cache is empty → 500 errors on first requests
- **With ApplicationRunner:** App doesn't mark as "ready" until cache is populated → Zero downtime deployment

**Conclusion:** ApplicationRunner provides guaranteed execution order, access to command-line arguments, proper error handling, and explicit support for startup data loading. It's the architecturally correct choice for initializing critical application state from external sources.

---

## 🎯 Summary

This implementation demonstrates production-ready Spring Boot development with:
- ✅ **Strategy Pattern** for extensible resource handling
- ✅ **FactoryBean** for sophisticated client configuration
- ✅ **ApplicationRunner** for reliable startup initialization
- ✅ **Reactive Programming** with Project Reactor
- ✅ **Thread-Safe In-Memory Caching** with immutability guarantees
- ✅ **Comprehensive Integration Testing** with WireMock
- ✅ **Personalized Calculations** based on GitHub username

The architecture prioritizes **extensibility**, **maintainability**, and **testability** while adhering to SOLID principles and Spring Boot best practices.

