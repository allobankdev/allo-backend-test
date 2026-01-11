Here is the clean `README.md` file without any icons or emojis, ready to copy and paste.

````markdown
# IDR Rate Aggregator Service

A Spring Boot application designed to aggregate financial data from the Frankfurter API, focusing on Indonesian Rupiah (IDR) rates. This service implements a Read-Only Data Proxy architecture using the Strategy Pattern, Thread-Safe In-Memory storage, and custom Factory Beans.

## Setup & Run Instructions

### Prerequisites

- Java 17 or 21
- Maven 3.6+

### 1. Run the Application

You can run the JAR file directly or use the Maven plugin:

```bash
# Option A: Maven
mvn spring-boot:run
```
````

The application will start on port 8080.
Watch the console for the log: "Data loaded for: latest_idr_rates".

---

## Endpoint Usage

The service exposes a single polymorphic endpoint: GET /api/finance/data/{resourceType}.

### 1. Latest IDR Rates (with Custom Spread)

Returns the current exchange rates based on IDR, including the calculated USD_BuySpread_IDR.

```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates

```

### 2. Historical Data (IDR to USD)

Returns the time-series exchange data for IDR to USD (Jan 01, 2024 - Jan 05, 2024).

```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd

```

### 3. Supported Currencies

Returns the list of all available currency symbols and names.

```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies

```

---

## Personalization Note

This solution implements a unique "Spread Factor" calculation based on my GitHub username.

- **GitHub Username:** [YOUR_GITHUB_USERNAME]
- **Calculated Spread Factor:** [YOUR_CALCULATED_FACTOR] (e.g., 0.00765)

This factor is dynamically applied to the USD_BuySpread_IDR calculation during the startup data ingestion.

---

## Architectural Rationale

### 1. Polymorphism Justification (Strategy Pattern)

**Why use the Strategy Pattern over if/else logic?**

The Strategy Pattern was chosen to adhere to the Open/Closed Principle.

- **Extensibility:** If we need to add a new resource in the future, we simply create a new class implementing IDRDataFetcher. The Controller requires zero changes because it injects strategies dynamically via a Map.
- **Maintainability:** It enforces the Single Responsibility Principle. The logic for fetching "Latest Rates" is entirely isolated from "Historical Data." A bug in one strategy cannot affect the others, unlike a monolithic if/else block where shared scope can lead to regression bugs.

### 2. Client Factory (FactoryBean vs @Bean)

**Why use a FactoryBean to construct the API Client?**

While a simple @Bean is sufficient for basic objects, a FactoryBean provides a clean separation between the definition of a complex object and its usage.

- **Encapsulation of Complexity:** The FinanceClientFactoryBean encapsulates the logic for base URLs, default headers, and timeout configurations. This keeps the @Configuration classes clean.
- **Lazy Initialization Control:** Spring treats FactoryBeans differently, allowing for more granular control over exactly when the target object is instantiated, which is critical when externalizing configuration properties that might need validation before the client is built.

### 3. Startup Runner (ApplicationRunner vs @PostConstruct)

**Why use ApplicationRunner for data ingestion?**

ApplicationRunner is preferred over @PostConstruct for network-heavy initialization for two reasons:

1. **Context Safety:** @PostConstruct runs during the bean creation phase. If the network stack or other dependencies aren't fully initialized, the API call might fail. ApplicationRunner triggers only after the Spring ApplicationContext is fully refreshed and ready.
2. **Failure Isolation:** If the external API is down, @PostConstruct can crash the entire application startup, causing the deployment to fail. Logic inside ApplicationRunner can be wrapped in try/catch blocks to allow the app to start even if the initial fetch fails, providing better resilience.

```

```
