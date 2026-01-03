
# IDR Rates Aggregator - Allo Bank Backend Test

A Spring Boot application that aggregates financial data from the Frankfurter Exchange Rate API and exposes a unified REST endpoint for Indonesian Rupiah (IDR) data. The application demonstrates clean architecture, Strategy Pattern, thread-safe in-memory storage, and personalized currency spread calculation.


## Prerequisites

- Java 17
- Maven 3.8+
- Internet connection (for external API access)


## Setup & Run

Clone the project

```bash
  git clone https://github.com/fluxions-471/allo-backend-test.git
```

Go to the project directory

```bash
  cd /allo-backend-test
```

Build the project

```bash
  mvn clean install
```

Run the application

```bash
  mvn spring-boot:run
```

Application will start on http://localhost:8080


## API Endpoint

#### Get Finance Data

```http
  GET /api/finance/data/{resourceType}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `resourceType` | `string` | One of latest_idr_rates, historical_idr_usd, supported_currencies|

 #### Example Request

 ```bash
  curl --location 'localhost:8080/api/finance/data/latest_idr_rates'
  curl --location 'localhost:8080/api/finance/data/historical_idr_usd'
  curl --location 'localhost:8080/api/finance/data/supported_currencies'
```

 #### Response Format

All responses follow a standardized JSON structure:

 ```json
  {
    "success": true,
    "message": "Data fetched successfully",
    "data": { ... },
    "timestamp": "2026-01-03T23:52:02.537488",
  }
```

GitHub username : fluxions-471

Spread Factor : 0.00089


## Running Tests

To run tests, run the following command

```bash
  mvn test
```

## Architectural Rationale

#### Strategy Pattern
- Each resource type has a dedicated strategy implementing IDRDataFetcher. 
- Promotes extensibility: adding a new resource requires minimal changes.
- Controller dynamically selects the correct strategy from a Spring-injected map, avoiding if/else or switch.

#### WebClient FactoryBean
- External API client is created via a custom FactoryBean.
- Centralizes configuration (base URL, timeouts) and promotes testability and decoupling.

#### Startup Data Loader
- Uses ApplicationRunner to fetch all resources on application startup.
- Stores data in an immutable, thread-safe in-memory store.
- Ensures endpoint responses do not make repeated external API calls.

