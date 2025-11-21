# Application Documentation

This Spring Boot application aggregates Indonesian Rupiah (IDR) financial data from the public Frankfurter API using clean architecture patterns such as the Strategy Pattern, FactoryBean-based client creation, and immutable in-memory caching loaded at startup.

---

## Setup Instructions

### Prerequisites

- **Java 17** 
- **Maven 3.9+**
- **Git**
- **Internet connection** (to access Frankfurter API)

### Step 1: Clone the Repository

```bash
git clone https://github.com/lukypanca/allo-backend-test.git
```

### Step 2: Change Branch to feat/idr-rate-aggregator

```bash
git checkout feat/idr-rate-aggregator
```

---
### Step 3: Build the Application
```bash
mvn clean install
```

---
### Step 4 Run All Tests

```bash
mvn test 
```

---
### Step 5: Run the Application

```bash
mvn spring-boot:run
```

### Verify Application Started Successfully

You should see output similar to:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.7)

2025-11-21T15:35:08.782+07:00  INFO 27984 --- [allo-bank] [           main] c.example.allo_bank.AlloBankApplication  : Starting AlloBankApplication using Java 17.0.15 with PID 27984 (C:\Users\Luky\Documents\Project\allobank\allo-backend-test\target\classes started by Luky in C:\Users\Luky\Documents\Project\allobank\allo-backend-test)
2025-11-21T15:35:08.784+07:00  INFO 27984 --- [allo-bank] [           main] c.example.allo_bank.AlloBankApplication  : No active profile set, falling back to 1 default profile: "default"
2025-11-21T15:35:09.982+07:00  INFO 27984 --- [allo-bank] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8081 (http)
2025-11-21T15:35:09.995+07:00  INFO 27984 --- [allo-bank] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-11-21T15:35:09.995+07:00  INFO 27984 --- [allo-bank] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.48]
2025-11-21T15:35:10.056+07:00  INFO 27984 --- [allo-bank] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-11-21T15:35:10.057+07:00  INFO 27984 --- [allo-bank] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1211 ms
2025-11-21T15:35:10.728+07:00  INFO 27984 --- [allo-bank] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8081 (http) with context path '/'
2025-11-21T15:35:10.738+07:00  INFO 27984 --- [allo-bank] [           main] c.example.allo_bank.AlloBankApplication  : Started AlloBankApplication in 2.486 seconds (process running for 2.9)
```

The application will start on **http://localhost:8081** by default can be changed on application.yml.

---

## API Endpoint Usage

### Base Endpoint

```
GET /api/finance/data/{resourceType}
```

### Resource Types

| resourceType | Description                                                          |
|---|----------------------------------------------------------------------|
| `latest_idr_rates` | Latest IDR exchange rates with custom USD buy spread |
| `historical_idr_usd` | Historical IDR/USD rates from start date to end date                 |
| `supported_currencies` | List of all supported currency codes and names                       |

---

### 1. Get Latest IDR Rates with Spread Factor

**Request:**

```bash
curl -X GET "http://localhost:8081/api/finance/data/latest_idr_rates" \
  -H "Accept: application/json"
```

**Response (200 OK):**

```json
{
  "resourceType": "latest_idr_rates",
  "status": "success",
  "data": {
    "base": "IDR",
    "date": "2025-11-20",
    "rates": {
      "USD": 0.000060
    },
    "usdBuySpreadIdr": 16828.00000
  }
}
```

---

### 2. Get Historical IDR/USD Data

**Request:**

```bash
curl -X GET "http://localhost:8081/api/finance/data/historical_idr_usd" \
  -H "Accept: application/json"
