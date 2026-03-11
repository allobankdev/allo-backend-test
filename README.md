# IDR Rate Aggregator API

A Spring Boot REST API that aggregates Indonesian Rupiah (IDR) exchange-rate data from the public [Frankfurter API](https://api.frankfurter.app) and exposes it through a single polymorphic endpoint.

---

## Setup & Run Instructions

### Prerequisites

| Tool | Minimum Version |
|---|---|
| Java JDK | 17 |
| Maven | 3.9+ |
| VS Code | Latest (with Extension Pack for Java) |

### Clone & Build

```bash
git clone https://github.com/MRafi68/allo-backend-test.git
cd allo-backend-test
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or press **F5** in VS Code with the Extension Pack for Java installed.

The server starts on **port 8081** by default.

### Run Tests

```bash
# All tests
mvn test

# Unit tests only
mvn test -Dtest="*FetcherTest"

# Integration test only
mvn test -Dtest="DataLoaderRunnerIntegrationTest"
```

---

## Endpoint Usage

### Base URL
```
http://localhost:8081
```

### `GET /api/finance/data/{resourceType}`

| `resourceType` | Description |
|---|---|
| `latest_idr_rates` | Latest IDR exchange rates + USD_BuySpread_IDR |
| `historical_idr_usd` | IDR → USD time-series (2024-01-01 to 2024-01-05) |
| `supported_currencies` | Full list of supported currency codes and names |

### Example cURL Commands

**1. Latest IDR Rates**
```bash
curl -X GET http://localhost:8081/api/finance/data/latest_idr_rates \
  -H "Accept: application/json" | python -m json.tool
```

Expected response shape:
```json
{
  "resourceType": "latest_idr_rates",
  "data": [
    {
      "base": "IDR",
      "date": "2025-02-26",
      "rates": { "AUD": 0.000099, "USD": 0.000062, "..." : "..." },
      "USD_BuySpread_IDR": 16137.84210000,
      "spreadFactor": 0.00637
    }
  ]
}
```

**2. Historical IDR → USD**
```bash
curl -X GET http://localhost:8081/api/finance/data/historical_idr_usd \
  -H "Accept: application/json" | python -m json.tool
```

Expected response shape:
```json
{
  "resourceType": "historical_idr_usd",
  "data": [
    { "date": "2024-01-02", "rates": { "USD": 0.000064 } },
    { "date": "2024-01-03", "rates": { "USD": 0.000064 } },
    { "date": "2024-01-04", "rates": { "USD": 0.000063 } },
    { "date": "2024-01-05", "rates": { "USD": 0.000063 } }
  ]
}
```

**3. Supported Currencies**
```bash
curl -X GET http://localhost:8081/api/finance/data/supported_currencies \
  -H "Accept: application/json" | python -m json.tool
```

Expected response shape:
```json
{
  "resourceType": "supported_currencies",
  "data": [
    { "code": "AUD", "name": "Australian Dollar" },
    { "code": "IDR", "name": "Indonesian Rupiah" },
    { "code": "USD", "name": "US Dollar" }
  ]
}
```

**Invalid resource type (400 Bad Request)**
```bash
curl -X GET http://localhost:8081/api/finance/data/invalid_type
# Returns: 400 Bad Request
```

---

## Personalization Note

**GitHub Username:** `MRafi68`

**Spread Factor Calculation:**

| Step | Value |
|---|---|
| Lowercase username | `mrafi68` |
| Unicode (ASCII) sum | `m(109) + r(114) + a(97) + f(102) + i(105) + 6(54) + 8(56) = 637` |
| Formula | `(637 % 1000) / 100000.0` |
| **Spread Factor** | **`0.00637`** |

**Final Formula:**
```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00637)
```

Example: if `Rate_USD = 0.000062` then:
```
USD_BuySpread_IDR = (1 / 0.000062) * 1.00637 = 16231.00
```

---

## Architectural Rationale

### i. Polymorphism Justification — Why Strategy Pattern?

The three resource types (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) each require distinct fetch logic, URL construction, and data transformation. A naive implementation would use a cascading `if/else` or `switch` block in the service or controller layer.

**The Strategy Pattern was chosen instead for two key reasons:**

**Extensibility (Open/Closed Principle):** Adding a fourth resource type requires only creating a new class that implements `IDRDataFetcher` — no existing code is modified. The controller automatically discovers it via Spring's dependency injection of all `IDRDataFetcher` beans into a `Map<String, IDRDataFetcher>`. This means the routing logic scales to N resources with zero code changes to the controller or service.

**Maintainability:** Each strategy is a self-contained, independently testable unit. A bug in the historical rates transformation cannot affect the currency list logic. Each class has a single reason to change. The `getResourceType()` method serves as a self-registering key, eliminating the need for any external mapping configuration.

---

### ii. Client Factory Bean — Why `FactoryBean<WebClient>` over `@Bean`?

Spring's `FactoryBean<T>` interface is a dedicated creational contract for complex object construction. It was chosen over a simple `@Bean` method in a `@Configuration` class for the following reasons:

A `@Bean` method in `@Configuration` is a general-purpose object provider with no lifecycle semantics beyond creation. A `FactoryBean` makes the construction intent explicit — the class *is* a factory, not a configuration class that happens to produce a bean among many others. This enforces **Single Responsibility**: `WebClientFactoryBean` has exactly one job.

`FactoryBean` also provides `getObjectType()` and `isSingleton()` as first-class methods, giving Spring richer metadata about the produced object for proxy generation and context management. Additionally, it encapsulates all WebClient configuration (base URL, headers, codec setup, timeouts) in one place, keeping `@Configuration` classes clean and free of infrastructure concerns.

---

### iii. Startup Runner Choice — Why `ApplicationRunner` over `@PostConstruct`?

Both `ApplicationRunner` and `@PostConstruct` run code after bean creation, but they differ in timing and context:

`@PostConstruct` runs during bean initialisation, **before** the full application context is ready. This means other beans that the data-loading logic depends on (such as the `WebClient` or all `IDRDataFetcher` strategies) may not yet be fully initialised, creating subtle ordering bugs.

`ApplicationRunner` runs **after the entire Spring context is fully refreshed and ready**, guaranteeing that every bean, configuration, and dependency is available. This is the correct hook for application-level startup tasks like pre-loading data.

Furthermore, `ApplicationRunner` receives `ApplicationArguments`, making it easy to add command-line-driven behaviour in the future (e.g., `--refresh-data` flag). `@PostConstruct` has no such extension point.

The `.block()` call in the runner is intentional and correct here: the application must not begin serving requests until the in-memory store is populated, and blocking at startup (not at request time) achieves this without any concurrency risk.

---