## README

## 📝 Personalization

- **GitHub Username:** hollymolly2708
- **Sum of Unicode Values:** 1318
- **Spread Factor Formula:** (1318 % 1000) / 100000
- **Final Spread Factor:** 0.00318

## 🚀 Run Instructions

```bash
git clone https://github.com/hollymolly2708/allo-backend-test
cd allo-backend-test
mvn clean install
mvn spring-boot:run
```

## 🔎 Endpoint Usage
```curl
curl http://localhost:8080/api/finance/data/latest_idr_rates

curl http://localhost:8080/api/finance/data/historical_idr_usd

curl http://localhost:8080/api/finance/data/supported_currencies
```
## 🧠 Architectural Rationale

1️⃣ Why Strategy Pattern?

The Strategy Pattern is used to eliminate conditional branching logic (e.g., if-else or switch) from the controller layer and to comply with the Open/Closed Principle.

Each resource type (e.g., latest, historical, supported_currencies) is encapsulated in its own strategy implementation. This provides:

- Extensibility — New resource types can be added by introducing a new strategy implementation without modifying existing controller logic.

- Separation of Concerns — The controller delegates processing logic to the appropriate strategy.

- Maintainability — Business logic is isolated and easier to test independently.

This design prevents the controller from becoming tightly coupled to specific resource-handling logic.

2️⃣ Why FactoryBean?

FactoryBean is used to encapsulate the construction and configuration of the WebClient.

Instead of defining a simple @Bean, FactoryBean provides:

- Encapsulation of creation logic — The construction details are hidden from consumers.

- Centralized configuration management — Base URL, headers, and other settings are managed in a single location.

- Improved testability — The client configuration can be replaced or mocked without impacting business logic.

- Clear separation between infrastructure configuration and application logic.

This approach improves abstraction and keeps configuration concerns separate from strategy implementations.

3️⃣ Why ApplicationRunner?

ApplicationRunner is used to preload external data during the application startup lifecycle.

Compared to @PostConstruct, it provides:

- Guaranteed execution after full application context initialization

- Access to application arguments if needed

- Better alignment with Spring Boot lifecycle events

By fetching external data at startup:

- External API calls are performed only once

- Runtime latency for first request is reduced

- Application readiness is predictable

This ensures consistent initialization behavior while maintaining lifecycle clarity.