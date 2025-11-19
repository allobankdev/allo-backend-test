IDR Rate Aggregator Service

***Personalization Note (Spread Factor)***
GitHub Username Used: mendochandra
Total Sum of Unicode Values: 1252
Spread Factor Derivation: (1252 % 1000) / 100000.0
Exact Spread Factor: 0.00252

Calculation Illustration (Example)
If the API returns a USD Rate of 0.000060, calculation will yield:
USD Buy Spread IDR = 1/0.000060 * (1 + 0.00252) 
USD Buy Spread IDR = 16708.64$$

***Setup and Run Instructions***
You will need Java 17+ and Apache Maven to run this application.

1. Build Application
Open the terminal in the project's root directory (where pom.xml is located) and run:
# Clean and package the JAR file
./mvnw clean package

2. Run Application
The application will automatically execute the ApplicationRunner to fetch and cache the necessary data during startup.
# (default port: 8080)
./ mvn spring-boot:run

3. Run Tests
This command will execute all your Unit Tests (using MockWebServer) and Integration Tests.
# Run all tests to verify business logic and error handling
./mvnw test


***Endpoint Usage***
The application exposes a single, polymorphic main endpoint, meaning its behavior changes based on the resourceType parameter.

latest_idr_rates = curl -i http://localhost:8080/api/finance/data/latest_idr_rates

historical_idr_usd = curl -i http://localhost:8080/api/finance/data/historical_idr_usd

supported_currencies = curl -i http://localhost:8080/api/finance/data/supported_currencies