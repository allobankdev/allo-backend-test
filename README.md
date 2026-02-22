# Finance Data Service

## 1. Setup / Run Instructions

### Prerequisites
- Java 17+
- Maven 3.8+

### Clone Repository
```bash
git clone hhttps://github.com/ahmad-shiddiqi/allo-backend-test.git
```

### Build Application
```bash
mvn clean install
```

### Run Application
```bash
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

### Run Tests
```bash
mvn test
```

---

## 2. Endpoint Usage

Base URL:
```
http://localhost:8080/api/finance/data/{resourceType}
```

### 1) Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2) Historical IDR to USD
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3) Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## 3. Personalization Note

GitHub Username: `ahmad-shiddiqi`

Calculated Spread Factor: `0.00399`

(The spread factor is computed via the implemented calculation function as required.)

---

## 4. Architectural Rationale

### Polymorphism Justification (Strategy Pattern)

The application exposes a single multi-resource endpoint that serves different financial data types. Instead of implementing conditional logic (e.g., `if-else` or `switch`) inside the service layer, the Strategy Pattern is used.

Each resource type implements a common `IDRDataFetcher` interface. This design:

- Adheres to the Open/Closed Principle
- Allows new resource types to be added without modifying existing logic
- Keeps the service layer clean and focused
- Improves maintainability and testability

This approach scales better than a conditional block, which would grow increasingly complex as new resource types are introduced.

---

### Client Factory (FactoryBean)

A `FactoryBean` is used to construct the external API client.

The FactoryBean:

- Encapsulates client construction logic
- Centralizes WebClient configuration
- Allows future enhancements such as interceptors, timeouts, retries, or authentication
- Decouples client configuration from business components

While a simple `@Bean` method could create the client, the FactoryBean approach provides greater flexibility and better separation of concerns for external integrations.

---

### Startup Runner Choice (ApplicationRunner)

`ApplicationRunner` is used for initial data ingestion instead of `@PostConstruct`.

This is preferred because:

- It runs after the full Spring context is initialized
- It clearly separates lifecycle execution from bean initialization
- It provides better control over startup sequencing
- It aligns better with production-grade startup handling

Using `@PostConstruct` would mix initialization logic with bean lifecycle concerns and provides less explicit control over execution timing.

---
