# ✅ PHASE 1 — Project Setup (Foundation First)

### 1️⃣ Create Project

Use:

* Spring Boot 3.x
* Java 17 or 21
* Maven
* Dependencies:

  * spring-boot-starter-webflux (use `WebClient`)
  * spring-boot-starter-validation
  * spring-boot-starter-test
  * lombok (optional but clean)
  * mockito
  * junit-jupiter

Why WebFlux?
Because it shows modern stack knowledge + better testability for client mocking.

---

### 2️⃣ Define application.yml

```yaml
external:
  frankfurter:
    base-url: https://api.frankfurter.app

app:
  github-username: yourgithubusername
```

Never hardcode base URLs.

---

# ✅ PHASE 2 — Architecture Design (Before Writing Logic)

You need this package structure:

```
com.allobank.finance
 ├── controller
 ├── strategy
 │    ├── IDRDataFetcher.java
 │    ├── LatestIdrRatesFetcher.java
 │    ├── HistoricalIdrUsdFetcher.java
 │    └── SupportedCurrenciesFetcher.java
 ├── client
 │    └── FrankfurterClientFactoryBean.java
 ├── config
 ├── runner
 ├── store
 ├── model
 └── exception
```

---

# ✅ PHASE 3 — Implement Strategy Pattern (Core Requirement)

## 3.1 Define Strategy Interface

```java
public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
```

Notice:

* `getResourceType()` allows Spring to map it
* `fetchData()` returns raw result object

---

## 3.2 Implement 3 Concrete Strategies

### A) LatestIdrRatesFetcher

* Calls `/latest?base=IDR`
* Extract USD rate
* Compute Spread
* Add `USD_BuySpread_IDR`

Spread calculation:

```java
private double calculateSpreadFactor(String username) {
    int sum = username.toLowerCase()
        .chars()
        .sum();

    return (sum % 1000) / 100000.0;
}
```

Final formula:

```java
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + spreadFactor)
```

Return structured DTO.

---

### B) HistoricalIdrUsdFetcher

Call:

```
/2024-01-01..2024-01-05?from=IDR&to=USD
```

Return minimal transformation.

---

### C) SupportedCurrenciesFetcher

Call:

```
/currencies
```

Return as map or array.

---

# ✅ PHASE 4 — Strategy Injection via Map (NO if/else Allowed)

In service layer:

```java
@Service
public class FinanceDataService {

    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceDataService(List<IDRDataFetcher> strategies) {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(
                IDRDataFetcher::getResourceType,
                Function.identity()
            ));
    }

    public Object getData(String resourceType) {
        IDRDataFetcher fetcher = strategyMap.get(resourceType);

        if (fetcher == null) {
            throw new IllegalArgumentException("Invalid resource type");
        }

        return fetcher.fetchData();
    }
}
```

Controller is now clean:

```java
@GetMapping("/api/finance/data/{resourceType}")
public ResponseEntity<?> getData(@PathVariable String resourceType) {
    return ResponseEntity.ok(service.getData(resourceType));
}
```

✅ No switch
✅ No if/else
✅ Fully polymorphic

---

# ✅ PHASE 5 — FactoryBean for WebClient (Important!)

This is heavily weighted.

### Implement:

```java
public class FrankfurterClientFactoryBean 
        implements FactoryBean<WebClient> {
```

Inject base URL using `@Value`.

Inside:

```java
@Override
public WebClient getObject() {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
}
```

Register it:

```java
@Bean
public FactoryBean<WebClient> frankfurterClient() {
    return new FrankfurterClientFactoryBean();
}
```

Why this is important:

* Encapsulates client creation
* Centralizes configuration
* Shows deep Spring knowledge

---

# ✅ PHASE 6 — Startup Data Loading (CRITICAL)

This part differentiates juniors from seniors.

## 6.1 Create In-Memory Store

```java
@Component
public class FinanceDataStore {

    private Map<String, Object> data;

    public synchronized void initialize(Map<String, Object> loadedData) {
        this.data = Map.copyOf(loadedData); // Immutable
    }

    public Object get(String key) {
        return data.get(key);
    }
}
```

Key points:

* `Map.copyOf()` ensures immutability
* No setters afterward
* Thread-safe

---

## 6.2 ApplicationRunner

```java
@Component
public class DataLoaderRunner implements ApplicationRunner {
```

In `run()`:

* Loop through all strategies
* Fetch data
* Put into map
* Store in `FinanceDataStore`

Now:

* External API called only once
* Endpoint serves memory only

---

# ✅ PHASE 7 — Error Handling

Create:

```
@ExceptionHandler(WebClientResponseException.class)
```

Return proper JSON error.

Add:

* Timeout config in WebClient
* Graceful fallback on startup failure (fail fast is better for banking)

---

# ✅ PHASE 8 — Testing Strategy (Very Important)

## Unit Tests (3 Required)

For each Strategy:

* Mock WebClient
* Mock API response
* Verify:

  * correct parsing
  * spread calculation
  * edge cases

Test spread calculation separately.

---

## Integration Test

Use:

```java
@SpringBootTest
```

Verify:

* Application starts
* `FinanceDataStore` contains 3 entries
* Endpoint returns stored data
* No external call triggered after startup

You can mock WebClient with `@TestConfiguration`.

---

# ✅ PHASE 9 — README (This is where candidates fail)

Your README must include:

---

## 1️⃣ Setup

```
git clone
mvn clean install
mvn spring-boot:run
```

---

## 2️⃣ Sample cURL

```
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## 3️⃣ Spread Personalization

Example:

```
GitHub Username: aryaevan
Unicode Sum: XXXX
Spread Factor: 0.00765
```

Show the calculation.

---

## 4️⃣ Architectural Rationale

This is how you should answer:

### Strategy Pattern Justification

* Eliminates conditional branching
* Open/Closed Principle compliant
* Easy to add new resource type
* Controller stays thin
* Encourages test isolation

Mention SOLID explicitly.

---

### FactoryBean Justification

* Encapsulates complex instantiation logic
* Allows environment-driven configuration
* Promotes reusability
* Keeps client lifecycle controlled by Spring container

---

### ApplicationRunner vs @PostConstruct

Say this:

* `ApplicationRunner` runs after full context initialization
* Better lifecycle control
* Allows ordering
* Supports dependency readiness
* Cleaner for startup workflows

This shows maturity.

---


# 🚀 Final Advice (From Lead Perspective)

This test evaluates:

* Polymorphism depth
* Spring container mastery
* Immutability understanding
* Production mindset
* Testing discipline

Do NOT:

* Put logic in controller
* Skip immutability
* Skip integration test
* Skip rationale section

---
