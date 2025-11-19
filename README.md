# IDR Exchange Rate Service

A Spring Boot microservice that fetches and serves Indonesian Rupiah (IDR) exchange rate data from the Frankfurter API. The service provides three different resource types through a single endpoint and includes personalized spread factor calculations.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Resilience & Fault Tolerance](#-resilience--fault-tolerance)
- [Documentation](#documentation)
- [Setup/Run Instructions](#setuprun-instructions)
- [Endpoint Usage](#endpoint-usage)
- [Personalization Note](#personalization-note)
- [🛠️ Architectural Rationale](#️-architectural-rationale)

## ✨ Features

- **Three Resource Types**: Latest rates with USD spread calculation, historical rates, and supported currencies
- **Strategy Pattern**: Clean separation of concerns for different data fetching strategies
- **Performance Optimized**: Data loaded once at startup and served from memory
- **Resilience4j Retry**: Automatic retries for transient failures with configurable backoff
- **Personalized**: Custom spread factor calculation based on GitHub username

## 🏗️ Architecture

The application follows a layered architecture with the following key components:

- **Controller Layer**: REST endpoints for data access
- **Service Layer**: Business logic and strategy delegation
- **Strategy Layer**: Individual data fetchers for different resource types
- **Client Layer**: WebClient-based HTTP communication
- **Data Store**: In-memory storage for API responses
- **Configuration**: Properties and factory beans for WebClient creation

## ⚡ Resilience & Fault Tolerance

### Retry Mechanism

The application uses **Resilience4j** to provide automatic retry capabilities for external API calls, ensuring robust communication with the Frankfurter API.

**Configuration** (`application.yaml`):
- **Max Attempts**: 3 retries
- **Wait Duration**: 2 seconds between attempts
- **Retry Exceptions**:
  - `WebClientResponseException` (4xx/5xx errors)
  - `WebClientRequestException` (network/timeout errors)
- **Ignored Exceptions**: `ClientException` (4xx client errors are not retried)

**How It Works:**

Each strategy implementation is annotated with `@Retry(name = "frankfurterApi")`:
- `LatestIdrRatesStrategy` - Retries failed API calls for latest rates
- `HistoricalIdrUsdStrategy` - Retries failed API calls for historical data
- `SupportedCurrenciesStrategy` - Retries failed API calls for currency list

**Benefits:**
- **Transient Failure Handling**: Automatically recovers from temporary network issues
- **Graceful Degradation**: Application can still start even if API is temporarily unavailable
- **Exponential Backoff**: 2-second wait between retries prevents overwhelming the API

**Example Scenario:**

If the Frankfurter API returns a 503 (Service Unavailable), the strategy will:
1. Wait 2 seconds
2. Retry the request (up to 2 more times)
3. If all retries fail, throw `ServerException`
4. Application continues running with cached data from previous startup (if available)

## 📖 Documentation

### Setup/Run Instructions

#### Prerequisites

- Java 21 or higher
- Gradle (or use the included gradlew wrapper)

#### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/frhn9/allo-backend-test.git
   ```

2. **Build the application**
   ```bash
   ./gradlew clean build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   Or run the JAR file:
   ```bash
   java -jar build/libs/allo-backend-test-0.0.1-SNAPSHOT.jar
   ```

4. **Run tests**
   ```bash
   ./gradlew test
   ```

#### Expected Output

The application will start and load data from the Frankfurter API during initialization:

```
=== Starting IDR exchange rate data loading at application startup ===
Loading latest_idr_rates...
Loading historical_idr_usd...
Loading supported_currencies...
=== IDR exchange rate data loading completed successfully ===
All resources loaded: latest_idr_rates, historical_idr_usd, supported_currencies
```

The service will be available at `http://localhost:8080/api` (context path).

### Endpoint Usage

The service provides a single endpoint for all three resource types:

**Base URL**: `http://localhost:8080/api/finance/data/{resourceType}`

#### 1. Latest IDR Rates (with USD spread calculation)

```bash
curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates" \
     -H "Accept: application/json"
```

**Sample Response**:
```json
{
  "responseSchema": {
    "responseCode": "success",
    "responseMessage": "Success"
  },
  "data": {
    "amount": 1.0,
    "base": "IDR",
    "date": "2025-11-19",
    "rates": {
      "AUD": 0.000092,
      "BGN": 0.0001,
      "BRL": 0.00032,
      "CAD": 0.000084,
      "CHF": 0.000048,
      "CNY": 0.00043,
      "CZK": 0.00125,
      "DKK": 0.00039,
      "EUR": 0.000052,
      "GBP": 0.000046,
      "HKD": 0.00047,
      "HUF": 0.01974,
      "ILS": 0.0002,
      "INR": 0.00529,
      "ISK": 0.00758,
      "JPY": 0.00934,
      "KRW": 0.08769,
      "MXN": 0.0011,
      "MYR": 0.00025,
      "NOK": 0.00061,
      "NZD": 0.00011,
      "PHP": 0.00352,
      "PLN": 0.00022,
      "RON": 0.00026,
      "SEK": 0.00057,
      "SGD": 0.000078,
      "THB": 0.00194,
      "TRY": 0.00253,
      "USD": 0.000060,
      "ZAR": 0.00103
    },
    "USD_BuySpread_IDR": 16747.83
  }
}
```

#### 2. Historical IDR to USD Rates

```bash
curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd" \
     -H "Accept: application/json"
```

**Sample Response** (Returns array of `HistoricalRateResponse` DTOs):
```json
{
  "responseSchema": {
    "responseCode": "success",
    "responseMessage": "Success"
  },
  "data": [
    {
      "date": "2024-01-01",
      "currency": "USD",
      "rate": 0.000064
    },
    {
      "date": "2024-01-02",
      "currency": "USD",
      "rate": 0.0000635
    },
    {
      "date": "2024-01-03",
      "currency": "USD",
      "rate": 0.0000638
    }
  ]
}
```

#### 3. Supported Currencies

```bash
curl -X GET "http://localhost:8080/api/finance/data/supported_currencies" \
     -H "Accept: application/json"
```

**Sample Response** (Returns array of `CurrencyResponse` DTOs):
```json
{
  "responseSchema": {
    "responseCode": "success",
    "responseMessage": "Success"
  },
  "data": [
    {
      "code": "AUD",
      "name": "Australian Dollar"
    },
    {
      "code": "CAD",
      "name": "Canadian Dollar"
    },
    {
      "code": "CHF",
      "name": "Swiss Franc"
    },
    {
      "code": "EUR",
      "name": "Euro"
    },
    {
      "code": "GBP",
      "name": "British Pound"
    },
    {
      "code": "USD",
      "name": "US Dollar"
    }
  ]
}
```

### Personalization Note

This application includes a personalized spread factor calculation based on the developer's GitHub username.

**GitHub Username**: `frhn9`

**Spread Factor Calculation**:
- Formula: `(Sum of Unicode Values % 1000) / 100000.0`
- For username "frhn9": 
  - Unicode values: f(102) + r(114) + h(104) + n(110) + 9(57) = 487
  - Calculation: `(487 % 1000) / 100000.0 = 0.00487`
- **Exact Spread Factor**: `0.00487`

The spread factor is applied to calculate the USD buy spread in IDR: `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)`

## 🛠️ Architectural Rationale

### 1. Polymorphism Justification: Strategy Pattern

**Why Strategy Pattern over conditional blocks?**

The Strategy Pattern was implemented in the service layer (`ExchangeRateServiceImpl`) to handle the multi-resource endpoint instead of using simple conditional blocks. This design choice provides several critical benefits:

**Extensibility Benefits:**
- **Open/Closed Principle**: New resource types can be added without modifying the existing service implementation
- **Automatic Discovery**: Spring automatically injects all `IDRDataFetcher` implementations, making it trivial to add new strategies
- **Type Safety**: The `ResourceType` enum ensures compile-time safety for resource routing

**Maintainability Benefits:**
- **Single Responsibility**: Each strategy handles exactly one resource type with its own data fetching and transformation logic
- **Testability**: Individual strategies can be tested in isolation without complex mocking scenarios
- **Clear Separation**: Business logic for different resources is cleanly separated, making the codebase easier to understand and modify
- **Reduced Complexity**: Eliminates large conditional blocks that would become increasingly complex as more resource types are added

**Real-world Impact:**
If we needed to add a new resource type (e.g., `INTRADAY_RATES`), we simply create a new implementation of `IDRDataFetcher` and Spring automatically integrates it. With conditional blocks, we'd need to modify the existing service method, increasing the risk of introducing bugs in existing functionality.

### 2. Client Factory: FactoryBean for WebClient

**Role and Benefits of FactoryBean:**

The `WebClientFactoryBean` is used to construct the external API client (`WebClient`) instead of a standard `@Bean` method for several important reasons:

**Configuration Complexity:**
- **Complex Setup**: WebClient requires multiple configurations (HTTP client timeouts, connection settings, exchange strategies) that would clutter a regular `@Bean` method
- **Parameter-driven**: The factory receives configuration from `FrankfurterApiProperties`, making it easier to manage and modify settings
- **Specialized Logic**: The factory encapsulates all the complex WebClient setup logic, keeping the configuration class clean

**Lifecycle Management:**
- **Fine-grained Control**: `FactoryBean` provides better control over the creation process with methods like `getObject()`, `getObjectType()`, and `isSingleton()`
- **Conditional Creation**: Allows for conditional logic in the WebClient creation if needed (e.g., different configurations for different environments)
- **Error Handling**: Centralized error handling during the complex setup process

**Preferability over Standard `@Bean`:**
- **Readability**: Moves complex construction logic out of configuration classes
- **Reusability**: The factory pattern makes it easier to create multiple instances with different configurations if needed
- **Testing**: Easier to mock and test the factory independently

### 3. Startup Runner Choice: ApplicationRunner

**Justification over @PostConstruct:**

The `StartupLoader` uses `ApplicationRunner` instead of `@PostConstruct` for initial data ingestion for these reasons:

**Execution Order Control:**
- **Application Context Ready**: `ApplicationRunner` runs after the Spring Boot application is fully initialized and all beans are ready
- **Dependency Injection**: Ensures all dependencies (`IDRDataFetcher` list, `DataStoreService`, `WebClient`) are properly injected before execution
- **Configuration Properties**: Guarantees that configuration properties are loaded and available

**Error Handling:**
- **Exception Propagation**: Better exception handling - if data loading fails, the application startup can be failed appropriately
- **Logging Integration**: Standardized logging through Spring Boot's logging system from the very beginning
- **Graceful Degradation**: Can implement sophisticated error handling and retry mechanisms

**Spring Boot Integration:**
- **Lifecycle Integration**: Seamlessly integrates with Spring Boot's application lifecycle
- **Resource Management**: Better resource management as the application context is fully prepared
- **Configuration Aware**: Can access all Spring Boot configuration and external configuration sources

**Practical Benefits:**
- **Startup Optimization**: Data is loaded exactly once when the application starts, not on every bean initialization
- **Memory Efficiency**: Ensures consistent loading order and prevents multiple simultaneous data fetch attempts
- **Monitoring**: Better integration with Spring Boot Actuator for startup monitoring and health checks

Using `@PostConstruct` would run during bean instantiation, potentially before all dependencies and configurations are ready, leading to race conditions or missing configurations.
