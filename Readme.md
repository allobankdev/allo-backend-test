# Set-Up Guide - IDR Rate Aggregator

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## Installation & Running the Application

### 1. Clone the Repository

```bash
git clone https://github.com/boysimbolon/allo-backend-test.git
cd allo-backend-test/idr-rate-aggregator
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/idr-rate-aggregator-1.0.0.jar
```

The application will start on **port 8080** by default.

## API Endpoints

### Base URL
```
http://localhost:8080/api/finance/data
```

### Available Endpoints

#### 1. Get Latest IDR Rates with USD Buy Spread
```
GET http://localhost:8080/api/finance/data/latest_idr_rates
```

**Response Example:**
```json
{
  "base": "IDR",
  "date": "2026-01-22",
  "rates": {
    "USD": 0.000059,
    "EUR": 0.000054,
    ...
  },
  "USD_BuySpread_IDR": 17003.56,
  "github_username": "boysimbolon"
}
```

#### 2. Get Historical IDR to USD Rates
```
GET http://localhost:8080/api/finance/data/historical_idr_usd
```

**Response Example:**
```json
{
  "base": "IDR",
  "start_date": "2024-01-01",
  "end_date": "2024-01-05",
  "rates": {
    "2024-01-01": {"USD": 0.000064},
    "2024-01-02": {"USD": 0.000065},
    ...
  }
}
```

#### 3. Get Supported Currencies
```
GET http://localhost:8080/api/finance/data/supported_currencies
```

**Response Example:**
```json
[
  "AUD",
  "BGN",
  "BRL",
  "CAD",
  "CHF",
  "CNY",
  "EUR",
  "GBP",
  "IDR",
  "USD",
  ...
]
```

## Configuration

You can modify the configuration in `src/main/resources/application.yml`:

```yaml
app:
  github-username: boysimbolon  # Change to your GitHub username

frankfurter:
  api:
    base-url: https://api.frankfurter.app
    historical:
      start-date: "2024-01-01"
      end-date: "2024-01-05"
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=FinanceDataControllerIntegrationTest
```

## Test Coverage

The application includes comprehensive tests:
- Unit tests for SpreadCalculator
- Unit tests for all Strategy classes
- Integration tests for FinanceDataController

All 27 tests passing ✅

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, you can change it in `application.yml`:

```yaml
server:
  port: 8081
```

### Maven Build Issues
If you encounter build issues, try:

```bash
mvn clean
mvn clean install -U
```

## Technology Stack

- **Spring Boot 4.0.1** - Application framework
- **Spring WebFlux** - Reactive HTTP client
- **Lombok** - Code generation
- **JUnit 5** - Testing framework
- **MockWebServer** - Mock HTTP server for testing

## Data Source

The application fetches exchange rate data from [Frankfurter API](https://www.frankfurter.app/) - a free and open-source currency data API.

## Features

- ✅ Real-time exchange rate fetching from Frankfurter API
- ✅ Personalized spread calculation based on GitHub username
- ✅ Historical exchange rate data
- ✅ Caching mechanism for improved performance
- ✅ Strategy pattern for clean architecture
- ✅ Comprehensive unit and integration tests
- ✅ RESTful API design
