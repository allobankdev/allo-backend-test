# Allo Bank Backend Take-Home Test

This Spring Boot application is developed to fulfill the **Allo Bank Backend Developer Take-Home Test**, aiming to implement a REST API that aggregates exchange rate data from the **Frankfurter Exchange Rate API**, with a primary focus on Indonesian Rupiah (IDR).


---

## Tech Stack

- Java 17
- Spring Boot 4.0.1
- Maven

---

## Features
- Fetch historical exchange rates
- External API integration
- Configurable properties
- Startup data loading
- In-memory storage
- Timeout handling

---

## Configuration

`src/main/resources/application.properties`

```properties
# Spring Config
spring.application.name=takehometest

# Frankfurter API Configuration
app.frankfurter.baseurl=https://api.frankfurter.dev/v1
app.frankfurter.timeout-ms=5000

# Github Params
app.github.username=irzaardian

# Historical exchange rate configuration
# Used by HistoricalIdrUsdFetcher at startup
app.historical.from-date=2026-01-01
app.historical.to-date=2026-01-05
app.historical.base=IDR
app.historical.target=USD
