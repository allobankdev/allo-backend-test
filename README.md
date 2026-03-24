# Allo Bank Backend Developer Take-Home Test

## 🚀 Overview

This project implements a Spring Boot REST API that aggregates financial data from the public Frankfurter Exchange Rate API, focusing on Indonesian Rupiah (IDR).

The application demonstrates clean architecture, polymorphism, and production-ready design.

---

## ⚙️ How to Run

```bash
git clone <https://github.com/salwafadillah171011450139/allo-backend-test>
branch test-salwa-fadillah
cd allo-backend-test
mvn clean install
mvn spring-boot:run
```

App will run at:

```
http://localhost:8080
```

---

## 📡 API Endpoint

### Base Endpoint

```
GET /api/finance/data/{resourceType}
```

### Available Resource Types

#### 1. Latest IDR Rates

```
/api/finance/data/latest_idr_rates
```

#### 2. Historical IDR to USD

```
/api/finance/data/historical_idr_usd
```

#### 3. Supported Currencies

```
/api/finance/data/supported_currencies
```

---

## 📊 Example Response (Latest Rates)

```json
[
  {
    "usdRate": 0.000059,
    "spreadFactor": 0.0097,
    "usdBuySpreadIdr": 17113.56
  }
]
```

---

## 🧠 Personalization

GitHub Username:

```
salwafadillah171011450139
```

Spread Factor Calculation:

```
(sum ASCII % 1000) / 100000
```

Result:

```
0.0097
```

---

## 🏗️ Architecture

### Strategy Pattern

Each resource is handled by a separate strategy:

* LatestRatesFetcher
* HistoricalRatesFetcher
* CurrencyFetcher

This eliminates conditional logic and improves scalability.

---

### FactoryBean

WebClient is created using a custom FactoryBean to centralize configuration and externalize the base URL.

---

### ApplicationRunner

All external data is fetched once during application startup and stored in memory.

---

### In-Memory Storage

* Uses ConcurrentHashMap
* Data is immutable
* Thread-safe access

---

## ⚠️ Error Handling

* External API failures are handled gracefully
* Fallback responses are returned instead of crashing
* Prevents startup failure

---

## 📌 Notes

* No if/else is used for resource selection (Strategy Pattern applied)
* Data is fetched only once at startup
* Clean JSON response format

---

## 👨‍💻 Author

Salwa Fadillah
