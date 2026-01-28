Tech Stack
Java 17
Spring Boot
Spring WebFlux (WebClient)
JUnit 5 + Mockito
Maven


Setup & Run Instructions
git clone "url"
mvn clean install
App will start at: http://localhost:8080

Available Endpoints
http://localhost:8080/api/finance/data/latest_idr_rates
http://localhost:8080/api/finance/data/latest_idr_rates
http://localhost:8080/api/finance/data/supported_currencies

Architectural Overview
Why Strategy Pattern?
Avoids large conditional blocks
Easier to extend (add new resource = new class)
Improves testability
Follows Open/Closed Principle

FrankfurterClientFactory creates a configured WebClient
Benefits:
Centralized configuration
Reusable client instance
Clean separation of infrastructure vs business logic

ApplicationRunner executes after the Spring application context is fully initialized.
Benefits:
Safe to call external APIs
Allows one-time data loading during startup
Suitable for cache or in-memory store initialization
Clearly aligned with the application lifecycle
More production-ready approach