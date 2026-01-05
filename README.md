# Allo Bank Backend Developer Take-Home Test

Thank you for applying to our team! This take-home test is designed to evaluate your practical skills in building **production-ready** Spring Boot applications within a finance domain, focusing on architectural patterns and complex data handling.

## 📝 Objective

Your task is to create a single Spring Boot REST API endpoint capable of aggregating data from multiple, distinct resources provided by the public, keyless **Frankfurter Exchange Rate API**. The primary focus is on handling Indonesian Rupiah (IDR) data.

The focus of this test is not just functional correctness, but demonstrating clean code, advanced Spring concepts, thread-safe design, and architectural clarity.

## I. Core Task: The Polymorphic API

### 1. External API Integration (Frankfurter API)

* **Base URL (Public):** `https://api.frankfurter.app/`.

* You must integrate with three distinct data resources to enforce the architectural pattern:

   1.  `/latest?base=IDR` (The latest rates relative to IDR)

   2.  **Historical Data:** Query a specific, small time series (e.g., `/2024-01-01..2024-01-05?from=IDR&to=USD`). **Note:** *Use the date range provided in this example unless a different range is communicated separately.*

   3.  `/currencies` (The list of all supported currency symbols)

### 2. Internal API Endpoint

You must expose **one single endpoint** in your application: ```GET /api/finance/data/{resourceType}```

Where `{resourceType}` can be one of the three strings: `latest_idr_rates`, `historical_idr_usd`, or `supported_currencies`.

### 3. Required Functionality & Business Logic

* **Resource Handling:** Your service must correctly map the three incoming `resourceType` values to the correct data fetching strategies.

* **Data Load:** All three resources should be fetched from the external API.

* **Data Transformation (Latest IDR Rates only) - Unique Calculation:** For the **`latest_idr_rates`** resource, you must calculate and include a new field, `"USD_BuySpread_IDR"`. This is the Rupiah selling rate to USD after applying a banking spread/margin.

  **The Spread Factor Must Be Unique :**

   1.  **Input:** Your GitHub username (e.g., `johndoe47`).
   2.  **Calculation:** Calculate the sum of the Unicode (ASCII) values of all characters in your lowercase GitHub username string.
   3.  **Spread Factor Derivation:** `Spread Factor = (Sum of Unicode Values % 1000) / 100000.0`
       *(This will yield a unique factor between 0.00000 and 0.00999, ensuring a personalized result.)*

  **Final Formula:** `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)` (where `Rate_USD` is the value from the API when `base=IDR`).

* **Other Resources:** The `historical_idr_usd` and `supported_currencies` resources can return their data with minimal transformation, but the final output must be a unified JSON array of results.

## II. Architectural Constraints

Meeting the core task is only one part of the solution. The following constraints must be strictly adhered to and will be heavily weighted during evaluation:

### Constraint A: The Strategy Pattern

The logic for handling the three different resources (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) must be implemented using the **Strategy Design Pattern**.

1.  Define a clear **Strategy Interface** (e.g., `IDRDataFetcher`).

2.  Implement **three concrete strategy classes** (one for each resource).

3.  The main `Controller` should dynamically select the correct strategy implementation using a map-based lookup injected by Spring, avoiding any manual `if/else` or `switch` logic in the controller layer.

### Constraint B: Client Factory Bean

The instance of your chosen external API client (`WebClient` or `RestTemplate`) **must be defined and created within a custom implementation of Spring's `FactoryBean<T>` interface**.

* This `FactoryBean` should be responsible for externalizing the API Base URL via `@Value` or `@ConfigurationProperties` and applying any initial configuration (e.g., timeouts, shared headers).

* ***You may not define the client as a simple `@Bean` in a `@Configuration` class.***

### Constraint C: Startup Data Runner & Immutability

The aggregated data for **ALL three resources** must be fetched **exactly once on application startup** and loaded into an in-memory store.

1.  Use a Spring Boot **`ApplicationRunner`** or **`CommandLineRunner`** component to initiate the data fetching process.

2.  The API endpoint (`GET /api/finance/data/{resourceType}`) must serve the data from this **in-memory store**, not by making a new call to the external API on every request.

3.  The in-memory storage mechanism (e.g., a service holding the data) must be designed to be **thread-safe** and ensure the data is **immutable** once the `ApplicationRunner` has finished loading it.

## III. Production Readiness & Deliverables

Your final solution must demonstrate production quality through code, testing, and communication.

### 1. Robustness & Best Practices

* Graceful **Error Handling** for network failures or 4xx/5xx responses from the external API.

