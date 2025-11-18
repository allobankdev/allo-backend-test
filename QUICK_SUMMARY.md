# Quick Implementation Summary

## What I'll Build

### Single API Endpoint
```
GET /api/finance/data/{resourceType}
```
Where `resourceType` can be:
- `latest_idr_rates` → Latest IDR exchange rates + calculated USD buy spread
- `historical_idr_usd` → Historical IDR to USD rates (Jan 1-5, 2024)
- `supported_currencies` → List of all supported currencies

---

## Your Personalization
- **GitHub Username:** RadityaDito
- **Spread Factor:** 0.00182
- **Applied to:** USD_BuySpread_IDR calculation in latest rates

---

## Technology Choices
- **HTTP Client:** WebClient (modern, reactive)
- **Lombok:** Yes (cleaner code)
- **Testing:** JUnit 5 + Mockito + MockWebServer

---

## Architecture (Simplified)

### 1. Strategy Pattern (3 Strategies)
```
DataFetcherStrategy (interface)
├── LatestIDRRatesStrategy      → Fetches latest rates + calculates spread
├── HistoricalIDRUSDStrategy    → Fetches historical data
└── SupportedCurrenciesStrategy → Fetches currency list
```

**Why?** Controller automatically picks the right strategy based on resourceType (no if/else needed)

### 2. WebClient via FactoryBean
```
WebClientFactoryBean → Creates WebClient with config from application.yml
```

**Why?** Cleaner separation, externalizes API URL, demonstrates advanced Spring knowledge

### 3. Startup Data Loading
```
ApplicationRunner → Runs on startup
                  → Fetches all 3 resources ONCE
                  → Stores in memory (thread-safe, immutable)
                  → API serves from memory (not real-time API calls)
```

**Why?** Faster responses, no repeated API calls, data is immutable after startup

---

## Project Structure

```
src/main/java/com/allo/backend/test/code/
├── config/
│   └── WebClientFactoryBean.java        # Creates WebClient
├── controller/
│   └── FinanceDataController.java       # REST endpoint
├── service/
│   ├── DataStorageService.java          # In-memory store (thread-safe)
│   ├── startup/
│   │   └── DataInitializationRunner.java # Loads data on startup
│   └── strategy/
│       ├── DataFetcherStrategy.java     # Interface
│       ├── LatestIDRRatesStrategy.java
│       ├── HistoricalIDRUSDStrategy.java
│       └── SupportedCurrenciesStrategy.java
├── model/
│   ├── dto/        # API response models
│   └── domain/     # Internal data models
└── util/
    └── SpreadFactorCalculator.java      # Calculates your spread factor
```

---

## What Gets Tested

### Unit Tests (with mocks)
- ✅ Spread factor calculator
- ✅ Each strategy (all 3)
- ✅ Error handling in strategies

### Integration Tests (real Spring context)
- ✅ ApplicationRunner loads data on startup
- ✅ Controller endpoint returns correct data for each resourceType
- ✅ Invalid resourceType returns 400 error

---

## Implementation Order

### Phase 1: Setup (Steps 1-3)
- Update `build.gradle` with dependencies
- Create `application.yml` with API configuration

### Phase 2: Core Components (Steps 4-7)
- Spread factor calculator
- API response models (DTOs)
- WebClientFactoryBean

### Phase 3: Strategy Pattern (Steps 8-12)
- Strategy interface
- 3 strategy implementations
- Strategy configuration

### Phase 4: Storage & Startup (Steps 13-14)
- Thread-safe in-memory storage
- ApplicationRunner to load data

### Phase 5: API Layer (Steps 15-16)
- Controller with endpoint
- Exception handler

### Phase 6: Testing (Steps 17-20)
- All unit tests
- All integration tests

### Phase 7: Documentation (Steps 21-23)
- Complete README
- Architectural rationale
- cURL examples

---

## Key Features

✅ **Strategy Pattern** - Clean, extensible design for multi-resource handling
✅ **FactoryBean** - Professional WebClient creation
✅ **ApplicationRunner** - Data loaded once on startup
✅ **Thread-Safe Storage** - Immutable after initialization
✅ **Spread Calculation** - Personalized with your GitHub username
✅ **Comprehensive Tests** - Unit + Integration coverage
✅ **Clean Code** - Lombok, proper error handling, separation of concerns

---

## Example Usage (After Implementation)

```bash
# Get latest IDR rates with calculated USD buy spread
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Get historical IDR to USD data
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Get list of supported currencies
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## Ready to Start?

If this looks good, I'll start coding through all phases in sequence. Each phase builds on the previous one, ending with a fully tested, production-ready application.

**Estimated Implementation: ~20 commits, full test coverage, complete documentation**
