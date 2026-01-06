# Allo Bank – IDR Finance Data Aggregator API

Spring Boot REST API that aggregates multiple resources from the public **Frankfurter Exchange Rate API**, with a focus on Indonesian Rupiah (IDR).

This project demonstrates production-ready backend architecture, including Strategy Pattern, startup data loading, immutability, thread safety, and comprehensive testing.

---

## 📝 Overview

The application exposes a single polymorphic REST endpoint:

GET /api/finance/data/{resourceType}

Supported `{resourceType}` values:
- latest_idr_rates
- historical_idr_usd
- supported_currencies

All data is fetched once during application startup, stored in an immutable and thread-safe in-memory cache, and served without calling the external API per request.

---

## ⚙️ Setup & Run Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- Git

### Clone Repository
git clone https://github.com/mrashadyusuf/allo-backend-test.git
cd allobank-finance-api

### Build Application
mvn clean package

### Run Application
mvn spring-boot:run

Application runs at:
http://localhost:8080

All Frankfurter API resources are fetched once during startup.

### Run Tests
mvn test

This executes:
- Unit tests for each Strategy (mocked API client)
- Unit tests for spread calculation
- Integration test verifying startup data initialization

---

## 🔌 API Endpoint Usage

### Latest IDR Rates (with USD Spread)
curl http://localhost:8080/api/finance/data/latest_idr_rates

### Historical IDR → USD Rates
curl http://localhost:8080/api/finance/data/historical_idr_usd

### Supported Currencies
curl http://localhost:8080/api/finance/data/supported_currencies

---

## 🧮 Personalization Note (Spread Factor)

GitHub Username:
mrashadyusuf

The GitHub username is configured via application properties and is used only to generate a deterministic spread factor.

### Spread Factor Calculation

SpreadFactor = (Sum of Unicode values of username % 1000) / 100000.0

For username `mrashadyusuf`:
  - Sum of Unicode values: 1191
  - Spread Factor: 0.00191


### USD Buy Spread Formula

USD_BuySpread_IDR = (1 / Rate_USD) × (1 + SpreadFactor)

Where Rate_USD is retrieved from:
https://api.frankfurter.app/latest?base=IDR

---

## 🏗️ Architecture Summary

- Strategy Pattern is used to handle each resource type
- External API client is constructed via a custom FactoryBean
- ApplicationRunner loads all data once at startup
- In-memory store is immutable, thread-safe, and read-only
- Tests cover unit logic and startup integration

---

## ✅ Guarantees

- External API is never called per request
- Data is loaded exactly once
- Safe for concurrent access
- Deterministic spread calculation per candidate

---

Author  
GitHub: mrashadyusuf
