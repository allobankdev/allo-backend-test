# IDR Rate Aggregator

A **Spring Boot** application that aggregates IDR exchange rate data from the [Frankfurter API](https://www.frankfurter.app/) and exposes it through a unified internal API.

The application fetches three resources from the external API **on startup**, stores them in an in-memory store, and serves them via a REST endpoint — **without calling the external API again** at runtime.

---

## Table of Contents

1. [Setup and Run Instructions](#1-setup-and-run-instructions)
2. [API Endpoint](#2-api-endpoint)
3. [Example cURL Requests](#3-example-curl-requests)
4. [Personalization Note](#4-personalization-note)
5. [Architectural Rationale](#5-architectural-rationale)
6. [Client Factory Rationale](#6-client-factory-rationale)
7. [Startup Runner Choice](#7-startup-runner-choice)
8. [Thread Safety and In-Memory Storage](#8-thread-safety-and-in-memory-storage)
9. [Error Handling](#9-error-handling)
10. [Technology Stack](#10-technology-stack)

---

## 1. Setup and Run Instructions

### Clone the Repository

```bash
git clone https://github.com/musfiulchaggi/allo-backend-test.git
cd allo-backend-test
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or using the JAR:

```bash
java -jar target/idr-rate-aggregator-0.0.1-SNAPSHOT.jar
```

### Run Tests

```bash
mvn test
```

---

## 2. API Endpoint

**Base URL:**

```
http://localhost:8080/api/finance/data/{resourceType}
```

**Available Resource Types:**

| Resource Type          | Description                         |
|------------------------|-------------------------------------|
| `latest_idr_rates`     | Latest IDR exchange rates           |
| `historical_idr_usd`   | Historical IDR → USD rates          |
| `supported_currencies` | List of all supported currencies    |

---

## 3. Example cURL Requests

### Latest IDR Rates

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

**Example Response:**

```json
{
  "success": true,
  "errMessage": null,
  "data": {
    "amount": 1,
    "base": "IDR",
    "rates": {
      "AUD": 0.000083,
      "JPY": 0.00938
    },
    "date": "2026-03-11",
    "USD_BuySpread_IDR": "17014.237288135611936"
  },
  "httpStatus": "OK"
}
```

### Historical IDR → USD Rates

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

**Example Response:**

```json
{
  "success": true,
  "errMessage": null,
  "data": {
    "amount": 1,
    "base": "IDR",
    "rates": {
      "2023-12-29": {
        "USD": 0.000065
      },
      "2024-01-02": {
        "USD": 0.000064
      }
    },
    "start_date": "2023-12-29",
    "end_date": "2024-01-05"
  },
  "httpStatus": "OK"
}
```

### Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

**Example Response:**

```json
{
  "success": true,
  "errMessage": null,
  "data": {
    "CNY": "Chinese Renminbi Yuan",
    "THB": "Thai Baht",
    "AUD": "Australian Dollar"
  },
  "httpStatus": "OK"
}
```

---

## 4. Personalization Note

**GitHub Username used for spread calculation:** `musfiulchaggi`

### Spread Factor Calculation Logic

1. Convert username to lowercase
2. Sum ASCII values of all characters
3. Apply `mod 1000`
4. Divide result by `100000`

**Example:**

```
sum(username chars) % 1000 = 384
spreadFactor = 384 / 100000
spreadFactor = 0.00384
```

**Final Spread Factor:** `0.00384`

This factor is applied when calculating `USD_BuySpread_IDR`:

```
USD_BuySpread_IDR = (1 / rateUsd) * (1 + spreadFactor)
```

---

## 5. Architectural Rationale

### Polymorphism — Strategy Pattern

The application supports multiple resource types:
- Latest rates
- Historical rates
- Supported currencies

Instead of a large conditional block:

```java
if (resourceType == latest_idr_rates) { ... }
if (resourceType == historical_idr_usd) { ... }
if (resourceType == supported_currencies) { ... }
```

The **Strategy Pattern** was used. Each resource type is implemented as a separate fetcher strategy implementing a common interface:

```
IDRDataFetcher
├── LatestIdrRatesFetcherImpl
├── HistoricalIdrUsdFetcherImpl
└── CurrenciesFetcherImpl
```

These implementations are resolved dynamically through a **resolver**.

### Benefits

- **Extensibility** — Adding a new resource type (e.g., `latest_usd_rates`) only requires adding a new implementation, without modifying existing code. This follows the **Open/Closed Principle**.
- **Maintainability** — Each fetcher contains its own isolated logic, making the code easier to read, test, and maintain.
- **Cleaner Service Layer** — The service layer remains small and free of large conditional blocks.

---

## 6. Client Factory Rationale

A `FactoryBean` was used to construct the `WebClient`:

```
WebClientFactoryBean
```

### Benefits

| Benefit | Description |
|---|---|
| **Centralized Client Construction** | All configuration (timeouts, headers, base config) is centralized in one place |
| **Better Testability** | Client construction can easily be mocked or replaced during testing |
| **Separation of Concerns** | Client implementation focuses only on API interaction; factory handles creation logic |

### Why Not a Simple `@Bean`?

Using `FactoryBean` allows more flexible object creation logic and provides better control when creating complex or configurable clients.

---

## 7. Startup Runner Choice

The application uses a Spring Boot `ApplicationRunner` to fetch external API data at startup:

```
FinanceStartupRunner
```

### Why `ApplicationRunner` Instead of `@PostConstruct`?

| Aspect | ApplicationRunner | @PostConstruct |
|---|---|---|
| **Lifecycle Safety** | Runs after entire Spring context is fully initialized | May run earlier, before all dependencies are ready |
| **Clarity** | Specifically designed for startup logic | General-purpose lifecycle hook |
| **Control** | Easily extended, logged, or controlled via application arguments | Less flexible |

---

## 8. Thread Safety and In-Memory Storage

Aggregated data is stored in a thread-safe in-memory store:

```
FinanceDataStoreService
```

Using the concurrent data structure:

```java
ConcurrentHashMap<ResourceType, Object>
```

### Properties

- ✅ Thread-safe
- ✅ Data loaded once during startup
- ✅ Immutable after initialization
- ✅ No additional external API calls during runtime

---

## 9. Error Handling

External API failures are handled using a custom exception:

```
ExternalApiException
```

**Handled scenarios:**

- Network failures
- `4xx` client error responses
- `5xx` server error responses
- Invalid or malformed API responses

This ensures graceful failure and clear error messaging throughout the application.

---

## 10. Technology Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Core language |
| **Spring Boot** | Application framework |
| **Spring Web** | REST API layer |
| **Spring WebFlux (WebClient)** | Reactive HTTP client for external API calls |
| **Lombok** | Boilerplate code reduction |
| **Maven** | Build and dependency management |