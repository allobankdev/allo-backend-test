
# IDR Currency Services

This project provides three reactive services for fetching IDR-related currency data using **Spring WebFlux**, structured with clean architecture and reusable components.

## 🚀 Features

### 1. Latest IDR Rates  
Service to retrieve the latest currency rates with IDR as the base.  
Includes additional computation for `usdBuySpreadIdr` based on GitHub username spread factor.

### 2. Historical IDR → USD Rate  
Fetches historical exchange rate for a specified date.

### 3. Supported Currencies  
Retrieves a map of supported currencies.

---

## 📦 Project Structure

```
/config
    /properties
        ClientProperties
    FrankFurterClientBean
    
/controller
    /base
        GlobalExceptionHandler
        BaseController
    FinanceController
    
/dto
    /base
        BaseResponse
        BaseCurrencyResponse
    /response
        GetHistoricalResponse
        GetLatestIDRResponse
        
/enums
    Commons
    RESPONSE

/exceptions
    BusinessException
    ExternalException

/runner
    StartupDataLoader
/service
    GetHistoricalService
    GetLatestIDRService
    GetSupportedCurrencyService
    IDRDataFetcher
    InMemoryDataStore

AllobankApplication
```

---

## ⚙️ How It Works

Each service implements:

```
public interface IDRDataFetcher<T> {
    Mono<T> fetch();
}
```

The controller calls services by name using:

```
private final Map<String, IDRDataFetcher<?>> dataFetchers;
```

No `if/else` required — clean, extensible, and TYPE-SAFE.

---

## 🧪 Testing

Test coverage includes:

- WebClient mocking using `WebClientMock` / `WebTestClient`
- Service logic (spread computation, error mapping)
- Controller contract response using `createSuccessResponse`
- Startup initialization validation

Example test included in `/test/`.

---

## ▶️ API Endpoints

### Get Latest IDR Rates
```http
GET /api/finance/data/latest_idr_rates
```

### Get Historical IDR → USD Rate
```http
GET /api/finance/data/historical_idr_usd
```

### Get Supported Currencies
```http
GET /api/finance/data/supported_currencies
```

---

## 🛠️ Requirements
- Java 21+
- Spring Boot 3+
- WebFlux
- Lombok
- Actuator

---

## 📝 Response Format

Your shared method is used for standard responses:

```
{
  "status": "success",
  "code": "00",
  "message": "Success",
  "data": { ... }
}
```

---

## 📘 Notes

- All services are reactive (Mono-based).
- Services are fully isolated and easily extendable.
- Avoids unnecessary branching in controller.

---

## 🛠️ Architectural Rationale

### 1. Why Strategy Pattern?
Using Strategy Pattern avoids ```if/switch``` blocks and cleanly isolates the logic for each resource type.
This ensures:
- High extensibility (adding new resource = new strategy class)
- High maintainability (strategies are self-contained)
- Single Responsibility Principle compliance

Controller remains extremely clean and polymorphic.

### 2. Why FactoryBean for WebClient?
FactoryBean provides:
- Custom lifecycle control
- Ability to centralize WebClient construction logic
- Cleaner separation between configuration and usage
- Eliminates manual bean definitions
- Required by assignment to enforce architectural discipline

It allows preconfigured headers, base URL loading via ```@ConfigurationProperties```, and timeouts.

### 3. Why ApplicationRunner Instead of @PostConstruct?
Reasons:
- ``ApplicationRunner`` runs after the entire context is ready
- Ensures all strategies, WebClient, and properties are fully initialized
- Allows blocking startup until all external data is loaded
- ```@PostConstruct``` is discouraged for blocking operations (can cause deadlocks)

This approach ensures the application won't start unless all required data has been successfully fetched.
