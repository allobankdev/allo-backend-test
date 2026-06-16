# Allo Bank Backend Developer Take-Home Test

Welcome, and thank you for your interest in joining Allo Bank Engineering!

This challenge is intentionally open-ended. There is no skeleton, no guided steps, and no single correct answer. We want to see how you think, how you structure a solution, and what you consider important in production-grade code.

---

## The Challenge: Split Bill API

Build a **Spring Boot REST API** that helps a group of people manage shared expenses and calculate who owes whom at the end.

Think of a real scenario: a group trip, a team lunch, a shared apartment. People take turns paying for things, and at the end someone needs to figure out the fairest way to settle up.

**Your API should, at minimum, support:**

1. Creating a bill group with a name and a list of participants
2. Adding expenses to a group — who paid, how much, and who it was for
3. Retrieving a settlement summary — a clear breakdown of who owes whom and how much

Everything else is up to you.

---

## Technical Requirements

These are non-negotiable:

- **Java 17+**, **Spring Boot**, **Maven**
- **`BigDecimal`** for all monetary values — no `float` or `double`
- **A `Dockerfile`** using a multi-stage build (see `Dockerfile.template` in this repo)
- At least **one unit test** covering your settlement calculation logic
- A **`README.md`** in your submission with:
  - How to build and run your project
  - Example `curl` commands for each endpoint
  - Your **GitHub username** and your calculated **service charge** value (see Personalization section below)
  - Answer to the submission question (see below)

---

## Personalization

Every settlement response must include two additional fields: `service_charge_pct` and `service_charge_amount`.

The `service_charge_pct` is unique to you and is calculated as follows:

1. Take your GitHub username in **lowercase**
2. Sum the Unicode (ASCII) values of all characters
3. `service_charge_pct = (sum % 10)` — this gives a value between 0 and 9 (representing a percentage)

**Example:** GitHub username `johndoe47`
- Unicode sum: `106+111+104+110+100+111+101+52+55` = `850`
- `service_charge_pct = 850 % 10` = **0** (0%)

The `service_charge_amount` is this percentage applied to the total group expenses.

Include both fields in your settlement response. This value must be computed in code — do not hardcode it.

---

## Show Your Skills

The minimum requirements get you through the door. What you build beyond that is how you stand out.

Some directions to explore — pick what interests you, or invent your own:

- **Multiple split strategies** — equal split, split by percentage, split by exact amount per person
- **Settlement optimization** — minimize the total number of transactions needed to settle all debts
- **Payment recording** — mark a debt as paid and update outstanding balances
- **Expense categories** — tag expenses (food, transport, accommodation) and show per-category summaries
- **Audit trail** — track when expenses and payments were added

There is no bonus point checklist. We are looking at the quality of what you choose to build, not the quantity.

---

## Submission Question

In your `README.md`, answer the following in a short paragraph (3–5 sentences):

> **"What was the hardest design decision you made while building this, and what trade-off did you accept?"**

There is no wrong answer. We ask this because it tells us more about how you think than the code itself.

---

## Submission Process

