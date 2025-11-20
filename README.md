**AlloBank IDR Finance API**

**I. Core Task: The Polymorphic API**

**1\. External API Integration (Frankfurter API)**

- **Base URL (Public):** <https://api.frankfurter.app/>
- The service integrates with three distinct data resources:
    - /latest?base=IDR - The latest rates relative to IDR.
    - **Historical Data:** Query a small time series, e.g., /2024-01-01..2024-01-05?from=IDR&to=USD.  
      **Note:** Use the date range provided unless otherwise specified.
    - /currencies - The list of all supported currency symbols.

**2\. Internal API Endpoint**

- **Endpoint:**

GET /api/currency/data/{resourceType}

- **Allowed resourceType values:**

| **Resource Type**    | **Description**                                           |
|----------------------|-----------------------------------------------------------|
| latest_idr_rates     | Latest IDR rates with USD_BuySpread_IDR calculation.      |
| historical_idr_usd   | Historical USD rates for IDR over the defined date range. |
| supported_currencies | List of all supported currencies and their names.         |

**3\. Usage**

**Run the service:**

mvn spring-boot:run

Service runs at:

<http://localhost:8080>

**cURL Examples:**

\# Latest IDR rates

curl -X GET <http://localhost:8080/api/currency/data/latest_idr_rates>

\# Historical IDR -> USD rates

curl -X GET <http://localhost:8080/api/currency/data/historical_idr_usd>

\# Supported currencies

curl -X GET <http://localhost:8080/api/currency/data/supported_currencies>

**Postman Requests:**

- GET <http://localhost:8080/api/currency/data/latest_idr_rates>
- GET <http://localhost:8080/api/currency/data/historical_idr_usd>
- GET <http://localhost:8080/api/currency/data/supported_currencies>

**4\. Notes**

- The service **fetches all data once at startup** and caches it in an **immutable, thread-safe in-memory store**.
- latest_idr_rates includes a **personalized USD_BuySpread_IDR**, calculated from your GitHub username:

frankfurter-api.github.username=alwi-maulana

- Adding new resources is easy: implement a new CurrencyDataFetcher bean and annotate it with @Component("
  &lt;resource_name&gt;").

**5\. Author**

**Author:** Alwi Maulana  
**GitHub Username:** alwi-maulana  
**Spread Factor:** 0.00765 (computed from GitHub username)