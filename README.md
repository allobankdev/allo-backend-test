# Finance Data Aggregator API

A production-ready Spring Boot Reactive API that aggregates and transforms currency exchange data from the public Frankfurter API. Built with high performance, thread-safety, and clean architecture in mind.

##  Setup & Run Instructions


**Prerequisites:**
- Java 17 or higher
- Maven 3.8+

**1. Clone the repository:**
```bash
git clone <repository_url>
cd allo-backend-test
```

**2. Build the application and run tests:**
```bash
mvn clean install
```

**3. Run Application:**
```bash
mvn spring-boot:run
```


## Endppoint Usage

The application exposes a single endpoint serving data from the in-memory store.
```bash
Base URL: http://localhost:8080/api/finance/data/{resourceType}
```
Example cURL Commands:

Latest IDR Rates (with custom spread calculation):
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

Historical IDR to USD:
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

Supported Currencies:
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization Note
GitHub Username: [asyifa-zahwa]

Calculated ASCII Sum: [1221]

Calculated Spread Factor: [17279.48275862069]



