# AlloBank Finance API

AlloBank Finance API is a Spring Boot application that retrieves currency exchange data from the Frankfurter public API and exposes it through a single polymorphic REST endpoint. The application loads all required data once at startup and serves it from an immutable in-memory store.

Technologies:
- Java 17+
- Spring Boot
- Maven
- RestTemplate
- Jackson
- Strategy Pattern
- In-memory storage (thread-safe & immutable)

External API:
Base URL: https://api.frankfurter.app  
Endpoints used:
- /latest?base=IDR  
- /2024-01-01..2024-01-05?from=IDR&to=USD  
- /currencies  

Configuration (application.properties):
server.port=8080  
server.servlet.context-path=/TesAlloBank  
frankfurter.base-url=https://api.frankfurter.app  
github.username=Fahmiir  

Setup & Run Instructions:
1. Clone the repository  
   git clone <your-repository-url>  
   cd <your-project-folder>  

2. Build the project  
   mvn clean package  

3. Run the application  
   mvn spring-boot:run  
   or run AlloBankApplication from your IDE.

Base URL:
http://localhost:8080/TesAlloBank/api/finance/data

Endpoint Usage (cURL Examples):

1. Latest IDR Rates  
curl -X GET http://localhost:8080/TesAlloBank/api/finance/data/latest_idr_rates

2. Historical IDR to USD Rates  
curl -X GET http://localhost:8080/TesAlloBank/api/finance/data/historical_idr_usd

3. Supported Currencies  
curl -X GET http://localhost:8080/TesAlloBank/api/finance/data/currencies

Project Structure:
com.example.AlloBank  
├── client        (Frankfurter API client)  
├── config        (RestTemplate & properties configuration)  
├── controller    (REST controller)  
├── strategy      (Strategy pattern implementations)  
├── store         (In-memory immutable data store)  
├── loader        (Startup data loader)  
├── response      (External API response mapping)  
├── dto           (DTOs for API responses)  
└── exception     (Global exception handling)  

Data Flow:
1. Application starts  
2. StartupDataLoader fetches latest rates, historical rates, and supported currencies  
3. Data is stored in FinanceStore (immutable and thread-safe)  
4. Controller routes requests to the correct Strategy  
5. Strategy transforms data into DTOs  
6. Data is returned as JSON response  

Error Handling:
Errors from the Frankfurter API are wrapped in ExternalServiceException and handled globally by GlobalExceptionHandler. Error responses are returned in JSON format.

Personalization Note:
GitHub Username: Fahmiir  

Sum of Unicode values = 736  
Spread Factor = (736 % 1000) / 100000.0  
Spread Factor = 0.00736  

The spread factor is applied using the formula:  
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)

Notes:
- Do not call Frankfurter API directly from Postman.  
- Use only the local application endpoints.  
- Historical data uses a fixed date range.  
- Data is stored in memory (no database).

Author:
Fahmi Irmansyah
