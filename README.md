== OVERVIEW ==
This application integrates with the Frankfurter API to provide:
1. Latest IDR exchange rates (with custom spread calculation)
2. Historical IDR to USD data
3. Supported currency list
All data is fetched at startup and served via a REST API.

== Setup & Run Instructions ==
1. Clone Repository
git clone <your-repo-url>
cd api_finance_data

2. Build Project
mvn clean install

3. Run Application
mvn spring-boot:run

Application will start at:
http://localhost:8080

4. Run Tests
mvn test

== Endpoint Usage ==
1. Get Latest IDR Rates
curl http://localhost:8080/api/finance/data/latest_idr_rates

2. Get Historical IDR → USD
curl http://localhost:8080/api/finance/data/historical_idr_usd

3. Get Supported Currencies
curl http://localhost:8080/api/finance/data/supported_currencies

== Personalization Note ==
GitHub Username: nisaulchaira14

Spread Factor Calculation
1. Convert username to lowercase
2. Sum ASCII values
3. Apply formula:
Spread Factor = (sum % 1000) / 100000.0

Result:
Spread Factor = 0.00369

For latest_idr_rates, a custom field is added:
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)

== Architectural Rationale ==
1. Polymorphism Justification (Strategy Pattern)

The Strategy Pattern is used to handle multiple resource types (latest, historical, currencies) instead of using conditional logic (if-else or switch-case).
Benefits:
- Extensibility: New resource types can be added by simply implementing a new IDRDataFetcher without modifying existing code.
- Maintainability: Each fetcher encapsulates its own logic, making the code easier to read and debug.
- Single Responsibility: Each class handles only one type of data retrieval.

2. Client Factory (FactoryBean)
A custom FactoryBean<RestTemplate> is used to construct the external API client.
Purpose:
Centralize HTTP client configuration (timeouts, headers)
Ensure consistent client creation across the application

3. Startup Runner Choice (ApplicationRunner)
ApplicationRunner is used to load all external data at application startup.
Benefits of ApplicationRunner:
- Executes after Spring context is fully ready
- Ensures all beans are available
- More reliable for external API calls
- Better suited for production initialization tasks

Thank you for reviewing this submission :)