```

**Response (200 OK):**

```json
{
  "resourceType": "historical_idr_usd",
  "status": "success",
  "data": {
    "amount": 1.0,
    "base": "IDR",
    "startDate": "2023-12-29",
    "endDate": "2024-01-05",
    "rates": {
      "2023-12-29": {
        "USD": 0.000065
      },
      "2024-01-02": {
        "USD": 0.000064
      },
      "2024-01-03": {
        "USD": 0.000064
      },
      "2024-01-04": {
        "USD": 0.000064
      },
      "2024-01-05": {
        "USD": 0.000064
      }
    }
  }
}
```

---

### 3. Get Supported Currencies

**Request:**

```bash
curl -X GET "http://localhost:8081/api/finance/data/supported_currencies" \
  -H "Accept: application/json"
```

**Response (200 OK):**

```json
{
  "resourceType": "supported_currencies",
  "status": "success",
  "data": {
    "AUD": "Australian Dollar",
    "BGN": "Bulgarian Lev",
    "BRL": "Brazilian Real",
    "CAD": "Canadian Dollar",
    "CHF": "Swiss Franc",
    "CNY": "Chinese Renminbi Yuan",
    "CZK": "Czech Koruna",
    "DKK": "Danish Krone",
    "EUR": "Euro",
    "GBP": "British Pound",
    "HKD": "Hong Kong Dollar",
    "HUF": "Hungarian Forint",
    "IDR": "Indonesian Rupiah",
    "ILS": "Israeli New Sheqel",
    "INR": "Indian Rupee",
    "ISK": "Icelandic Króna",
    "JPY": "Japanese Yen",
    "KRW": "South Korean Won",
    "MXN": "Mexican Peso",
    "MYR": "Malaysian Ringgit",
    "NOK": "Norwegian Krone",
    "NZD": "New Zealand Dollar",
    "PHP": "Philippine Peso",
    "PLN": "Polish Złoty",
    "RON": "Romanian Leu",
    "SEK": "Swedish Krona",
    "SGD": "Singapore Dollar",
    "THB": "Thai Baht",
    "TRY": "Turkish Lira",
    "USD": "United States Dollar",
    "ZAR": "South African Rand"
  }
}
```

---

### Error Handling Examples

**Invalid Resource Type:**

```bash
curl -X GET "http://localhost:8081/api/finance/data/invalid_type"
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2025-11-21T08:31:58.541+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/finance/data/latest_idr_rateS"
}
```

---

## Personalization Details



### Personalization Calculated Spread Factor

**GitHub Username: `lukypanca`**

```
✅ Spread Factor: 0.00968
✅ Calculation Verification:
   - Username: lukypanca
   - Sum of Unicode values: 968
   - Spread Factor: 0.00968
```

---


## 🛠️ Architectural Rationale

### i. Polymorphism Justification (Strategy Pattern)
The Strategy Pattern is used to avoid a large conditional block and to keep each resource’s logic isolated. It allows the controller to dynamically select the correct handler using a Spring-injected strategy map, improving extensibility and maintainability. Adding a new resource requires only creating a new strategy class—no changes to the controller—fully complying with the Open/Closed Principle. It also makes testing and refactoring significantly easier because each strategy is independently testable and self-contained.

---

### ii. Client Factory (Why FactoryBean Instead of @Bean)
A FactoryBean provides full control over how the external API client is constructed, including applying base URL configuration, timeouts, headers, and other customization in a clean, centralized way. Unlike a simple `@Bean`, a FactoryBean encapsulates the entire client-creation lifecycle and separates configuration concerns from application logic. This approach produces a more flexible and production-ready client and satisfies the requirement to avoid defining the client in a standard `@Bean` method.

---

### iii. Startup Runner Choice (Why ApplicationRunner Over @PostConstruct)
`ApplicationRunner` executes after the entire Spring context has been initialized, ensuring all strategies, configuration properties, and the API client are fully ready before data loading begins. It is safer for performing external API calls and heavy initialization tasks, which are not recommended inside `@PostConstruct` due to lifecycle timing issues. Using a runner also guarantees that the in-memory cache is fully populated before the API endpoint receives any requests, providing predictable and thread-safe startup behavior.

---
