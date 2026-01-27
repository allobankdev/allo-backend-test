# IDR Finance Data Aggregator

This project is a Spring Boot REST API that aggregates multiple financial data resources related to Indonesian Rupiah (IDR) from the public Frankfurter Exchange Rate API.

The application demonstrates production-ready architecture using:
- Strategy Design Pattern
- FactoryBean for HTTP client construction
- Startup data ingestion with immutable, thread-safe in-memory storage
- Unit and integration testing

---

## - Tech Stack

- Java 25
- Spring Boot 4.x
- Maven
- WebClient (via custom FactoryBean)
- JUnit 5 & Mockito

---

##  - Setup & Run Instructions

### 1. Clone Repository

```bash
git clone https://github.com/muhammadakbaar/allo-backend-test.git
cd allo-backend-test
```

### 2. Build & Run

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`.

---

## - Endpoint

### Get Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

### Get Historical IDR-USD Rates
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

### Get Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## - Personalization Note

- **GitHub Username**: `muhammadakbaar`
- **Calculated Spread Factor**: `0.00455`

---

## - Architectural Rationale

### Polymorphism Justification
The Strategy Pattern was used to handle the different `resourceType` values. This is preferable to a simple conditional block because it allows for easy extension. To add a new resource type, one only needs to create a new class that implements the `IDRDataFetcher` interface. The new strategy will be automatically discovered and registered by Spring, without any modification to the existing code. This adheres to the Open/Closed Principle.

### Client Factory
A `FactoryBean` was used to construct the `WebClient` instance. This is preferable to a standard `@Bean` method because it allows for more complex initialization logic. For example, the `FactoryBean` can be used to externalize the API base URL via `@ConfigurationProperties` and apply any initial configuration (e.g., timeouts, shared headers). This also allows for the creation of multiple `WebClient` instances with different configurations, if needed.

### Startup Runner Choice
An `ApplicationRunner` was used for the initial data ingestion. This is preferable to a `@PostConstruct` method because it ensures that the data is fetched only after the application context is fully initialized. This prevents any race conditions that might occur if the data were to be fetched before all the necessary beans have been created.
