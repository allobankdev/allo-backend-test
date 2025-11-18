# Allo Bank Backend Test - Implementation Plan

## 1. API Analysis Results

### Endpoint 1: Latest IDR Rates
**URL:** `https://api.frankfurter.app/latest?base=IDR`

**Response Structure:**
```json
{
  "amount": 1.0,
  "base": "IDR",
  "date": "2025-11-17",
  "rates": {
    "USD": 6.0e-05,
    "EUR": 5.2e-05,
    "GBP": 4.5e-05,
    // ... 28 more currencies
  }
}
```

### Endpoint 2: Historical IDR to USD
**URL:** `https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD`

**Response Structure:**
```json
{
  "amount": 1.0,
  "base": "IDR",
  "start_date": "2023-12-29",
  "end_date": "2024-01-05",
  "rates": {
    "2023-12-29": {"USD": 6.5e-05},
    "2024-01-02": {"USD": 6.4e-05},
    "2024-01-03": {"USD": 6.4e-05},
    "2024-01-04": {"USD": 6.4e-05},
    "2024-01-05": {"USD": 6.4e-05}
  }
}
```

### Endpoint 3: Supported Currencies
**URL:** `https://api.frankfurter.app/currencies`

**Response Structure:**
```json
{
  "AUD": "Australian Dollar",
  "USD": "United States Dollar",
  "EUR": "Euro",
  // ... 29 more currencies
}
```

---

## 2. Personalization: Spread Factor Calculation

**GitHub Username:** `RadityaDito`

**Calculation Steps:**
1. Convert to lowercase: `radityadito`
2. Calculate sum of Unicode values:
   - r(114) + a(97) + d(100) + i(105) + t(116) + y(121) + a(97) + d(100) + i(105) + t(116) + o(111)
   - Sum = 1182
3. Apply formula: `(1182 % 1000) / 100000.0`
4. **Spread Factor = 0.00182**

**USD_BuySpread_IDR Formula:**
```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00182)
```

Example: If Rate_USD = 6.0e-05 (0.00006)
```
USD_BuySpread_IDR = (1 / 0.00006) * (1 + 0.00182)
                  = 16666.67 * 1.00182
                  = 16696.97
```

---

## 3. Technology Stack

### Core Dependencies
- **Spring Boot:** 3.5.7
- **Java:** 21
- **HTTP Client:** WebClient (Spring WebFlux) - reactive, non-blocking, modern
- **Lombok:** For reducing boilerplate code
- **Jackson:** For JSON serialization/deserialization

### Testing Dependencies
- **JUnit 5:** Unit testing framework
- **Mockito:** Mocking framework
- **MockWebServer (OkHttp):** For mocking HTTP responses
- **Spring Boot Test:** Integration testing

### Configuration
- **application.yml:** Externalized configuration for API URL, timeouts

---

## 4. Architecture & Design

### 4.1 Package Structure
```
com.allo.backend.test.code/
├── config/
│   └── WebClientFactoryBean.java          # FactoryBean for WebClient
├── controller/
│   └── FinanceDataController.java         # REST controller
├── service/
│   ├── DataStorageService.java            # Thread-safe in-memory store
│   ├── startup/
│   │   └── DataInitializationRunner.java  # ApplicationRunner
│   └── strategy/
│       ├── DataFetcherStrategy.java       # Strategy interface
│       ├── LatestIDRRatesStrategy.java    # Strategy 1
│       ├── HistoricalIDRUSDStrategy.java  # Strategy 2
│       └── SupportedCurrenciesStrategy.java # Strategy 3
├── model/
│   ├── dto/
│   │   ├── LatestRatesResponse.java       # API response DTOs
│   │   ├── HistoricalRatesResponse.java
│   │   └── CurrenciesResponse.java
│   └── domain/
│       ├── LatestRatesData.java           # Domain models
│       ├── HistoricalRatesData.java
│       └── CurrenciesData.java
└── util/
    └── SpreadFactorCalculator.java        # Utility for spread calculation
```

### 4.2 Strategy Pattern Implementation

**Strategy Interface:**
```java
public interface DataFetcherStrategy {
    String getResourceType();
    Object fetchData(WebClient webClient);
}
```

**Three Concrete Strategies:**
1. **LatestIDRRatesStrategy** - Handles `latest_idr_rates`
   - Fetches from `/latest?base=IDR`
   - Calculates `USD_BuySpread_IDR` with spread factor

