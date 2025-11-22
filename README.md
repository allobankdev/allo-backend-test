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
