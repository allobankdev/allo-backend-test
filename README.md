# 💰 IDR Finance Data Aggregator API

This project is a Spring Boot REST API that aggregates financial data related to Indonesian Rupiah (IDR) using the public Frankfurter Exchange Rate API.

It demonstrates clean architecture, Strategy Pattern, FactoryBean usage, and thread-safe in-memory data handling.

---

## 🚀 Tech Stack

* Java 8
* Spring Boot 2.7.x
* WebClient (Spring WebFlux)
* Maven

---

## ⚙️ Setup & Run

### 1. Clone Project

```bash
git clone https://github.com/fauziladzuardhirokhmana/allo-backend-test.git
cd allo-backend-test
```

### 2. Build

```bash
mvn clean install
```

### 3. Run Application

```bash (choose which one you like)
mvn spring-boot:run (if don't want to build the project)
java -jar target\allo-backend-test-main-0.0.1-SNAPSHOT.jar (just if the project has built)

```

---

## 🌐 API Endpoint

Base URL:

```
http://localhost:8080/api/finance/data
```

### 1. Latest IDR Rates (with spread)

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2. Historical IDR to USD

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## 👤 Personalization

* **GitHub Username**: `fauziladzuardhirokhmana`

### Spread Factor Calculation

Spread Factor is calculated using:

```
(sum of ASCII values of username % 1000) / 100000.0
```

### Your Result (Example Format)

```
Username: fauziladzuardhirokhmana
ASCII Sum: 2456
Spread Factor: 0.00456
```

---

## 🧠 Architectural Rationale

### 1. Strategy Pattern

The Strategy Pattern is used to handle multiple resource types (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) without using conditional logic.

**Why this approach:**

* Improves maintainability by separating logic into independent classes
* Makes the system easily extensible for new resource types
* Avoids complex if/else or switch statements in the controller

---

### 2. FactoryBean for WebClient

A custom `FactoryBean<WebClient>` is used to construct the external API client.

**Benefits:**

* Centralized configuration (base URL, headers, timeouts)
* Flexible instantiation logic
* Fulfills the architectural constraint requirement

---

### 3. ApplicationRunner for Data Initialization

`ApplicationRunner` is used to load all data at application startup.

**Why not @PostConstruct:**

* Ensures execution after full Spring context initialization
* More suitable for complex startup logic
* Better control over application lifecycle

---

## 🧠 Data Flow

1. Application starts
2. `ApplicationRunner` fetches all required data from external API
3. Data is stored in a thread-safe in-memory store
4. API serves data directly from memory (no repeated API calls)

---

## ⚠️ Error Handling

* External API errors are handled using `ExternalApiException`
* Global exception handler ensures consistent error responses

---

## 🧪 Testing

### Unit Tests

* Strategy classes tested using mocked external client

### Integration Tests

* Verifies data is loaded into memory at startup

---

## ✅ Features Summary

* ✔ Strategy Pattern (no if/switch)
* ✔ FactoryBean for WebClient
* ✔ Startup data loading (ApplicationRunner)
* ✔ Thread-safe in-memory storage
* ✔ Custom spread calculation
* ✔ Clean architecture

---

## 📌 Notes

* Data is fetched only once at startup
* No database is used (in-memory only)
* Designed for scalability and maintainability

---

## 🙌 Author

Fauzi Ladzuardhi Rokhmana
