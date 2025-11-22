# Allo Bank Backend Developer Take-Home Test
This repository was developed as part of the **Allo Bank Backend Technical Test**.

It fetches and transforms data from three external API endpoints that aggregate IDR exchange data from `https://api.frankfurter.app/`

***List of Fetched Endpoints:***
```shell
curl --location 'https://api.frankfurter.app/latest?base=IDR'
curl --location 'https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD'
curl --location 'https://api.frankfurter.app/currencies'
```
---
## I. Setup/Run Instructions

### 1. Clone the Repository
```sh
git clone -b feat/idr-rate-aggregator https://github.com/vilis-iv/allo-backend-test.git
cd allo-backend-test/
```

### 2. Build Project
```sh
./mvnw clean package
```

### 3. Run Application
```sh
./mvnw spring-boot:run
```
---
## II. Usage
execute the following cURL in terminal 

### 1. latest_idr_rates
```shell
curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates'
```

### 2. historical_idr_usd
```shell
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd'
```

### 3. supported_currencies.
```shell
curl --location 'http://localhost:8080/api/finance/data/supported_currencies'
```

_note: importing to postman is also possible by copying the curl using postman build-in import feature_

---
## III. Personalization Note
- **GitHub Username**: vilis-iv
- **Spread Factor**: 0.00819
---

### 🛠️ Architectural Rationale

**1. Polymorphism Justification:** Explain *why* the Strategy Pattern was used over a simpler conditional block in the service layer for handling the multi-resource endpoint. Discuss the benefits in terms of **extensibility** and **maintainability**.
> By defining dedicated interface `IDRDataFetcher` ensure consistency for each implementation (`LatestIdrRatesImpl`, `HistoricalIdrUsdImpl`, `SupportedCurrenciesImpl`) 
> to remain true to their main functionality while encapsulating each implementation logic. This keep each implementation isolated, testable, 
> simpler to debug due to clear separation of responsibility.
>
> Using a map-based lookup in `ExchangeRateController` improves extensibility, because adding new business flow will only 
> focus on implementation without adding or modifying the Controller.
---

**2. Client Factory:** Explain the specific role and benefit of using a **`FactoryBean`** to construct the external API client. Why is this preferable to defining the client using a standard `@Bean` method in this scenario?
> Using `FactoryBean` to construct external API client `FrankFurtherApiClientConfig` ensure every `WebClient` injection 
> receive the same tuned configuration without duplicating setup logic.
> It also encapsulate the construction process in a dedicated factory class rather than a method within a configuration class.
> 
> While standard `@Bean` method could do similar setup, using `FactoryBean` ensure `WebClient` setup to be separated from general configuration,
> keeping the construction logic isolated and easier to manage as the `WebClient` configuration becomes more complex.
---

**3. Startup Runner Choice:** Justify the choice of using an `ApplicationRunner` (or `CommandLineRunner`) for the initial data ingestion over a simpler `@PostConstruct` method.
> `ApplicationRunner` is chosen because it executed only after Spring Boot application has fully started and all beans and dependencies including `WebClient` are ready, 
> ensuring the Startup Runner can safely perform external API calls and store the result into in-memory storage.
> 
> `@PostConstruct` method is not suitable for this use-case, because it runs too early before Spring Boot completely ready.
> This can cause external API calls get executed before `WebClient` is ready
> risking failure during startup.

---