2. **HistoricalIDRUSDStrategy** - Handles `historical_idr_usd`
   - Fetches from `/2024-01-01..2024-01-05?from=IDR&to=USD`
   - Minimal transformation

3. **SupportedCurrenciesStrategy** - Handles `supported_currencies`
   - Fetches from `/currencies`
   - Minimal transformation

**Strategy Resolution:**
- Controller receives `resourceType` parameter
- Uses `Map<String, DataFetcherStrategy>` injected by Spring
- No manual if/else or switch statements

### 4.3 FactoryBean for WebClient

**Purpose:**
- Encapsulate WebClient creation logic
- Externalize base URL configuration
- Apply common configurations (timeouts, headers)

**Implementation:**
```java
@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

### 4.4 In-Memory Storage (Thread-Safe & Immutable)

**Requirements:**
- Store data for all 3 resources
- Thread-safe access
- Immutable after initialization

**Implementation:**
```java
@Service
public class DataStorageService {
    private volatile Map<String, Object> dataStore = new ConcurrentHashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void storeData(String resourceType, Object data) {
        if (initialized.get()) {
            throw new IllegalStateException("Data already initialized");
        }
        dataStore.put(resourceType, data);
    }

    public void markAsInitialized() {
        initialized.set(true);
        // Make store immutable
        dataStore = Collections.unmodifiableMap(dataStore);
    }

    public Object getData(String resourceType) {
        if (!initialized.get()) {
            throw new IllegalStateException("Data not yet initialized");
        }
        return dataStore.get(resourceType);
    }
}
```

### 4.5 ApplicationRunner for Startup Data Loading

**Purpose:**
- Fetch all 3 resources on startup
- Load into in-memory store
- Ensure completion before serving requests

**Implementation:**
```java
@Component
@Order(1)
public class DataInitializationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        // Fetch all 3 resources using strategies
        // Store in DataStorageService
        // Mark as initialized
    }
}
```

---

## 5. Implementation Steps

### Phase 1: Project Setup & Configuration
1. ✅ Update `build.gradle` with dependencies
2. ✅ Create `application.yml` with Frankfurter API configuration
3. ✅ Setup Lombok annotation processor

### Phase 2: Core Components
4. ✅ Implement `SpreadFactorCalculator` utility
5. ✅ Create DTO classes for API responses
6. ✅ Create domain models for stored data
7. ✅ Implement `WebClientFactoryBean`

### Phase 3: Strategy Pattern
8. ✅ Define `DataFetcherStrategy` interface
9. ✅ Implement `LatestIDRRatesStrategy`
10. ✅ Implement `HistoricalIDRUSDStrategy`
11. ✅ Implement `SupportedCurrenciesStrategy`
12. ✅ Create strategy configuration to expose map of strategies

### Phase 4: Storage & Initialization
13. ✅ Implement `DataStorageService`
14. ✅ Implement `DataInitializationRunner`

### Phase 5: API Layer
15. ✅ Implement `FinanceDataController`
16. ✅ Add global exception handler

### Phase 6: Testing
17. ✅ Unit tests for `SpreadFactorCalculator`
18. ✅ Unit tests for all 3 strategy implementations
19. ✅ Integration test for `DataInitializationRunner`
20. ✅ Integration test for controller endpoint

### Phase 7: Documentation
21. ✅ Update README.md with complete documentation
22. ✅ Add architectural rationale section
23. ✅ Add cURL examples

---

## 6. API Endpoint Design

**Endpoint:** `GET /api/finance/data/{resourceType}`

**Valid resourceType values:**
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

**Response Format:**
- Always returns JSON
- Structure varies by resource type
- Includes metadata (timestamp, source)

**Error Handling:**
- 400 Bad Request: Invalid resourceType
- 500 Internal Server Error: Data not initialized or fetch failed
- 503 Service Unavailable: External API unreachable

---

## 7. Testing Strategy

### Unit Tests
**File:** `SpreadFactorCalculatorTest.java`
- Test spread factor calculation for known usernames
- Verify edge cases (empty string, special characters)

**File:** `LatestIDRRatesStrategyTest.java`
- Mock WebClient to return fake API response
- Verify USD_BuySpread_IDR calculation
- Test error handling for API failures

**File:** `HistoricalIDRUSDStrategyTest.java`
- Mock WebClient
- Verify data transformation
- Test error handling

**File:** `SupportedCurrenciesStrategyTest.java`
- Mock WebClient
- Verify currency list parsing
- Test error handling

### Integration Tests
**File:** `DataInitializationRunnerIT.java`
- Use MockWebServer to simulate Frankfurter API
- Verify runner fetches all 3 resources
- Verify data stored in DataStorageService
- Verify immutability after initialization

**File:** `FinanceDataControllerIT.java`
- Test all 3 resource types via REST endpoint
- Verify correct strategy selection
- Test invalid resourceType handling

---

## 8. Configuration (application.yml)

```yaml
server:
  port: 8080

