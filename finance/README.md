# Allo Bank – IDR Finance Data Aggregator

This project is a Spring Boot application developed as part of the **Allo Bank Backend Developer Take-Home Test**.

The application exposes a **single polymorphic REST API endpoint** that aggregates financial data related to Indonesian Rupiah (IDR) from the public **Frankfurter Exchange Rate API**.  
All external data is fetched **once at application startup** and served from an **in-memory, thread-safe store**.

---

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web (MVC)
- Spring WebFlux (WebClient)
- Maven
- Lombok

---

## Features

- Single REST endpoint with polymorphic behavior
- Strategy Pattern for resource-based data fetching
- Custom `FactoryBean` for WebClient construction
- Startup data loading using `ApplicationRunner`
- Thread-safe and immutable in-memory data storage
- No repeated external API calls per request

---

## External API

Data is retrieved from the public **Frankfurter Exchange Rate API**.

Base URL:
```
https://api.frankfurter.app
```

The following external resources are consumed:
- `/latest?base=IDR`
- `/2024-01-01..2024-01-05?from=IDR&to=USD`
- `/currencies`

---

## API Endpoint Usage

This application exposes **only one REST endpoint**:

```
GET /api/finance/data/{resourceType}
```

### Available `resourceType` values

#### 1. Latest IDR Rates (with spread calculation)
```
http://localhost:8080/api/finance/data/latest_idr_rates
```

Response includes an additional calculated field:
- `USD_BuySpread_IDR`

---

#### 2. Historical IDR to USD Rates
```
http://localhost:8080/api/finance/data/historical_idr_usd
```

Returns a small time-series of historical exchange rates.

---

#### 3. Supported Currencies
```
http://localhost:8080/api/finance/data/supported_currencies
```

Returns the list of all supported currency symbols.

---

## Personalization Note

- GitHub Username: **Tarto Rizaldi**
- Calculated Spread Factor: **0.00097**

The spread factor is calculated by summing the ASCII values of all characters in the lowercase GitHub username and applying the formula defined in the test instructions.

---

## Architectural Rationale

### Strategy Pattern
The Strategy Pattern is used to handle different resource types (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) without conditional logic in the controller.  
This improves extensibility, readability, and maintainability.

### Client FactoryBean
A custom `FactoryBean` is used to construct the `WebClient` instance, allowing centralized configuration of the external API base URL and client settings.  
This approach avoids tight coupling and follows Spring’s advanced bean lifecycle patterns.

### Startup Data Loading
An `ApplicationRunner` is used to fetch and aggregate all external data at application startup.  
This ensures consistent data, avoids repeated external API calls per request, and improves runtime performance compared to `@PostConstruct`.

---

## How to Run

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on port `8080`.
