# IDR Rate Aggregator

Polymorphic IDR rate aggregator built with Spring Boot.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 4
- Maven
- WebClient (Reactive)
- JUnit 5
- Mockito

---

## 🏗 Architecture Overview

### ✅ Strategy Pattern
Three different resource types are handled via polymorphism:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Each resource has its own `IDRDataFetcher` implementation.

The controller does not use `if` or `switch`.  
Strategy resolution is handled via Spring map injection.

---

### ✅ FactoryBean for WebClient

The external API client is created using a custom `FactoryBean<WebClient>` implementation.

This allows:
- Centralized client construction
- Externalized base URL configuration
- Clean separation of configuration responsibility

---

### ✅ Startup Data Initialization

All external data is fetched **once** during application startup using `ApplicationRunner`.

The results are stored in an immutable, thread-safe in-memory store.

No external API call is made during request handling.

---

## 📦 Setup & Run

### 1. Clone repository

```bash
git clone https://github.com/IlhamX7/idr-rate-aggregator.git
cd idr-rate-aggregator
```

### 2.️ Build

```bash
mvn clean install
```

### 3. Run

```bash
mvn spring-boot:run
```
Application runs at: http://localhost:8080

### 4. API Usage

- Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

- Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

- Historical IDR → USD
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
``` 

🔢 Personalization (Spread Factor)

GitHub Username: IlhamX7

Spread Factor Calculation:

1. Convert username to lowercase

2. Sum ASCII values

3. (sum % 1000) / 100000.0

Calculated Spread Factor:

```bash
0.0XXXX
```

(Automatically computed in SpreadCalculator)


### 5. Testing

Run tests:
```bash
mvn test
```

📁 Project Structure

```bash
config/
client/
controller/
exception/
model/dto/
service/
strategy/
util/
```

🧠 Architectural Rationale

1️⃣ Why Strategy Pattern?

Instead of using conditionals (if/switch), Strategy:

- Enables Open/Closed Principle

- Allows easy extension for new resource types

- Improves maintainability

- Keeps controller clean and decoupled

Adding a new resource only requires a new strategy implementation.

2️⃣ Why FactoryBean for WebClient?

Using FactoryBean:

- Centralizes client configuration

- Avoids direct @Bean definition

- Enables flexible lifecycle management

- Cleanly separates configuration logic from business logic

3️⃣ Why ApplicationRunner instead of @PostConstruct?

ApplicationRunner:

- Ensures execution after full application context initialization

- Cleaner separation of initialization logic

- More testable and explicit than @PostConstruct

- Recommended for startup workflows in Spring Boot

✅ Production Considerations

- Thread-safe immutable in-memory store

- Graceful error handling for missing resources

- Clean separation of concerns

- Unit & integration tests provided

👨‍💻 Author
```bash
Ilham
```
