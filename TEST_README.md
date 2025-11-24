spread test = 0.00201

# Finance Exchange Rate Loader
A Spring Boot application that fetches IDR exchange rate data using three strategy implementations, loads it during application startup with `ApplicationRunner`, and stores the results in an in-memory data store.

### Setup & Run Instructions
### 1. Clone Repository
git clone https://github.com/Schwanzeirs/allo-backend-test
cd allo-backend-test
git checkout feature/idr-rate-aggregator

### 2. Build Project
./mvnw clean install

### 3. Run Application
./mvnw spring-boot:run

### 4. Run Unit & Integration Tests
./mvnw test

### Available Endpoints
Method | Endpoint | Description
-------|----------|-------------
GET | /api/rates/latest_idr_rates | Fetch exchange rate using Strategy 1
GET | /api/rates/historical_idr_usd | Fetch exchange rate using Strategy 2
GET | /api/rates/supported_currencies | Fetch exchange rate using Strategy 3

### Example cURL Requests
curl -X GET http://localhost:8080/api/rates/latest_idr_rates
curl -X GET http://localhost:8080/api/rates/historical_idr_usd
curl -X GET http://localhost:8080/api/rates/supported_currencies

### Spread Factor Calculation
Spread = (Sell - Buy) / Sell

Example output:
Spread Factor = 0.00201

### Personalization
GitHub Username: Schwanzeirs

### Architectural Rationale
1. Polymorphism Justification — Strategy Pattern:
The Strategy Pattern was selected instead of using simple conditional logic (e.g., if/else or switch blocks) inside the service layer because:

- Extensibility — New data sources can be added easily by implementing the interface without modifying existing code (Open/Closed Principle).
- Maintainability — Each strategy contains its own independent transformation and parsing logic, minimizing coupling.
- Testability — Each strategy can be unit tested in isolation using mock responses.
- Clean Code — Avoids large conditional blocks that become increasingly complex and harder to maintain when additional external sources are added.

This ensures scalable support for future requirements such as XML sources or additional REST endpoints.

2. Client Factory — FactoryBean vs @Bean:
A FactoryBean is used to construct the external API client because:

- It provides dynamic and customizable instantiation, such as different base URLs, timeouts, or security tokens depending on environment values.
- Supports lazy creation and replacement for test scenarios more easily than a static @Bean.
- Allows separation of the client creation process and business logic, following SRP (Single Responsibility Principle).

If we used @Bean, we would hard-code the configuration making it harder to substitute mocked clients in integration tests.

3. Startup Runner Choice — ApplicationRunner vs @PostConstruct:
ApplicationRunner (or CommandLineRunner) was chosen instead of @PostConstruct because:

- It ensures the ingestion process runs after the full Spring context is initialized, including completed dependency injection and proxy creation.
- Provides clear lifecycle control and ensures database and WebClient beans are ready.
- More testable, since it can be executed and verified in integration tests independent of controller or bean initialization order.
- @PostConstruct runs too early in the lifecycle and may fail if external services or caches are not yet ready.

Thus, using an ApplicationRunner results in more predictable and officially recommended startup logic execution.
  

### Testing Summary
| Test Type         | Description                                                                       |
| ----------------- | --------------------------------------------------------------------------------- |
| Unit tests        | Validate all three strategy implementations and spread calculation                |
| Integration tests | Verify startup logic initializes datasets into memory with mocked WireMock server |