frankfurter:
  api:
    base-url: https://api.frankfurter.app
    timeout: 5000

github:
  username: RadityaDito

logging:
  level:
    com.allo.backend.test: DEBUG
    org.springframework.web: INFO
```

---

## 9. Dependencies to Add (build.gradle)

```gradle
dependencies {
    // Web & WebFlux
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'io.projectreactor:reactor-test'
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.12.0'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

---

## 10. Commit Strategy

### Atomic, Descriptive Commits:
1. `chore: Add WebFlux and Lombok dependencies to build.gradle`
2. `feat: Implement SpreadFactorCalculator utility with GitHub username`
3. `feat: Create DTO models for Frankfurter API responses`
4. `feat: Implement WebClientFactoryBean for client creation`
5. `feat: Define DataFetcherStrategy interface`
6. `feat: Implement LatestIDRRatesStrategy with spread calculation`
7. `feat: Implement HistoricalIDRUSDStrategy`
8. `feat: Implement SupportedCurrenciesStrategy`
9. `feat: Create thread-safe DataStorageService`
10. `feat: Implement DataInitializationRunner for startup data load`
11. `feat: Create FinanceDataController with strategy pattern`
12. `feat: Add global exception handler`
13. `test: Add unit tests for SpreadFactorCalculator`
14. `test: Add unit tests for all strategy implementations`
15. `test: Add integration tests for DataInitializationRunner`
16. `test: Add integration tests for FinanceDataController`
17. `docs: Update README with setup, usage, and architectural rationale`

---

## 11. Architectural Rationale (For README & PR)

### Question 1: Why Strategy Pattern?
**Answer:** The Strategy Pattern provides extensibility and maintainability by:
- **Open/Closed Principle:** Adding new resource types requires only a new strategy class without modifying existing controller code
- **Single Responsibility:** Each strategy handles one specific resource type's logic
- **Testability:** Each strategy can be tested in isolation with mocked dependencies
- **Maintainability:** Changes to one resource's logic don't affect others
- **Scalability:** Easy to add new resources (e.g., `crypto_rates`, `commodity_prices`) by implementing the interface

Alternative (if/else in service) would create a monolithic method that violates SRP and becomes harder to maintain as requirements grow.

### Question 2: Why FactoryBean?
**Answer:** Using FactoryBean for WebClient provides:
- **Separation of Concerns:** Encapsulates complex client creation logic
- **Lifecycle Management:** Spring manages the singleton lifecycle
- **Configuration Flexibility:** Easy to add interceptors, filters, or custom configurations
- **Testability:** Can inject different implementations in tests
- **Enterprise Pattern:** Demonstrates understanding of Spring's advanced configuration mechanisms

Standard @Bean would work but FactoryBean provides more control and demonstrates architectural sophistication.

### Question 3: Why ApplicationRunner?
**Answer:** ApplicationRunner is preferred over @PostConstruct because:
- **Startup Order Control:** ApplicationRunner runs after all beans are initialized and configured
- **Command-Line Arguments:** Access to parsed application arguments if needed
- **Clear Intent:** Explicitly signals "startup task" vs generic post-construction
- **Error Handling:** Failures prevent application from starting (fail-fast principle)
- **Testability:** Easier to test startup logic in isolation

@PostConstruct runs during bean construction, which may occur before all dependencies are ready, risking race conditions.

---

## 12. Next Steps

Once you approve this plan, we'll proceed with implementation in the following order:
1. Update build.gradle and application.yml
2. Implement utilities and DTOs
3. Build strategy pattern components
4. Create storage and initialization logic
5. Implement controller and exception handling
6. Write comprehensive tests
7. Update documentation

Ready to start coding! 🚀
