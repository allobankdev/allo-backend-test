# Allo Bank Backend Test - IDR Rate Aggregator

## 📌 Overview
This project is a Spring Boot REST API that aggregates financial data from the Frankfurter Exchange Rate API. The application exposes a single polymorphic endpoint that serves multiple types of financial data related to Indonesian Rupiah (IDR).

The application is designed with production-ready practices, including:
- Strategy Design Pattern
- FactoryBean for WebClient configuration
- Immutable in-memory data store
- Startup data initialization
- Robust error handling
- Unit and integration testing

---

## 🚀 How to Run

### 1. Clone Repository
```bash
git clone https://github.com/WahidinAlambiyah/allo-backend-test
cd allo-backend-test
```

### 2. Build Project
```bash
mvn clean install
```

### 3. Run Application
```bash
mvn spring-boot:run
```
---
## 🌐 API Endpoint

### Base URL
```
http://localhost:8125
```

### Endpoint
```
GET /api/finance/data/{resourceType}
```
---
## 📊 Available Resource Types
### 1. Latest IDR Rates
```bash
curl localhost:8125/api/finance/data/latest_idr_rates
```

### 2. Historical IDR → USD
```bash
curl localhost:8125/api/finance/data/historical_idr_usd
```

### 13 Supported Currencies
```bash
curl localhost:8125/api/finance/data/supported_currencies
```
---
## 🧮 Personalization (Spread Factor)
GitHub Username: WahidinAlambiyah

### Spread Calculation
- Sum of ASCII values of username characters
- Formula:
```
Spread Factor = (Sum % 1000) / 100000.0
```
### Example Result
```
Spread Factor: 0.00612
```
### Final Formula
```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```
---
## 🏗️ Architecture
### 1. Strategy Pattern (Polymorphism)
The application uses the Strategy Design Pattern to handle multiple resource types dynamically.

**Why Strategy Pattern?**

Instead of using conditional logic (if/else or switch), each resource type is encapsulated in its own strategy implementation.

**Benefits**:
- **Extensibility**: New resource types can be added without modifying existing code
- **Maintainability**: Each strategy is isolated and easier to test
- **Clean Code**: Eliminates complex conditional branching

---

### 2. FactoryBean for WebClient
The WebClient instance is created using a custom `FactoryBean`.

**Why FactoryBean?**

- Centralized configuration of external API client
- Encapsulates complex setup (timeouts, base URL, headers)
- Cleaner lifecycle management compared to standard @Bean

**Advantages:**
- Reusable client configuration
- Better separation of concerns
- More flexible for scaling and customization

---

### 3. ApplicationRunner for Data Initialization

All external API data is fetched once at application startup using `ApplicationRunner`.

**Why not @PostConstruct?**
- `ApplicationRunner` runs after the full application context is ready
- More reliable for initializing external dependencies
- Better suited for startup workflows

---

### 4. In-Memory Data Store (Thread-Safe & Immutable)

All fetched data is stored in an immutable in-memory store.

**Design:**
- Data loaded once at startup
- Stored using Collections.unmodifiableMap
- Thread-safe access
  **Benefits:**
- No repeated API calls
- Faster response time
- Safe for concurrent access

---

## 🧪 Testing
**Unit Tests**
- Strategy classes tested using Mockito
- External API calls mocked via WebClient
  **Integration Test**
- Ensures data is loaded at startup via ApplicationRunner

---
## 🛡️ Error Handling
* Global exception handler using @RestControllerAdvice
* Graceful handling of:
    - API failures
    - Null responses
    - Invalid resource types

---

## 📦 Tech Stack
* Java 17+
* Spring Boot
* WebClient (Reactive)
* Lombok
* JUnit 5
* Mockito
---

## ✨ Notes
* Historical data uses fixed date range:
```
2024-01-01..2024-01-05
```
as required by the specification for consistency and deterministic results.


