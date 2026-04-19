# 💱 IDR Rate Aggregator

A Spring Boot application that aggregates exchange rate data for the Indonesian Rupiah (IDR) from an external API. It provides endpoints to retrieve the latest IDR rates with a spread factor calculated based on the GitHub username, historical IDR to USD rates, and a list of all available currencies.

---

# 🚀 Setup & Run

## 1. Clone the Project

```bash
git clone -b feat/idr-rate-aggregator https://github.com/allobankdev/allo-backend-test.git
cd allo-backend-test
```

## 2. Build the Project

```bash
mvn clean install
```

## 3. Run the Application

```bash
mvn spring-boot:run
```

Application will run at:

```
http://localhost:8080
```

---

# 📡 API Endpoints

## Get Data by Type

```
GET /api/finance/data/{type}
```

### Example cURL

```bash
curl http://localhost:8080/api/finance/data/latest_idr
```

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

```bash
curl http://localhost:8080/api/finance/data/currencies
```

---

# 📌 Example Responses

## ✅ Success

```json
{
  "base": "IDR",
  "rates": {
    "USD": 0.000058
  }
}
```

## ❌ Not Found

```json
{
  "timestamp": "2026-04-19T10:36:34.005Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/finance/data/invalid"
}
```

---

# 🧮 Spread Factor

Spread is calculated using the formula:

```
spread = (sum of ASCII values of github username % 1000) / 100000
```

---

## 👤 GitHub Username

```
mikleo04
```

---

## 🔍 Detailed Calculation

### Step 1: Convert each character to ASCII

```
m(109) + i(105) + k(107) + l(108) + e(101) + o(111) + 0(48) + 4(52)
```

### Step 2: Sum all values

```
Total = 741
```

### Step 3: Apply formula

```
spread = (741 % 1000) / 100000
       = 741 / 100000
       = 0.00741
```

---

## 🔢 Final Spread Result

```
0.00741 (~0.741%)
```

---

# 🏗️ Architecture & Design Decisions

---

## 🎯 Why Strategy Pattern?

The Strategy Pattern is used to handle different resource types:

* latest
* historical
* currencies

### ✅ Advantages:

* Easy to extend with new resource types
* Avoids complex conditional (`if/else`) logic
* Promotes modular design

### ❌ Compared to if/else:

| Strategy Pattern | if/else               |
| ---------------- | --------------------- |
| Extensible       | Hard to extend        |
| Clean structure  | Becomes messy quickly |
| Maintainable     | Error-prone           |

👉 Focus: **extensibility & maintainability**

---

## 🏭 Why FactoryBean?

Used to create and configure the RestTemplate instance.

### ✅ Advantages:

* Full control over object creation
* Can encapsulate complex configuration (timeout, interceptor, retry)
* More flexible than standard bean creation

### ❌ Compared to `@Bean`:

| FactoryBean                   | @Bean                 |
| ----------------------------- | --------------------- |
| More flexible                 | Simpler               |
| Supports complex logic        | Limited               |
| Better for advanced use cases | Good for simple cases |

---

## ⚙️ Why ApplicationRunner?

Used to load data from the external API during application startup.

### ✅ Advantages:

* Executed after Spring context is fully initialized
* Safe to use other beans
* Ideal for startup logic

### ❌ Compared to `@PostConstruct`:

| ApplicationRunner          | @PostConstruct                 |
| -------------------------- | ------------------------------ |
| Context fully ready        | Context not fully ready        |
| Safer for dependencies     | Risky                          |
| Suitable for startup logic | Only for simple initialization |

---

# 🧪 Testing

This project includes:

* Unit Tests (Service, Client, DataStore)
* Integration Tests:

  * External API test
  * ApplicationRunner startup test

Run all tests:

```bash
mvn test
```

---

# 📦 Tech Stack

* Java 17
* Spring Boot
* RestTemplate
* JUnit 5
* Mockito

---

# 📌 Notes

* Uses public API: https://api.frankfurter.app
* Data is stored in-memory
* No database is used
* Includes retry mechanism and error handling for external API calls

---

# 👨‍💻 Author

GitHub: https://github.com/mikleo04
