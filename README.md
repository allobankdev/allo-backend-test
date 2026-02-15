# IDR Rate Aggregator API

A professional Spring Boot-based backend service designed to aggregate financial data from the Frankfurter API. This project focuses on **Clean Code**, **SOLID Principles**, and high-performance **In-Memory Data Access**.

## Key Features
* **Automated Data Ingestion**: Fetches financial data exactly once at startup using `ApplicationRunner` to ensure zero live API calls during runtime.
* **Strategy Design Pattern**: Implements a polymorphic approach for data fetching, allowing the system to be easily extensible for new resources.
* **Thread-Safe In-Memory Cache**: Uses a `ConcurrentHashMap` that is transformed into an `unmodifiableMap` after startup to guarantee data immutability.
* **Global Exception Handling**: Centralized error management using `@RestControllerAdvice` for consistent and secure API responses.
* **Standardized DTOs**: Strongly-typed Data Transfer Objects for all API resources to ensure type safety and clear data contracts.

## Architectural Decisions

### 1. Strategy Pattern & Dynamic Injection
The application utilizes the Strategy Pattern to manage different Frankfurter API endpoints. By injecting a `Map<String, IDRDataFetcher>`, Spring automatically maps each strategy bean based on its `@Component` name. This allows adding new financial data resources without modifying the Controller or Service layers.

### 2. Startup Data Loading (Constraint Fulfillment)
To fulfill the requirement of serving data without live network calls, a `DataIngestionRunner` iterates through all registered strategies during the application's boot sequence and populates the in-memory storage.

### 3. Custom Spread Factor Calculation
The "Latest IDR Rates" endpoint includes a custom field `usd_buy_spread_idr` calculated based on the developer's GitHub username ASCII values.

**Calculation for `luthfiaryaa`**:
* **ASCII Sum**: 1515.
* **Spread Factor**: $(1515 \pmod{1000}) / 100000.0 = 0.00515$.
* **Formula**: $BuySpread = (1 / RateUSD) \times (1 + SpreadFactor)$.

##️ Getting Started

### Prerequisites
* **Java 17**
* **Maven 3.x**
* **IntelliJ IDEA** (Recommended)

### Installation & Run
1. Clone the repository:
```bash
  git clone <repository-url>
 ```

2. Clone the repository:
  ```bash
  mvn clean install
  mvn spring-boot:run
```

## API Documentation
| Resource              | Endpoint                                      | Description                                   |
|-----------------------|-----------------------------------------------|-----------------------------------------------|
| Latest Rates          | `GET /api/finance/data/latest_idr_rates`      | Latest IDR rates with custom spread           |
| Historical Data       | `GET /api/finance/data/historical_idr_usd`    | Data for Jan 1, 2024 to Jan 5, 2024           |
| Supported Currencies  | `GET /api/finance/data/supported_currencies`  | List of all currencies available              |


***Developed by Luthfi Aryarizki***