* Proper use of **Configuration Properties** (e.g., `application.yml`) for external service URLs.

* Clear separation of concerns (Controller, Service, Model/DTO, etc.).

### 2. Testing

* **Unit Tests** for all three `IDRDataFetcher` strategy implementations, ensuring data calculation and transformation logic is covered (using mock clients for external calls).

* **Integration Tests** to verify the `ApplicationRunner` successfully initializes and loads the data into the in-memory store before the application context is ready.

### 3. Documentation

A clear `README.md` is mandatory. It must include:

* **Setup/Run Instructions:** Clear steps to clone, build, and run the application and tests.

* **Endpoint Usage:** Example cURL commands to test the three different resource types.

* **Personalization Note:** Clearly state your GitHub username and show the exact **Spread Factor** (e.g., `0.00765`) calculated by your function.

* ---

* ### 🛠️ Architectural Rationale

  This section should contain a brief, but detailed, explanation answering the following questions:

   1.  **Polymorphism Justification:** Explain *why* the Strategy Pattern was used over a simpler conditional block in the service layer for handling the multi-resource endpoint. Discuss the benefits in terms of **extensibility** and **maintainability**.

   2.  **Client Factory:** Explain the specific role and benefit of using a **`FactoryBean`** to construct the external API client. Why is this preferable to defining the client using a standard `@Bean` method in this scenario?

   3.  **Startup Runner Choice:** Justify the choice of using an `ApplicationRunner` (or `CommandLineRunner`) for the initial data ingestion over a simpler `@PostConstruct` method.

## IV. Submission & Review Process

1.  **Fork** this repository.

2.  Implement your solution on a dedicated feature branch (e.g., `feat/idr-rate-aggregator`).

3.  When complete, submit your solution via a **Pull Request (PR)** back to the main repository.

**Your PR will be evaluated on the following:**

* **Commit History:** Clean, atomic, and descriptive commit messages (e.g., "feat: Implement IDR latest rates strategy," "fix: Correctly calculate IDR spread in tests").

* **PR Description:** The description must clearly summarize the solution and **must contain the full answers** to the three "Architectural Rationale" questions from Section III.

* **Code Review Readiness:** The code should be well-structured and ready for immediate review.

Good luck!


---

## Overview
his project is a Spring Boot–based backend application developed as part of the **Allo Bank Backend Developer Take-Home Test**.  
The primary goal of this application is to demonstrate the ability to design and implement a **production-ready financial service** with a strong focus on **clean architecture, extensibility, and deterministic data handling**.

## 🔗 Endpoint Usage

The application exposes **one single polymorphic REST endpoint** to serve different finance data resources.

### Base Endpoint

GET /api/finance/data/{resourceType}

Where `{resourceType}` determines the type of finance data to be returned.

## Supported Resource Types

| Resource Type | Description |
|--------------|------------|
| `supported_currencies` | Returns a list of supported currency codes and their descriptions |
| `historical_idr_usd` | Returns historical IDR to USD exchange rate data |
| `latest_idr_rates` | Returns the latest IDR exchange rates including a calculated USD buy spread |

[Postman Collection Details](docs/Allo%20Bank%20Test.postman_collection.json)

---
### Project Structure
```
.
├── .gitignore
├── .mvn
├── README.md
├── docs
│   └── Allo Bank Test.postman_collection.json
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── allobank
    │   │           └── allobanktest
    │   │               ├── AllobanktestApplication.java
    │   │               ├── client
    │   │               │   └── FrankfurterClientFactory.java
    │   │               ├── config
    │   │               │   ├── FrankfurterClientConfig.java
    │   │               │   └── FrankfurterProperties.java
    │   │               ├── controller
    │   │               │   └── FinanceController.java
    │   │               ├── dto
    │   │               │   ├── ApiResponse.java
    │   │               │   ├── HistoriRateResponse.java
    │   │               │   └── LatestIdrRateResponse.java
    │   │               ├── runner
    │   │               │   └── FinanceDataLoader.java
    │   │               ├── store
    │   │               │   └── FinanceDataStore.java
    │   │               ├── strategy
    │   │               │   ├── SupportedCurrenciesFetcher.java
    │   │               │   ├── HistoricalIdrUsdFetcher.java
    │   │               │   ├── LatestIdrRatesFetcher.java
    │   │               │   ├── ResourceType.java
    │   │               │   └── IDRDataFetcher.java
    │   └── resources
    │       ├── application.yaml
    └── test
        └── java
            └── com
                └── allobank
                    └── allobanktest
                        ├── AllobanktestApplictionTests.java
                        ├── runner
                        │   └── FinanceDataLoaderTest.java
                        └── strategy
                            ├── HistoricalIdrUsdFetcherTest.java
                            ├── LatestIdrRatesFetcherTest.java
                            └── SupportedCurrenciesFetcherTest.java
                                

```
### Details Project Structure

