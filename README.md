**Allo Backend Test**

This repository contains the full implementation for the Allo Backend Take-Home Test.
The application integrates with the Frankfurter API and exposes structured endpoints to retrieve:

- Supported currencies
- Latest IDR/USD exchange rate
- Historical IDR/USD exchange rates

It includes a modular fetcher system, internal caching, clean DTOs, global exception handling, and full integration tests.

**1. Setup & Run Instructions**

**Clone**
```bash
git clone https://github.com/achlaq/allo-backend-test.git
cd allo-backend-test
```

**Build**
```bash
mvn clean install
```

**Run Application**
```bash
mvn spring-boot:run
```


The application will start at:
```
http://localhost:7685
```

**Run Tests**
```bash
mvn test
```

**2. Endpoint Usage**

Below is one example.

The API supports: currencies, latest, and historical.

📌 Example: Fetch Latest IDR/USD Rate

**Request**
```bash
curl -X GET "http://localhost:7685/api/exchange?resource=latest"
```

**Sample Response**
```json
{
  "date": "2025-01-01",
  "base": "USD",
  "rates": {
    "IDR": 15750.12
  }
}
```
**3. Architectural Rationale**

**Strategy Pattern (Polymorphism)**

Used instead of conditional logic to ensure clean separation of concerns, easy extensibility, and maintainability.
Adding a new resource type requires only adding a new strategy class—no modification to existing logic (Open–Closed Principle).

**FactoryBean for API Client**

FactoryBean provides controlled and customizable instantiation of the WebClient, ideal for external API clients with dynamic configuration or lifecycle requirements.
It also improves encapsulation and testability compared to a simple @Bean.

**ApplicationRunner Instead of @PostConstruct**

ApplicationRunner executes after the Spring context is fully initialized, making it safe for API calls and startup data ingestion.
@PostConstruct runs too early and may cause failures due to incomplete bean initialization.

**4. Personalization Note**

**GitHub Username**
achlaq

**Spread Factor**
0.00765