1. **Create a private GitHub repository** for your solution
2. **Add `allobankdev` as a collaborator** (Settings → Collaborators → Add people)
3. **Include a `Dockerfile`** in the root of your project (see `Dockerfile.template`)
4. **Submit via the form:** [Click Here](https://forms.gle/nZKQ2EjTCPfAKHog7)

   The form will ask for:
   - Your full name and contact details
   - Your private GitHub repository URL
   - Your GitHub username (for personalization verification)

> Do not open a Pull Request to this repository. Submissions are private.

---

## What We Look For

| Area | What it signals |
|---|---|
| Data modeling | How you think about domain entities and relationships |
| API design | Clarity, consistency, and REST conventions |
| Monetary handling | Awareness of precision issues in financial systems |
| Code structure | Separation of concerns, readability, maintainability |
| Testing | What you consider worth testing and why |
| Submission answer | Genuine engagement with the problem |

We review every submission before the interview. The interview will include questions directly about your code — be ready to walk through it and extend it live.

Good luck!

---

# Implementation

This document describes the implementation of the Spring Boot REST API for aggregating Frankfurter Exchange Rate data.

## 📋 Table of Contents

- [Setup and Run Instructions](#setup-and-run-instructions)
- [Endpoint Usage](#endpoint-usage)
- [Personalization Note](#personalization-note)
- [Architectural Rationale](#architectural-rationale)
- [Project Structure](#project-structure)

## Setup and Run Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use Maven Wrapper)
- Internet connection (for fetching data from Frankfurter API)

### Building the Application

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd allo-backend-test
   ```

2. **Set your GitHub username (optional, defaults to "defaultuser"):**
   ```bash
   # On Windows (PowerShell)
   $env:GITHUB_USERNAME="your-github-username"
   
   # On Linux/Mac
   export GITHUB_USERNAME="your-github-username"
   ```
   
   Alternatively, you can set it in `application.yml`:
   ```yaml
   github:
     username: your-github-username
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

### Running the Application

1. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```
   
   Or if you've built the JAR:
   ```bash
   java -jar target/allo-backend-test-1.0.0.jar
   ```

2. **Wait for initialization:**
   The application will fetch data from the Frankfurter API on startup. Wait for the log message:
   ```
   Data initialization completed. Success: 3, Failures: 0
   ```

3. **Verify the application is running:**
   The application will be available at `http://localhost:8080`

### Running Tests

1. **Run all tests:**
   ```bash
   mvn test
   ```

2. **Run specific test class:**
   ```bash
   mvn test -Dtest=SpreadCalculatorTest
   ```

3. **Run integration tests:**
   ```bash
   mvn test -Dtest=DataInitializationIntegrationTest
   ```

## Endpoint Usage

The application exposes a single REST endpoint that serves aggregated data from the Frankfurter API.

### Base URL
```
http://localhost:8080/api/finance/data
```

### Available Resource Types

1. **Latest IDR Rates** - `latest_idr_rates`
2. **Historical IDR to USD** - `historical_idr_usd`
3. **Supported Currencies** - `supported_currencies`

### Example cURL Commands

#### 1. Get Latest IDR Rates
```bash
curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates" \
  -H "Accept: application/json"
```

**Expected Response:**
```json
[
  {
    "amount": 1.0,
    "base": "IDR",
    "date": "2024-01-15",
    "rates": {
      "USD": 0.000064,
      "EUR": 0.000059,
      ...
    },
    "USD_BuySpread_IDR": 15625.015625
  }
]
```

#### 2. Get Historical IDR to USD Rates
```bash
curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd" \
  -H "Accept: application/json"
```

**Expected Response:**
```json
[
  {
    "amount": 1.0,
    "base": "IDR",
    "start_date": "2024-01-01",
    "end_date": "2024-01-05",
    "rates": {
      "2024-01-01": {
        "USD": 0.000064
      },
      "2024-01-02": {
        "USD": 0.000065
      },
      ...
    }
  }
]
```

#### 3. Get Supported Currencies
```bash
curl -X GET "http://localhost:8080/api/finance/data/supported_currencies" \
  -H "Accept: application/json"
```

**Expected Response:**
```json
[
  {
    "currencies": {
      "USD": "United States Dollar",
      "IDR": "Indonesian Rupiah",
      "EUR": "Euro",
      ...
    }
  }
]
```

### Error Responses

#### Invalid Resource Type (400 Bad Request)
```bash
curl -X GET "http://localhost:8080/api/finance/data/invalid_resource"
```

**Response:**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Invalid Resource Type",
  "message": "Resource type must be one of: latest_idr_rates, historical_idr_usd, supported_currencies",
  "path": "/api/finance/data/invalid_resource"
}
```

#### Data Not Ready (503 Service Unavailable)
This occurs if the endpoint is called before data initialization is complete.

## Personalization Note

**GitHub Username:** `defaultuser` (can be configured via environment variable `GITHUB_USERNAME` or in `application.yml`)

**Spread Factor Calculation:**
- For username `defaultuser` (lowercase): `defaultuser`
- Sum of Unicode values: `100 + 101 + 102 + 97 + 117 + 108 + 116 + 117 + 115 + 101 + 114 = 1086`
- Spread Factor: `(1086 % 1000) / 100000.0 = 86 / 100000.0 = 0.00086`

**Note:** To calculate your own spread factor, set the `GITHUB_USERNAME` environment variable or update `application.yml` with your GitHub username, then restart the application.

## Architectural Rationale

### 1. Polymorphism Justification: Why Strategy Pattern?

The Strategy Pattern was chosen over a simpler conditional block (if/else or switch) for several critical reasons:

**Extensibility:**
- Adding a new resource type requires only creating a new strategy implementation and registering it as a Spring bean. No modification to existing code is needed, following the Open/Closed Principle.
- The controller remains unchanged when new strategies are added, as Spring automatically injects all `IDRDataFetcher` implementations.

**Maintainability:**
- Each strategy encapsulates its own data fetching and transformation logic, making the codebase easier to understand and modify.
- Changes to one resource type's handling don't affect others, reducing the risk of introducing bugs.
- The clear separation of concerns makes unit testing straightforward - each strategy can be tested in isolation.

**Testability:**
- Strategies can be easily mocked and tested independently.
- The controller logic is simplified, focusing only on routing and error handling rather than business logic.

**Type Safety:**
- The interface contract ensures all strategies implement the required methods consistently.
- Compile-time checking prevents missing implementations.

**Spring Integration:**
- Leverages Spring's dependency injection to automatically discover and inject all strategy implementations.
- The map-based lookup in the controller (via Spring's `List<IDRDataFetcher>` injection) eliminates manual strategy registration code.

In contrast, a conditional block would require modifying the controller/service every time a new resource type is added, violating the Open/Closed Principle and making the code harder to maintain as it grows.

### 2. Client Factory: Why FactoryBean?

Using a `FactoryBean` to construct the `WebClient` provides several advantages over a standard `@Bean` method:

**Encapsulation of Complex Creation Logic:**
- The `FactoryBean` encapsulates all the configuration logic for creating the `WebClient` in a single, reusable component.
- This includes base URL configuration, codec settings, timeout configurations, and any future enhancements (retry logic, circuit breakers, etc.).

**Configuration Externalization:**
- The `FactoryBean` uses `@ConfigurationProperties` to externalize all API configuration (base URL, timeouts) to `application.yml`.
- This makes the configuration environment-specific and easily changeable without code modifications.

**Lifecycle Management:**
- `FactoryBean` provides fine-grained control over bean creation lifecycle through `getObject()`, `getObjectType()`, and `isSingleton()` methods.
- The singleton pattern ensures only one `WebClient` instance is created and reused across all strategies, which is more efficient than creating multiple instances.

**Testability:**
- The `FactoryBean` can be easily mocked or replaced in test configurations.
- The separation of factory logic from bean definition makes it easier to test different configurations.

**Future Extensibility:**
- If we need to create different `WebClient` instances for different purposes (e.g., different base URLs, different timeout settings), we can create multiple `FactoryBean` implementations.
- The factory pattern allows for conditional bean creation based on profiles or properties.

**Compliance with Requirements:**
- The requirement explicitly states that the client must be created within a `FactoryBean` implementation, not as a simple `@Bean` method. This ensures consistency and enforces the architectural pattern.

While a simple `@Bean` method would work functionally, the `FactoryBean` approach provides better separation of concerns, easier configuration management, and aligns with Spring's best practices for complex bean creation.

### 3. Startup Runner Choice: ApplicationRunner vs @PostConstruct

Using an `ApplicationRunner` (or `CommandLineRunner`) for initial data ingestion is preferable to `@PostConstruct` for several reasons:

**Application Context Readiness:**
- `ApplicationRunner.run()` is called **after** the entire Spring application context is fully initialized, including all beans, configurations, and lifecycle callbacks.
- `@PostConstruct` methods are called during bean initialization, which may occur before all dependencies are fully wired, potentially causing issues with complex dependency graphs.

**Error Handling and Application Startup:**
- If data initialization fails in an `ApplicationRunner`, the application can still start (depending on error handling), but we have explicit control over this behavior.
- With `@PostConstruct`, failures during bean initialization can prevent the application from starting entirely, which may be too strict for non-critical initialization tasks.

**Asynchronous Operations:**
- `ApplicationRunner` provides a natural place to handle asynchronous operations (like our reactive `WebClient` calls) with proper synchronization mechanisms (e.g., `CountDownLatch`).
- `@PostConstruct` methods are typically synchronous, making it harder to coordinate multiple async operations.

**Testing:**
- `ApplicationRunner` implementations can be easily tested in isolation or excluded from test contexts.
- Integration tests can verify that the runner executes correctly and loads data into the store.

**Lifecycle Clarity:**
- The `ApplicationRunner` makes it explicit that data loading is an application startup concern, not a bean initialization concern.
- This separation makes the codebase more maintainable and the intent clearer.

**Command-Line Arguments:**
- `ApplicationRunner` receives `ApplicationArguments`, which can be useful for conditional initialization based on command-line parameters or profiles.

**Ordering Control:**
- Multiple `ApplicationRunner` implementations can be ordered using `@Order` annotation, providing fine-grained control over initialization sequence if needed.

In our implementation, the `DataInitializationRunner` uses reactive programming with `Mono` and coordinates multiple async operations using `CountDownLatch`, which would be awkward to implement in a `@PostConstruct` method. The runner also provides better error handling and logging, ensuring that partial failures don't prevent the application from starting.

## Project Structure

```
src/
├── main/
│   ├── java/com/allobank/
│   │   ├── AlloBackendTestApplication.java       # Main Spring Boot application
│   │   ├── config/
│   │   │   ├── FrankfurterApiProperties.java     # Configuration properties
│   │   │   └── WebClientConfig.java              # WebClient bean configuration
│   │   ├── controller/
│   │   │   └── FinanceDataController.java        # REST endpoint controller
│   │   ├── dto/
│   │   │   ├── LatestRatesResponse.java          # DTOs for API responses
│   │   │   ├── HistoricalRatesResponse.java
│   │   │   ├── CurrenciesResponse.java
│   │   │   └── ApiErrorResponse.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java       # Global error handling
│   │   ├── factory/
│   │   │   └── WebClientFactoryBean.java         # FactoryBean for WebClient
│   │   ├── runner/
│   │   │   └── DataInitializationRunner.java     # ApplicationRunner for startup data loading
│   │   ├── service/
│   │   │   └── InMemoryDataStore.java            # Thread-safe in-memory data store
│   │   ├── strategy/
│   │   │   ├── IDRDataFetcher.java               # Strategy interface
│   │   │   └── impl/
│   │   │       ├── LatestIdrRatesStrategy.java
│   │   │       ├── HistoricalIdrUsdStrategy.java
│   │   │       └── SupportedCurrenciesStrategy.java
│   │   └── util/
│   │       └── SpreadCalculator.java             # Spread calculation utility
│   └── resources/
│       └── application.yml                        # Application configuration
└── test/
    ├── java/com/allobank/
    │   ├── integration/
    │   │   └── DataInitializationIntegrationTest.java
    │   ├── runner/
    │   │   └── DataInitializationRunnerTest.java
    │   ├── service/
    │   │   └── InMemoryDataStoreTest.java
    │   ├── strategy/impl/
    │   │   ├── LatestIdrRatesStrategyTest.java
    │   │   ├── HistoricalIdrUsdStrategyTest.java
    │   │   └── SupportedCurrenciesStrategyTest.java
    │   └── util/
    │       └── SpreadCalculatorTest.java
    └── resources/
        └── application-test.yml                  # Test configuration
```

## Key Design Decisions

1. **Thread-Safe Data Store:** Uses `ConcurrentHashMap` and `AtomicBoolean` to ensure thread-safe operations and immutability after initialization.

2. **Reactive Programming:** Uses Spring WebFlux's `WebClient` for non-blocking HTTP calls, improving performance and resource utilization.

3. **Error Handling:** Comprehensive error handling at multiple levels - strategy level, controller level, and global exception handler.

4. **Configuration Management:** All external API configuration is externalized to `application.yml` using `@ConfigurationProperties`.

5. **Testing:** Comprehensive unit tests for all strategies and utilities, plus integration tests to verify startup behavior.

## Future Enhancements

Potential improvements for production use:

- Add caching with TTL for data refresh
- Implement retry logic with exponential backoff
- Add circuit breaker pattern for external API calls
- Implement health checks for data availability
- Add metrics and monitoring
- Support for data refresh on-demand via admin endpoint
- Add request/response logging
- Implement rate limiting