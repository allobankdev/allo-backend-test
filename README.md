# Allo Backend Take Home Test

##  Overview

This project is a Spring Boot application that provides financial data based on IDR (Indonesian Rupiah) using the Frankfurter API.

The application implements clean architecture principles, Strategy Pattern, and in-memory caching to ensure performance and scalability.

---

##  How to Run

### Prerequisites

* Java 17+
* Maven (or use wrapper)

### Run Application

```bash
./mvnw spring-boot:run
```
### Run Unit Test
```bash
.\mvnw.cmd test    
````

For Windows:

```bash
.\mvnw.cmd spring-boot:run
```



---

##  API Endpoints

### 1. Latest IDR Rates

```
GET /api/finance/data/latest_idr_rates
```

### 2. Historical IDR to USD

```
GET /api/finance/data/historical_idr_usd
```

### 3. Supported Currencies

```
GET /api/finance/data/supported_currencies
```

---

##  Architecture & Design

### 1. Strategy Pattern

Used to handle different resource types:

* LatestRatesFetcher
* HistoricalRatesFetcher
* CurrencyFetcher

This avoids conditional logic (if/switch) and makes the system extensible.

---

### 2. FactoryBean (WebClient)

WebClient is created using FactoryBean to encapsulate configuration and provide flexibility.

---

### 3. In-Memory Data Store

* Data is fetched once during application startup
* Stored in ConcurrentHashMap
* Immutable using List.copyOf()

This improves performance and avoids repeated external API calls.

---

### 4. Separation of Concerns

* Controller → API layer
* Client → external API communication
* Strategy → business logic
* Store → caching layer

---

## Spread Calculation

Spread is calculated based on GitHub username:

```
spread = (sum of ASCII % 1000) / 100000
```

Applied only to USD:

```
usdBuyRate = (1 / rate) * (1 + spread)
```

---

## Error Handling

GlobalExceptionHandler is used to standardize error responses:

```json
{
  "timestamp": "...",
  "status": 400,
  "message": "Invalid type"
}
```

---

##  Testing

Unit tests are provided for:

* SpreadCalculator

---

##  Notes

* Data is preloaded at startup for performance
* No external API call during request handling
* Designed to be easily extensible for new resource types

---

##  Author

GitHub Username: ramaaufadha