- `AllobanktestApplication.java`  
  The main Spring Boot application class responsible for bootstrapping and launching the Allo Bank finance data aggregator application.

---

- Package `com.allobank.allobanktest.client`:
    - `FrankfurterClientFactory.java`  
      A custom `FactoryBean` responsible for creating and configuring a singleton `WebClient` instance used to communicate with the Frankfurter Exchange Rate API. This centralizes external API client creation and configuration.

---

- Package `com.allobank.allobanktest.config`:
    - `FrankfurterClientConfig.java`  
      Spring configuration class that registers the Frankfurter API client factory as a managed bean within the application context.

    - `FrankfurterProperties.java`  
      Configuration properties class used to externalize and bind Frankfurter API settings (such as the base URL) from `application.yml`.

---

- Package `com.allobank.allobanktest.controller`:
    - `FinanceController.java`  
      REST controller that exposes a single polymorphic endpoint for retrieving finance data. It validates the requested resource type and serves data from the immutable in-memory store without invoking external APIs at runtime.

---

- Package `com.allobank.allobanktest.dto`:
    - `ApiResponse.java`  
      A minimal and generic API response wrapper that provides contextual metadata (resource type) while preserving the original response payload structure.

    - `HistoricalRateResponse.java`  
      Data Transfer Object (DTO) representing the historical IDR to USD exchange rate response returned by the Frankfurter API, including time-series rate data.

    - `LatestIdrRateResponse.java`  
      DTO representing the latest IDR exchange rate data enriched with a calculated USD buy spread. All monetary values are represented using `BigDecimal` to ensure financial precision.

---

- Package `com.allobank.allobanktest.runner`:
    - `FinanceDataLoader.java`  
      An `ApplicationRunner` implementation responsible for fetching all required finance data at application startup using the Strategy Pattern and storing the results in an immutable in-memory store.

---

- Package `com.allobank.allobanktest.store`:
    - `FinanceDataStore.java`  
      A thread-safe, immutable in-memory data store that holds all preloaded finance data. It ensures data consistency by allowing write operations only during application startup and read-only access at runtime.

---

- Package `com.allobank.allobanktest.strategy`:
    - `SupportedCurrenciesFetcher.java`  
      Strategy implementation responsible for fetching and returning the list of supported currency codes from the Frankfurter API.

    - `HistoricalIdrUsdFetcher.java`  
      Strategy implementation responsible for retrieving historical IDR to USD exchange rate data without applying additional business transformations.

    - `LatestIdrRatesFetcher.java`  
      Strategy implementation responsible for fetching the latest IDR exchange rates and calculating a personalized USD buy spread based on the GitHub username.

    - `ResourceType.java`  
      Enumeration defining all supported finance resource types used to safely route requests and strategy selection without relying on magic strings.

    - `IDRDataFetcher.java`  
      Strategy interface that defines a contract for fetching and transforming finance-related data. Each concrete implementation handles a specific resource type.

---

- File `application.yml`  
  Centralized configuration file used to define external API settings, GitHub username personalization, and other application-level properties.

- File `Allo Bank Test.postman_collection.json`  
  Postman collection containing predefined API requests to facilitate manual testing and validation of the finance endpoints.

- File `pom.xml`  
  Maven configuration file that defines project dependencies, build plugins, and Java version settings.

---

- Directory `src/main/java`  
  Contains the main application source code, including controllers, services (strategies), configuration, and infrastructure components.

- Directory `src/main/resources`  
  Holds application configuration files and other required runtime resources.

- Directory `src/test/java`  
  Contains unit and integration tests that validate strategy logic, startup data loading, and error handling behavior.
---

### Prerequisites
- Java SDK 17 or above
- Apache Maven 3.8.4 or above
---


### Installations
1. Clone the repo `git clone https://github.com/hasanalmunawr/allo-backend-test.git`
2. Install Maven Dependencies `mvn clean install`
3. Configure properties in `application.yml` file
  ```yml
    frankfurter:
      base-url: https://api.frankfurter.app
    github:
      username: <github-username> # The username of your github account
```
4. Run test `mvn test`
5. Run the application `mvn spring-boot:run`



### Personalization

- Github Username `hasanalmunawr`
- Spread Factor: `0.00394`




