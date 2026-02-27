# Finance Data Service

A Spring Boot service that aggregates multiple external currency resources from the Frankfurter API into a unified, immutable in-memory data store.

This project demonstrates:
- Strategy Pattern for multi-resource endpoints
- FactoryBean-based external client construction
- Startup data ingestion with immutable storage
- Personalized financial calculation

---

# 🚀 Features

- Unified endpoint:
GET /api/finance/data/{resourceType}

- Supported resources:
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

- Data is fetched **once at startup** and served from memory.

---

# 🛠️ Tech Stack

- Java 17+
- Spring Boot
- WebClient (Reactive HTTP client)
- JUnit 5 + Mockito
- Lombok

---

# 📦 Setup & Run Instructions

## 1️⃣ Clone repository
```
git clone https://github.com/habibullahdm/allo-backend-test.git
```
## 2️⃣ Build project
```
./mvnw clean install
```
## 3️⃣ Run application
```
./mvnw spring-boot:run
```
Application runs on: 
```
http://localhost:8080
```

## 4️⃣ Run tests
```
./mvnw test
```
---

# 📡 Endpoint Usage

Base path:
```
/api/finance/data/{resourceType}
```

## 🔹 1. Latest IDR Rates
Returns latest exchange rates relative to IDR + personalized spread calculation.
```
curl http://localhost:8080/api/finance/data/latest_idr_rates
```
## 🔹 2. Historical IDR → USD

Returns small time-series historical conversion.
```
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

## 🔹 3. Supported Currencies

Returns all available currency symbols.
```
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

# 🎯 Personalization Note

This implementation includes a unique financial spread factor derived from the GitHub username.

## GitHub Username
```
habibullahdm
```

## Spread Factor Formula
```
Spread Factor = (Sum of Unicode(username) % 1000) / 100000.0
```

## Calculated Spread Factor
```
0.00XXX
```

This value is used in:
```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)
```

This ensures each submission produces a unique financial result.
