# Allo Bank Backend Test

## Setup & Run

### Prerequisites

- Java 17+
- Maven

### Installation

1. Clone the repository.
2. Update `src/main/resources/application.properties` with your GitHub username:

```properties
app.github.username=your_actual_username(mine is inganta23)
```

3. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

## API Usage

The API exposes a single endpoint to retrieve aggregated financial data.

**Base URL:** `http://localhost:8080/api/finance/data/{resourceType}`

| Resource Type          | Description                                  |
| ---------------------- | -------------------------------------------- |
| `latest_idr_rates`     | Latest exchange rates relative to IDR.       |
| `historical_idr_usd`   | Historical IDR to USD rates (Jan 1-5, 2024). |
| `supported_currencies` | List of all supported currency symbols.      |

### cURL Examples

**1. Get Latest IDR Rates**

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates

```

**2. Get Historical Data**

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd

```

**3. Get Supported Currencies**

```bash
curl http://localhost:8080/api/finance/data/supported_currencies

```

## Architectural Rationale

### 1. Polymorphism

**Why:** To avoid conditional logic (`if/else` or `switch`) in the Controller.  
**Implementation:** I defined an `IDRDataFetcher` interface. Each resource type has its own dedicated class implementing this interface. The application dynamically selects the correct strategy at runtime based on the requested resource string. This adheres to the Open/Closed Principle, adding a new data source only requires adding a new class, not modifying existing code.

### 2. Client Factory Bean

**Why:** To centralize the configuration of the external API client.  
**Implementation:** Instead of a simple `@Bean`, I used a `FactoryBean` to create the `RestTemplate`. This encapsulates the complexity of setting up the client in a single infrastructure class. It keeps the business logic clean and allows Spring to manage the client's lifecycle while giving full control over its construction.

### 3. Startup Runner & Immutability

**Why:** To improve API response time and ensure reliability.  
**Implementation:** An `ApplicationRunner` fetches all required data exactly once when the application starts. This data is stored in a thread-safe `ConcurrentHashMap`.

- **Performance:** API requests are served instantly from memory (O(1)), avoiding the latency of calling the external Frankfurter API on every user request.
- **Stability:** If the external API goes down after startup, the application continues to serve the cached data without error.

## Personalization Note

**GitHub Username:** inganta23

**Spread Factor Calculation:**
The spread factor is derived from the sum of the Unicode (ASCII) values of my username characters.

- **Formula:** `(Sum of Unicode % 1000) / 100000.0`
- **Calculation:**
- Sum of ASCII values for `inganta23`: `839`
- `( 839 % 1000 ) / 100000.0`

- **Final Spread Factor:** `0.00839`

**Result:**
For the `latest_idr_rates` endpoint, the `USD_BuySpread_IDR` is calculated as:
`USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00839)`
