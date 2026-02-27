# IDR Rate Aggregator Service

A Spring Boot application that fetches exchange rate data from the Frankfurter API and loads it into an in-memory store during application startup using `ApplicationRunner`.

The application exposes REST endpoints to retrieve:

* Latest IDR exchange rates
* Historical IDR → USD exchange rates
* List of Supported Currencies

---

# Tech Stack

* Java 21
* Spring Boot 3.5
* Spring WebFlux (`WebClient`)
* Maven
* JUnit 5
* Mockito

---

# Setup & Run Instructions

## 1. Clone Repository

```bash
git clone https://github.com/hosea-adrianus/idr-rate-aggregator.git
cd idr-rate-aggregator
```

## 2. Build the Project

```bash
./mvnw clean install
```

To skip tests:

```bash
./mvnw clean install -DskipTests
```

## 3. Run the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or using the generated JAR:

```bash
java -jar target/idr-rate-aggregator-0.0.1-SNAPSHOT.jar
```

Application runs at:

```
http://localhost:8080
```

## 4. Run Tests

```bash
mvn test
```

---

# Run Instructions with Docker
if you have Docker installed, you can build and run the application using Docker Compose.
```
docker compose up --build
```

---

# Configuration

The Frankfurter API base URL is configured in:

`src/main/resources/application.yml`

```yaml
external:
  frankfurter:
    base-url: https://api.frankfurter.app
```

---

# API Endpoints

Base URL:

```
http://localhost:8080/api/finance/data
```

## 1. Latest IDR Rates

Returns latest exchange rates with base currency IDR.

### Example cURL

```bash
curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates"
```

## 2. Historical IDR → USD

Returns historical USD exchange rates for IDR between predefined dates.

### Example cURL

```bash
curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd"
```

## 3. Supported Currencies

Returns a list of supported currencies.

### Example cURL

```bash
curl -X GET "http://localhost:8080/api/finance/data/supported_currencies"
```

---

# Personalization Note

**GitHub Username:**  
`hosea-adrianus`

**Rate USD:**  
`0.00006`

**Calculated Spread Factor:**  
`16738.0`

## Calculation Steps

1. **Sum ASCII values of GitHub username:**
    ```
   hosea-adrianus
   h=104, o=111, s=115, e=101, a=97, -=45
   a=97, d=100, r=114, i=105, a=97, n=110, u=117, s=115
   Sum = 104 + 111 + 115 + 101 + 97 + 45 + 97 + 100 + 114 + 105 + 97 + 110 + 117 + 115 = 1428
   ```
2. **Compute spread modifier:**
   ```
   spread = (sum % 1000) / 100000
   spread = (1428 % 1000) / 100000
   spread = 428 / 100000
   spread = 0.00428
    ```
3. **Calculate final Spread Factor using USD rate:**
    ```
    Spread Factor = (1 / rateUsd) * (1 + spread)
    = (1 / 0.00006) * (1 + 0.00428)
    ≈ 16666.6667 * 1.00428
    ≈ 16738.0
    ```
   
---

# Architectural Rationale

## i. Polymorphism Justification

The Strategy Pattern is used to handle the multi-resource endpoint so that each resource type (historical, latest, currencies) has its own implementation, avoiding bulky if-else or switch statements in the service layer.
Benefits:
- **Extensibility:** Adding a new resource only requires creating a new class implementing `IDRDataFetcher`, without changing the service.
- **Maintainability:** Each resource’s logic is isolated, making it easier to test and maintain.

## ii. Client Factory

A FactoryBean is used to construct the FrankfurterWebClient with dynamic configurations like baseUrl and ObjectMapper strategy.
Advantages over a simple @Bean:
- Encapsulates client creation logic, enabling shared configuration (e.g., interceptors, error handling) without redundant setup for each client instance.

## iii. Startup Runner Choice
ApplicationRunner is chosen to preload data at application startup because:
- It runs after the full Spring context is initialized (all dependencies injected).
- It is safer than @PostConstruct, which executes immediately after bean creation, before the full context is ready, ensuring external clients and services are available.