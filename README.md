# Allo Bank Backend Developer Take-Home Test - `tech-enthusiast-168`

This project is the implemention of the Spring Boot REST API for the Allo Bank backend developer test.

## Key Architecture & Features
- **Java 21** & **Spring Boot 3.2.5** (WebFlux)
- **Constraint A**: Implements `DataFetcherStrategy` to fetch all 3 endpoints seamlessly via Map Injection in `FinanceController`.
- **Constraint B**: Custom `FrankfurterClientFactoryBean` creates and configures `WebClient` instances without standard `@Bean`.
- **Constraint C**: Uses `InitialDataRunner` (an `ApplicationRunner`) executing fetch routines completely once at startup. Information is saved into an immutable and Thread-Safe ConcurrentHashMap managed by `DataStoreService`.
- **Spread Factor Calculation**: Custom formula computing spreading based on ASCII values of `tech-enthusiast-168`.

## Spread Factor Calculation Result
- **Input:** `tech-enthusiast-168`
- **Calculation:** Sum of ascii values is `1765`. `1765 % 1000 = 765`. `765 / 100000.0`
- **Spread Factor Derived:** `0.00765`

## Setup & Running
Ensure you have Maven installed. Run the command below in the project root:
```bash
mvn clean install
mvn spring-boot:run
```

## Endpoints Usage
The server starts at `http://localhost:8080`.

1. **Latest IDR Rates (With Custom USD_BuySpread_IDR)**
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```
2. **Historical USD to IDR Data**
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```
3. **Supported Currencies list**
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Architectural Rationale
1. **Polymorphism Justification**: 
   The Strategy Pattern eliminates procedural `if/else` or `switch` cases. Spring's auto configuration allows `Map<String, DataFetcherStrategy>` injection so maintaining, adding, and scaling API resources occurs seamlessly without constantly editing the single centralized service or controller.
2. **Client Factory**:
   Using `FactoryBean<WebClient>` allows fine-grained, externalized API configuration like dynamic timeout injections pulled from the configuration environment at runtime cleanly, rather than littering the main configuration context file.
3. **Startup Runner Choice**:
   `ApplicationRunner` is beneficial over `@PostConstruct` because it guarantees execution only occurs *after* the entire entire Spring Application context has been fully warmed up and started. This limits circular dependencies and is perfectly suited for external API fetching compared to basic bean lifecycle setups.
