# Exchange Service

**Modern microservice** for fetching, caching and serving currency exchange rates.

Clean architecture · Strategy Pattern · Spring Boot · Java 21


## ✨ Features

- Real-time exchange rates from **Frankfurter API**  
- In-memory caching initialized at startup  
- Clean REST API (`/api/finance/data/{resourceType}`)  
- Extensible **Strategy Pattern** for new data sources  
- Graceful startup & failure-resilient data loading  
- Well-tested with unit & integration tests  

## 🚀 Quick Start

```bash
# 1. Clone the repository
git clone <your-repository-url>
cd ExchangeService

# 2. Build the project
./mvnw clean install          # Linux / macOS
# or
mvnw.cmd clean install        # Windows

# 3. Run the application
./mvnw spring-boot:run        # Linux / macOS
# or
mvnw.cmd spring-boot:run      # Windows

# Alternative: run the packaged JAR
java -jar target/Financial-0.0.1-SNAPSHOT.jar
```

The service will be available at:  
**http://localhost:8080**

## 📡 API Endpoint

### Get exchange rates

```http
GET /api/finance/data/{resource}
```

| Parameter   | Description                        | Example    |
|-------------|------------------------------------|------------|
| `resource`  | Service that want to be called                | `supported_currencies`, `historical_idr_usd`. `latest_idr_rates` |

**Example response (200 OK)**

```json
[
  {
    "ILS": "Israeli New Sheqel",
    "CAD": "Canadian Dollar",
    "BRL": "Brazilian Real",
    "IDR": "Indonesian Rupiah",
    "THB": "Thai Baht",
    "MYR": "Malaysian Ringgit",
    "GBP": "British Pound",
    "RON": "Romanian Leu",
    "CHF": "Swiss Franc",
    "USD": "United States Dollar",
    "ZAR": "South African Rand",
    "EUR": "Euro",
    "NOK": "Norwegian Krone",
    "ISK": "Icelandic Króna",
    "SEK": "Swedish Krona",
    "HUF": "Hungarian Forint",
    "TRY": "Turkish Lira",
    "SGD": "Singapore Dollar",
    "DKK": "Danish Krone",
    "AUD": "Australian Dollar",
    "HKD": "Hong Kong Dollar",
    "JPY": "Japanese Yen",
    "KRW": "South Korean Won",
    "MXN": "Mexican Peso",
    "NZD": "New Zealand Dollar",
    "CZK": "Czech Koruna",
    "PHP": "Philippine Peso",
    "PLN": "Polish Złoty",
    "CNY": "Chinese Renminbi Yuan",
    "INR": "Indian Rupee"
  }
]
```

**Error (404 Not Found)**

```json
{
  "error": "Resource 'ABC' not found or not initialized."
}
```

## 🧪 Run Tests

```bash
./mvnw test             # Linux / macOS
# or
mvnw.cmd test           # Windows
```

## ⚙️ Configuration

Key settings live in `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: Exchange Service

app:
  github:
    username: cory-work-tech
  api:
    base-url: https://api.frankfurter.app/
```

## 📂 Project Layout

```
ExchangeService
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.financial
│   │   │       ├── controller      REST endpoints
│   │   │       ├── service         Business logic & storage
│   │   │       ├── configuration   Spring @Configuration
│   │   │       ├── strategy        Pluggable fetch strategies
│   │   │       └── runner          Startup data loader
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java                    Tests
├── mvnw          │  Maven wrapper
├── pom.xml       │  Dependencies & build
└── README.md
```

## 🏛 Architectural Rationale

### 1. Polymorphism & Strategy Pattern for Data Fetching

Instead of using conditional blocks (`if-else` / `switch`) in the service layer to handle different data resources, the **Strategy Pattern** (`FinancialDataStrategy` interface + concrete implementations) was deliberately chosen.

**Justification & Benefits:**

- **Extensibility**  
  Adding a new financial data source requires only creating a new class that implements `FinancialDataStrategy`. No modification needed in `StartupDataRunner` or other existing strategies → **Open/Closed Principle** satisfied.

- **Maintainability**  
  Each strategy is self-contained → encapsulates logic for one resource only. Improves readability, simplifies debugging, and allows isolated unit testing. A growing switch/if-else block would become large, hard to read and error-prone.

- **Separation of Concerns**  
  High-level algorithm (iterating strategies + storing results in `StartupDataRunner`) is completely decoupled from the specific fetch/transform logic of each resource.

### 2. Client Factory: Why `FactoryBean<RestTemplate>` over plain `@Bean`?

A `FactoryBean<RestTemplate>` was implemented instead of a simple `@Bean` method to encapsulate complex client initialization.

**Detailed Reasons:**

- **Custom Initialization Control**  
  `FactoryBean` allows centralized construction, configuration, and normalization of the `RestTemplate` in a dedicated class — far beyond what a simple bean method provides.

- **Encapsulation of Complexity**  
  Handles externalized API base URL (from `application.yml`) and applies `UriTemplateHandler` so **all strategies** automatically benefit from a pre-configured root path.

- **Separation of Concerns**  
  Decouples the *definition* of the `RestTemplate` from the network/configuration logic (timeouts, base paths, interceptors…). Keeps `@Configuration` classes much cleaner.

- **Proxying & Lifecycle Hook**  
  `FactoryBean` integrates deeply into Spring’s bean creation lifecycle — ideal when construction involves significant setup logic that would otherwise clutter configuration classes.

### 3. Startup Runner: Why `ApplicationRunner` over `@PostConstruct`?

`ApplicationRunner` was chosen (via `StartupDataRunner`) for initial data ingestion instead of using `@PostConstruct` methods.

**Complete Justification:**

- **Context Full Readiness**  
  `@PostConstruct` runs during bean initialization — often too early. The full `ApplicationContext` (proxies, AOP, logging, networking components…) may not be completely ready yet.

- **Access to ApplicationArguments**  
  `ApplicationRunner` receives command-line arguments → enables future flexibility (e.g. selective loading via flags).

- **Failure Resilience**  
  If the external Frankfurter API is unavailable, an exception in `@PostConstruct` can **crash the entire application startup**.  
  `ApplicationRunner` executes later → allows graceful error handling so the JVM & application can finish starting even when initial data load fails.

- **Service Availability Guarantee**  
  Runs **only after** all beans are created and the application is ready to accept traffic → guarantees `InMemoryDataStoreService` (and all dependencies) are fully available & properly injected when fetching begins.  

## 🛠 Tech Stack

- Java 21  
- Spring Boot 3  
- Maven  
- RestTemplate + FactoryBean  
- In-memory store (concurrent-safe Map)  
- JUnit 5 + Spring Boot Test  

## ✍️ Personalization Note

Created by **Cory** (`cory-work-tech`), with Spread Factor is 0.00406 from 1406

