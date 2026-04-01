# Allo Backend Test – IDR Rate Aggregator

## 👤 Author

GitHub: **imam-ramadani**

---

# 🚀 How to Run

## 1. Clone the repo

```bash
git clone https://github.com/username-kamu/allo-backend-test.git
cd allo-backend-test/imam
```

## 2. Build

```bash
mvn clean install
```

## 3. Run the app

```bash
mvn spring-boot:run
```

App will run on:

```
http://localhost:8080
```

## 4. Run tests

```bash
mvn test
```

---

# 🌐 API Endpoints

Base URL:

```
http://localhost:8080/api/finance/data
```

### 1. Latest IDR Rates

```bash
http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2. Supported Currencies

```bash
http://localhost:8080/api/finance/data/supported_currencies
```

### 3. Historical IDR → USD

```bash
http://localhost:8080/api/finance/data/historical_idr_usd
```

---

# 🧮 Personalization

GitHub Username: **imam-ramadani**

Uniicode sum : 1294

Spread Factor: 0.00294

---

# 🛠️ Architecture Notes

## Why Strategy Pattern?

I used the Strategy Pattern to handle different resource types (`latest`, `currencies`, `historical`) instead of putting everything in one big `if-else`.

Each type has its own class implementing `IDRDataFetcher`.

**Why this helps:**

* Easier to add new data types later
* Cleaner code (no long conditional blocks)
* Each piece is easier to test

---

## Why FactoryBean for the client?

The `FrankfurterClient` is created using a `FactoryBean`.

Honestly, this could also be done with `@Bean`, but I used `FactoryBean` to:

* keep the client creation logic separate
* make it easier to manage/configure if it grows more complex later

---

## Why ApplicationRunner?

I used `ApplicationRunner` to load data when the app starts.

Why not `@PostConstruct`?

* `ApplicationRunner` runs after the whole Spring context is ready
* better for doing multiple API calls safely

So on startup:

* all fetchers run
* data is fetched
* stored in memory

---

# 🔁 How the Flow Works

1. App starts
2. `ApplicationRunner` triggers all fetchers
3. Each fetcher calls the external API via `WebClient`
4. Response is mapped into DTO
5. Data is stored in memory (`DataStoreService`)
6. API just reads from memory (no external call per request)

---

# 🧪 Testing

* Unit tests for each fetcher (using Mockito, so no real API call)
* 1 integration test to make sure data is loaded at startup

---

# 📌 Notes

* Data is fetched once at startup → faster response
* No database used (in-memory store is enough for this use case)
* `WebClient` is used for calling external API

---
