# Exchange Rate Aggregator

A simple Spring Boot application that fetches exchange rates from the Frankfurter API and provides latest rates, historical rates, and supported currencies.

---

## Setup / Run Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- Internet connection (for external API calls)

### Steps
- Build the project -> mvn clean install
- Run the application -> mvn spring-boot:run

The application will automatically fetch data on startup using CommandLineRunner.

---
## Endpoint Usage

Although the application runs automatically, here are example cURL commands if you want to test via REST endpoints:

### a. Get Latest IDR Rate
curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates"

### b. Get Historical Rates
curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd"

### c. Get Supported Currencies
curl -X GET "http://localhost:8080/api/finance/data/supported_currencies"

---
## Personalization Note

- GitHub Username: aderadia

- Spread Factor: 0.00765

The spread factor is calculated by summing ASCII values of your GitHub username, applying the formula (sumChar % 1000) / 100000.

### Notes

- All external API calls are handled via FrankfurterClient with error handler.

- JSON outputs are formatted with pretty print for readability.