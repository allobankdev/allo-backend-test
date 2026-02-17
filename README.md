## 🧾 Frankfurter Finance API

A Spring Boot application that aggregates and transforms financial data from the public Frankfurter Exchange Rate API, focusing on Indonesian Rupiah (IDR).

---

## 🚀 Tech Stack

* Java 21
* Spring Boot 4.0.2
* Maven
* WebClient (Spring Reactive Web)
* Strategy Design Pattern
* FactoryBean
* ApplicationRunner
* Immutable In-Memory Store

---

## 📌 Features

* Single polymorphic endpoint:

  ```
  GET /api/finance/data/{resourceType}
  ```

* Supported resource types:

  * `latest_idr_rates`
  * `historical_idr_usd`
  * `supported_currencies`

* Startup data preloading

* Thread-safe immutable in-memory storage

* Custom USD buy spread calculation based on GitHub username

---

## ⚙️ Setup & Run

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/<your-username>/frankfurter.git
cd frankfurter
```

### 2️⃣ Build

```bash
mvn clean install
```

### 3️⃣ Run

```bash
mvn spring-boot:run
```

Application will start on:

```
http://localhost:8080
```

---

## 📡 Endpoint Usage

### 1️⃣ Latest IDR Rates

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

---

### 2️⃣ Historical IDR → USD

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

---

### 3️⃣ Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## 🧮 Personalization: Spread Factor

GitHub Username: `yourgithubusername`

Spread Factor Calculation:

```
Sum of ASCII values = 1237
Spread Factor = (1237 % 1000) / 100000.0
Spread Factor = 0.001237
```

USD Buy Spread Formula:

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)
```

---

## 🏗️ Architectural Rationale

### 1️⃣ Why Strategy Pattern?

The application supports multiple resource types through a single endpoint.
Using the Strategy Pattern ensures:

* Open/Closed Principle compliance
* No conditional logic in controller layer
* Easy extensibility for new resource types
* Clean separation of transformation logic

---

### 2️⃣ Why FactoryBean?

The WebClient instance is created using a custom FactoryBean to:

* Externalize base URL configuration
* Centralize client construction logic
* Encapsulate initialization concerns
* Maintain clean separation of concerns

---

### 3️⃣ Why ApplicationRunner Instead of @PostConstruct?

ApplicationRunner ensures:

* Execution after full Spring context initialization
* Clear lifecycle control
* Better testability
* Deterministic startup data loading

---

## 🧪 Testing Strategy

### Unit Tests

* Strategy implementations
* Spread calculation logic

### Integration Tests

* ApplicationRunner initialization
* In-memory data loading verification

---

## 📦 External API Reference

Data is retrieved from:

```
https://api.frankfurter.app/
```

## API Response Examples
<img width="400" height="450" alt="Screenshot (142)" src="https://github.com/user-attachments/assets/80342eed-2973-485d-8e5e-b5eecbe304bb" />

---
<img width="500" height="450" alt="Screenshot (140)" src="https://github.com/user-attachments/assets/74d1f87b-8255-4f30-85d9-27eba7b50e09" />

---
<img width="450" height="350" alt="Screenshot (141)" src="https://github.com/user-attachments/assets/13428d3f-99c5-4af2-8091-ae603e029bf6" />

---

