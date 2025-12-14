**Allo Backend Test**

This repository contains the full implementation for the Allo Backend Take-Home Test.
The application integrates with the Frankfurter API and exposes structured endpoints to retrieve:

- Supported currencies
- Latest IDR exchange rate
- Historical IDR/USD exchange rates

It includes a modular fetcher system, internal caching, clean DTOs, global exception handling, and full integration tests.

**1. Setup & Run Instructions**

**Clone**
```bash
git clone https://github.com/achlaq/allo-backend-test.git
cd allo-backend-test
```

**Build**
```bash
mvn clean install
```

**Run Application**
```bash
mvn spring-boot:run
```


The application will start at:
```
http://localhost:7685
```

**Run Tests**
```bash
mvn test
```

**2. Endpoint Usage**

Below is one example.

The API supports: currencies, latest, and historical.

📌 Example: Fetch Latest IDR Rate

**Request**
```bash
curl -X GET "http://localhost:7685/api/finance/data/latest_idr_rates"
```

**Sample Response**
```json
[
  {
    "date": "2025-12-12",
    "base": "IDR",
    "rates": {
      "AUD": 0.00009,
      "BGN": 0.0001,
      "BRL": 0.00032,
      "CAD": 0.000083,
      "CHF": 0.000048,
      "CNY": 0.00042,
      "CZK": 0.00124,
      "DKK": 0.00038,
      "EUR": 0.000051,
      "GBP": 0.000045,
      "HKD": 0.00047,
      "HUF": 0.01968,
      "ILS": 0.00019,
      "INR": 0.00544,
      "ISK": 0.0076,
      "JPY": 0.00937,
      "KRW": 0.0887,
      "MXN": 0.00108,
      "MYR": 0.00025,
      "NOK": 0.00061,
      "NZD": 0.0001,
      "PHP": 0.00355,
      "PLN": 0.00022,
      "RON": 0.00026,
      "SEK": 0.00056,
      "SGD": 0.000078,
      "THB": 0.00189,
      "TRY": 0.00256,
      "USD": 0.00006,
      "ZAR": 0.00101
    },
    "spreadFactor": 0.00618,
    "usdBuySpreadIdr": 16769.6667
  }
]
```
**3. Architectural Rationale**

**Strategy Pattern (Polymorphism)**

Used instead of conditional logic to ensure clean separation of concerns, easy extensibility, and maintainability.
Adding a new resource type requires only adding a new strategy class—no modification to existing logic (Open–Closed Principle).

**FactoryBean for API Client**

FactoryBean provides controlled and customizable instantiation of the WebClient, ideal for external API clients with dynamic configuration or lifecycle requirements.
It also improves encapsulation and testability compared to a simple @Bean.

**ApplicationRunner Instead of @PostConstruct**

ApplicationRunner executes after the Spring context is fully initialized, making it safe for API calls and startup data ingestion.
@PostConstruct runs too early and may cause failures due to incomplete bean initialization.

**4. Personalization Note**

**GitHub Username**
achlaq

**Spread Factor**
0.00618
