# Allo IDR Rate Aggregator
**IDR Rate Aggregator – Spring Boot WebFlux**
A production-ready Spring Boot WebFlux REST API that aggregates multiple Indonesian Rupiah (IDR)–related financial resources using a clean, extensible, and testable architecture.

---

## 📌 Pull Request Title

**feat: Implement IDR rate aggregator with strategy-based architecture**

---

## 📄 Overview

This project implements a Spring Boot WebFlux REST API that aggregates multiple IDR-related financial datasets from the **Frankfurter Exchange Rate API**.

The application exposes a **single polymorphic endpoint** that dynamically serves different financial resources using the **Strategy Pattern**.

All external data is:
- Loaded **once at application startup**
- Cached in a **thread-safe immutable in-memory store**
- Served efficiently **without repeated external API calls**

This design ensures performance, consistency, and scalability.


---
## 🚀 Features
- **🔗 Unified REST Endpoint**
GET /api/finance/data/{resourceType}

## 📊 Supported Resource Types

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

## 🧠 Architecture & Design

- **Strategy Pattern** for extensible resource handling

- **FactoryBean-based WebClient** for external API access

- **Startup data preloading** using `ApplicationRunner`

- **Thread-safe immutable in-memory cache**

- **Externalized configuration** via `application.yml`

## 💱 USD Buy Spread Calculation

Deterministically derived from GitHub username

Ensures uniqueness and reproducibility across environments

## 🧪 Testing

Comprehensive unit tests for all strategy implementations

Integration tests for startup lifecycle and REST API endpoints

## 👤 Personalization

GitHub Username: JokoKusnandi

Spread Calculation Logic:
The USD buy spread factor is deterministically derived from the GitHub username using character summation.
This guarantees a unique yet reproducible spread value across all environments and executions.

## 🛠️ Architectural Rationale
1️⃣ Why the Strategy Pattern?

The Strategy Pattern was chosen to handle multiple financial resource types in a clean, extensible, and maintainable manner.

Each resource is encapsulated in its own IDRDataFetcher implementation, which:

Eliminates conditional logic (if/else or switch)

Adheres to the Open/Closed Principle

Allows new resource types to be added without modifying controllers or services

This results in a highly modular and scalable design.

2️⃣ Why use a FactoryBean for the API Client?

A custom FactoryBean<WebClient> centralizes the creation and configuration of the external Frankfurter API client.

Benefits include:

Clear architectural boundaries

Avoidance of scattered client configuration

Clean externalized configuration support

Greater control compared to a simple @Bean

This approach produces a cleaner and more maintainable infrastructure layer.

3️⃣ Why ApplicationRunner instead of @PostConstruct?

ApplicationRunner executes after the full Spring application context is initialized, offering superior lifecycle control.

This ensures:

All required beans are available before making external API calls

Startup behavior is explicit and predictable

Improved error handling and testability

It is more suitable than @PostConstruct for startup workflows involving external dependencies.

## 🧪 Testing Strategy
✅ Unit Tests

Cover all IDRDataFetcher strategy implementations

Validate business logic, including USD spread calculation

✅ Integration Tests

Verify successful data loading at application startup

Ensure the in-memory store is populated before serving requests

Validate REST API behavior using WebTestClient

## 🧾 Example API Usage
curl -v http://localhost:9093/api/finance/data/latest_idr_rates
curl -v http://localhost:9093/api/finance/data/historical_idr_usd
curl -v http://localhost:9093/api/finance/data/supported_currencies

✅ Code Review Readiness

Clean and modular package structure

Clear separation of concerns

Atomic and descriptive commit history

Production-grade architecture

Fully tested and ready for immediate review