# Frankfurter Finance Aggregation API

## Overview

This project is a Spring Boot REST API that aggregates financial data from the public **Frankfurter Exchange Rate API**.

The API exposes a **single polymorphic endpoint** capable of returning different financial resources depending on the requested `resourceType`.

The application demonstrates:
Clean Architecture
- Strategy Design Pattern
- FactoryBean usage
- Immutable in-memory data storage
- Startup data ingestion with `ApplicationRunner`
- Thread-safe design
- Integration with external APIs

External API used:
`https://api.frankfurter.app`

# Setup & Run Instructions

## 1. Clone the Repository

```bash
git clone https://github.com/Minyeng/allo-backend-test.git
cd allo-backend-test
```
## 2. Build the Project

The project uses Maven.
```bash
mvn clean install
```
This command will:
 - Compile the application
 - Run all unit tests
 - Build the application jar
## 3. Run the Application
```bash
mvn spring-boot:run
```
Or run the generated jar:
```bash
java -jar target/idr-rate-aggregator-0.0.1-SNAPSHOT.jar
```
The API will start on:
```bash
http://localhost:8080
```
## 4. Run Tests
Run all tests with:
```bash
mvn test
```
The test suite includes:
 - Unit tests for Strategy implementations
 - Business logic tests for Spread calculation
 - Integration tests verifying startup data loading
# API Endpoint Usage
The application exposes a single polymorphic endpoint:
```bash
GET /api/finance/data/{resourceType}
```
## 1. Latest IDR Rates

Fetch the latest exchange rates relative to Indonesian Rupiah.
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```
Example response (simplified):
```json
{
  "timestamp": "2026-03-11T08:17:14.1535863",
  "status": 200,
  "message": "Success",
  "data": {
    "base": "IDR",
    "date": "2026-03-09",
    "rates": {
      "USD": 0.000059,
      "AUD": 0.000084,
      "JPY": 0.00936,
    },
    "usdBuySpreadIdr": 17077.7966101695
  }
}
```
## 2. Historical IDR → USD Rates

Fetch historical exchange rate data.
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```
Example response:
```json
{
  "timestamp": "2026-03-11T09:18:41.7988217",
  "status": 200,
  "message": "Success",
  "data": {
    "amount": 1,
    "base": "IDR",
    "rates": {
      "2024-01-04": {
        "USD": 0.000064
      },
      "2024-01-05": {
        "USD": 0.000064
      }
    }
  }
}
```
## 3. Supported Currencies
Fetch all supported currency symbols.
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```
Example response:
```json
{
  "timestamp": "2026-03-11T10:14:39.1794292",
  "status": 200,
  "message": "Success",
  "data": {
    "currencies": {
      "USD": "United States Dollar",
      "EUR": "Euro",
      "SGD": "Singapore Dollar"
    }
  }
}
```
# Personalization Note
This implementation includes a unique banking spread factor derived from the GitHub username.
*Spread Factor Calculation*
1. Convert username to lowercase
2. Sum the Unicode values of each character
3. Apply the formula:
```code
Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
```
Example:
```code
Username: Minyeng
Unicode Sum: 759
Spread Factor = (759 % 1000) / 100000
Spread Factor = 0.00759
Final Formula Used
```
For the latest_idr_rates resource:
```code
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)
```
Where:
 - `Rate_USD` = USD rate returned by Frankfurter API when base = IDR
 - `SpreadFactor` = personalized factor derived from GitHub username
This produces a *unique spread value per developer implementation*.