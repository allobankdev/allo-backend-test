# Allo Bank Backend Developer Take-Home Test

This project is a Spring Boot application that aggregates **Indonesian Rupiah (IDR) finance data** from the public [Frankfurter Exchange Rate API](https://api.frankfurter.app/). It demonstrates clean architecture, Strategy pattern usage, thread-safe in-memory caching, and production-ready API design.

---

## ✅ Setup & Run Instructions

1. **Clone the repository:**

```bash
git clone https://github.com/your-username/allo-bank-backend-test.git
cd allo-bank-backend-test
```

2. **build and run application :**

Personal preference on vscode, run comment  ` FN + f5`

```bash
# install dependency
./mvnw clean install

# run application 
./mvnw spring-boot:run

# run test 
./mvnw test

```

3. **List end point**

```bash
#latest_idr_rates
curl -s http://localhost:8080/api/finance/data/latest_idr_rates

#historical_idr_usd
curl -s http://localhost:8080/api/finance/data/historical_idr_usd

#supported_currencies
curl -s http://localhost:8080/api/finance/data/supported_currencies

```


4.  **Personalization Note:**

GitHub username: DewaSRY
Spread Facto: 17079.15254237288
