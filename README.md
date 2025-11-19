# 💱 IDR Currency Services (Reactive WebFlux)

A Spring WebFlux–based service that provides IDR currency information using a clean, extensible, and testable architecture.  
This project implements Strategy Pattern, centralized WebClient factory, and startup data preloading.

---

---

# 📌 Requirements
- Java 21+
- Spring Boot 3.x (WebFlux)
- Maven 3.9+
- Lombok enabled
- Internet connection (to call external API)

---

# 🚀 Setup & Run Instructions

## 1️⃣ Clone Repository
```
git clone https://github.com/<your_repo>.git
cd <your_repo>
```

## 2️⃣ Build the Application
```
mvn clean install
```

## 3️⃣ Run the Application
```
mvn spring-boot:run
```

---

# 🧪 Running All Tests

```
mvn test
mvn clean test
```

---

# 📘 API Endpoints

| Endpoint | Description |
|---------|-------------|
| **GET /api/finance/data/latest_idr_rates** | Latest IDR currency rates with spread factor |
| **GET /api/finance/data/historical_idr_usd** | Historical rate IDR → USD (fixed date: 2024-01-05) |
| **GET /api/finance/data/supported_currencies** | List of supported currency codes |

---

# 🖥️ Example cURL Commands

### Latest IDR Rates
```
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

### Historical IDR → USD
```
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

### Supported Currencies
```
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

---

# 🧠 Personalization Note (GitHub Username + Spread Factor)

Spread factor formula:

```
spreadFactor = (sum(charCodes(username)) % 1000) / 100000.0
```

Example (replace with your GitHub username):

```
Username   : ravialdi
Char Sum   : 844
Spread     : 0.00844
```

This spread is applied to derive:

```
usdBuySpreadIdr = (1 / usdRate) * (1 + spreadFactor)
```

---

# 🧱 Architecture Overview

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


Core design patterns:
- Strategy Pattern for data fetchers
- WebClient FactoryBean for centralized config
- ApplicationRunner for startup data loading
- Reactive non-blocking architecture (Mono)

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



# 🧪 Testing Notes

Coverage includes:

- WebClient mocks
- Error mapping (4xx, 5xx)
- Spread factor calculation
- StartupRunner integration test
- Controller contract responses

Run all tests using:

```
mvn test
```

---

# 📄 License
For Allobank Technical Test.