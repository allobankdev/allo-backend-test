# IDR Exchange Service

A Spring Boot application that fetches exchange rate data from the Frankfurter API, processes it, caches it on startup, and exposes REST endpoints to retrieve the cached results. The cache is immutable after the application has finished loading.

---

## 🚀 Features

* Fetches:

  * Latest IDR exchange rates
  * Historical data (IDR → USD)
  * Supported currencies list
* In-memory immutable cache initialized using `ApplicationRunner`.
* Graceful error handling and logging for HTTP failures.
* Includes full integration and unit tests.

---

## 📦 Requirements

* Java 17+
* Maven 3.9+
* Internet connection (for calling Frankfurter API)

---

## 📁 Project Structure

```
src/main/java
  ├─ cache/                 # In-memory resource cache
  ├─ integration/           # External API clients
  ├─ service/               # Business logic
  ├─ controller/            # REST endpoints
  ├─ boot/                  # ApplicationRunner data loader
  ├─ util/                  # Financial calculator
  └─ properties/            # Configuration binding
```

---

## ⚙️ Setup & Run Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/candra160391/allo-backend-test.git
cd allo-backend-test
```

### 2️⃣ Build the Application

```bash
mvn clean install
```

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

Or run the generated jar:

```bash
java -jar target/allo-bank-test.jar
```

---

## 🧪 Running Tests

### Run All Tests

```bash
mvn test
```

---

## 🧵 Configuration

All external API configuration are stored in:

```
src/main/resources/application.yml
```

Example:

```yaml

integration:
  api:
    frankfurter:
      #        base-url: http://localhost:8080/v1
      base-url: https://api.frankfurter.app
      path:
        lates-idr: ${integration.api.frankfurter.base-url}/latest
        currencies: ${integration.api.frankfurter.base-url}/currencies
        exchange-history: ${integration.api.frankfurter.base-url}```

 ``` 

GitHub username (for spread factor calculation):
```yaml
github:
  username: candra160391
```

---

## 🌐 Available Endpoints

| Method | Endpoint                    | Description                  |
| ------ | --------------------------- | ---------------------------- |
| GET    | `/api/latest-idr-rates`     | Latest exchange rate data    |
| GET    | `/api/historical-idr-usd`   | Historical IDR→USD data      |
| GET    | `/api/supported-currencies` | List of supported currencies |

All responses follow:

```json
{
  "status": "success",
  "data": { ... },
  "timestamp": "2025-11-19T12:00:00Z"
}
```

---

## 🧠 Cache Initialization

* Cache is loaded **on application startup** via `ApplicationRunner`.
* Once loaded, the data is immutable and thread-safe.
* If API calls fail, fallback empty values are used.

---

## 🐞 Error Handling

* All WebClient calls:

  * Log 4xx/5xx errors
  * Provide fallback default responses

---

### 🔎 Personalization Note

This project was completed by **GitHub user: `candra160391`**.

The system generates a unique Spread Factor for each developer based on their GitHub username.
This value influences how the USD buy rate in IDR is calculated.

```
SpreadFactor = (sum_of_unicode_characters(username) * 1000) / 100000
             = (925 × 1000) / 100000
             = 9.25000

```

Calculating USD Buy Rate in IDR
```
BuyRateIDR = (1 / USD_Rate) × (1 + SpreadFactor)
           = (1 / 0.00006) * (1 * 9.25000)
           = 170833.33358
```


### 🔎 Architectural Rationale
1. Polymorphism Justification (Strategy Pattern vs. Conditional Logic)

   a. Open/Closed Principle: New strategies (e.g., new exchange rate types) can be added without modifying existing service logic. 

   b. Isolated responsibilities: Each fetcher only needs to focus on handling its own data retrieval logic.

   c. Dependency inversion: The service layer depends on abstraction (IDRDataFetcher) instead of concrete classes.

   d. Testability: Each fetcher can be unit tested independently without mocking large branching structures.


2. Client Factory Rationale (Why use FactoryBean)
   
   a. Deferred and controlled instantiation: FactoryBean gives full control over how and when the WebClient is constructed.

   b. Testing convenience: Unit or integration tests can override the factory to point to MockWebServer or any other simulated endpoint without touching business code.


4. Startup Runner Choice (ApplicationRunner vs. @PostConstruct)
   
   a. ApplicationRunner executes after the entire application has started, ensuring all beans are ready to use, while @PostConstruct runs before Spring finishes initializing the full application context. At that moment, caching infrastructure may not be ready.
   
   b. Clear startup contracts: “Execute this block at application boot before handling real requests.”

   c. Full application readiness, better error handling.