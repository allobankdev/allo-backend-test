<<<<<<< HEAD
# Frankfurter Exchange Rate Aggregator
    Frankfurter Exchange Rate Aggregator as part of Allobank Backend Developer test.

## Personalization

- **GitHub Username:** nflhm  
- **Spread Factor Calculation**  
  Sum of ASCII values of lowercase username:  
  `n`=110, `f`=102, `l`=108, `h`=104, `m`=109  
  Total = 110+102+108+104+109 = **533**  
  Spread Factor = (533 % 1000) / 100000.0 = **0.00533**

---

## Prerequisites

- **Java 21** (or later)
- **Maven 3.6+**

---

## Setup & Run Instructions

1. **Clone the repository**  
    git clone https://github.com/nflhm/frankfurter-aggregator.git

2. **Configure GitHub username**
    Open src/main/resources/application.properties and set github.username value

3. **Build the application**
    Run the following command in cmd: mvn clean compile

4. **Run the tests**
    Run the following command in cmd: mvn test

5. **Start the Application**
    Run the following command in cmd: mvn spring-boot:run

6. **Access the API**
    The service runs on http://localhost:8080 with the following endpoint:
###     1. Latest IDR rates (includes USD_BuySpread_IDR)
        curl http://localhost:8080/api/finance/data/latest_idr_rates

###     2. Historical IDR to USD rates
        curl http://localhost:8080/api/finance/data/historical_idr_usd

###     3. All supported currencies
        curl http://localhost:8080/api/finance/data/supported_currencies

---

## Architectural Rationale
1. **Polymorphism Justification – Strategy Pattern**
    The application supports three distinct data sources (latest_idr_rates, historical_idr_usd, supported_currencies), each requiring different logic for fetching and transforming data. Using the Strategy Pattern encapsulates each variant in its own class, implementing a common interface (IDRDataFetcher). The controller selects the appropriate strategy via a map lookup (injected by Spring).
    
    Benefits:
    Extensibility: Adding a new resource type only requires creating a new strategy class; no existing code needs modification.
    Maintainability: Each strategy is isolated, making it easier to test, debug, and modify individual behaviors.
    Avoids Conditional Logic: Eliminates the need for if-else or switch statements in the controller or service layer, keeping the code clean and reducing complexity.

2. **Client Factory – Why FactoryBean?**
    A custom FactoryBean<WebClient> is used to create the WebClient instance. This factory centralizes configuration (base URL, timeouts, headers) and can be reused across all strategies.
    
    Advantages over a standard @Bean method:
    Encapsulation of Complex Setup: The FactoryBean can perform additional logic (e.g., validation, conditional configuration) that would otherwise clutter a simple @Bean definition.
    Separation of Concerns: The factory bean is responsible only for client creation, keeping the configuration class clean.
    Testability: The factory can be easily mocked or replaced with a test double, allowing unit tests to inject a custom WebClient.

3. **Startup Runner – ApplicationRunner over @PostConstruct**
    The initial data load is performed in an ApplicationRunner component.
    
    Why not @PostConstruct?
    @PostConstruct runs before the application context is fully initialized; if any dependencies (e,g., WebClient) are not yet ready, the operation may fail.
    ApplicationRunner guarantees that all beans are fully instantiated and the context is ready before execution.

    Benefits:
        Reliability: The data load happens after the entire application is ready, reducing the risk of missing dependencies.
        Error Handling: Failures during data loading can be managed to prevent the application from starting with incomplete data (or to allow startup with graceful fallback).
=======
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
4.  Please complete the form to submit your technical test: [Click Here](https://forms.gle/nZKQ2EjTCPfAKHog7)

**Your PR will be evaluated on the following:**

* **Commit History:** Clean, atomic, and descriptive commit messages (e.g., "feat: Implement IDR latest rates strategy," "fix: Correctly calculate IDR spread in tests").

* **PR Description:** The description must clearly summarize the solution and **must contain the full answers** to the three "Architectural Rationale" questions from Section III.

* **Code Review Readiness:** The code should be well-structured and ready for immediate review.

Good luck!
>>>>>>> upstream/main
