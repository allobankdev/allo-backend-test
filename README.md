# IDR Rate Aggregator Service

A robust Spring Boot REST API designed to aggregate financial data from the Frankfurter Exchange Rate API, with a primary focus on Indonesian Rupiah (IDR). This service demonstrates advanced architectural patterns including Strategy, FactoryBean, and Thread-Safe Caching.

## 👤 Personalization Note

* **GitHub Username:** `aldobuarlele`
* **Spread Factor Calculation:**
    1.  **Input String:** `aldobuarlele`
    2.  **Sum of ASCII values:**
        * `a` (97) + `l` (108) + `d` (100) + `o` (111) + `b` (98) + `u` (117) + `a` (97) + `r` (114) + `l` (108) + `e` (101) + `l` (108) + `e` (101)
        * **Total Sum:** `1260`
    3.  **Modulo Operation:** `1260 % 1000` = `260`
    4.  **Division:** `260 / 100000.0`
    5.  **Final Spread Factor:** **`0.00260`**

---

## 🚀 Setup & Installation

### Prerequisites
* Java 17 or higher
* Maven 3.6+

### Build and Run

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/aldobuarlele/allo-backend-test.git](https://github.com/aldobuarlele/allo-backend-test.git)
    cd allo-backend-test
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

The server will start on port `8080`.

---

## 📡 API Usage & Endpoints

There is a single unified endpoint structure: `GET /api/finance/data/{resourceType}`.

### 1. Get Latest IDR Rates (with Spread)
Fetches the latest rates relative to IDR. This endpoint includes a custom calculated field `USD_BuySpread_IDR`.

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```
**response:**
```bash
[
    {
        "date": "2026-02-06",
        "base": "IDR",
        "rates": {
            "USD_BuySpread_IDR": 16993.22,
            "USD": 0.000064,
            "EUR": 0.000059,
            "JPY": 0.0094,
            "SGD": 0.000086,
            "AUD": 0.000098
            // ... containing all other supported currencies
        }
    }
]
```


### 2. Get Historical Data (IDR to USD)
Fetches time-series data for IDR to USD exchange rates over a specific date range (configured in application.yml).

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```
**response:**
```bash
[
    {
        "amount": 1.0,
        "base": "IDR",
        "start_date": "2023-12-29",
        "end_date": "2024-01-05",
        "rates": {
            "2023-12-29": { "USD": 0.000065 },
            "2024-01-02": { "USD": 0.000064 },
            "2024-01-03": { "USD": 0.000064 },
            "2024-01-04": { "USD": 0.000064 },
            "2024-01-05": { "USD": 0.000064 }
        }
    }
]
```

### 3. Get Supported Currencies
Fetches the list of all available currency symbols and their full names.

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```
**response:**
```bash
[
    {
        "AUD": "Australian Dollar",
        "BRL": "Brazilian Real",
        "IDR": "Indonesian Rupiah",
        "USD": "United States Dollar",
        "EUR": "Euro",
        "JPY": "Japanese Yen",
        "SGD": "Singapore Dollar"
        // ... list continues for all currencies
    }
]
```
