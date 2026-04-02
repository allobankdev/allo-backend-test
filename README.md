# Finance API

A Spring Boot REST API that fetches Frankfurter exchange rate data, focusing specifically on the Indonesian Rupiah (IDR).

## Setup

### Prerequisites
- Java 17 or newer
- Maven Wrapper scripts (mvnw, mvnw.cmd) are included; the maven-wrapper.jar is not tracked by Git and will be downloaded automatically on first run of ./mvnw

### Running the project
```bash
./mvnw clean package
./mvnw spring-boot:run
```

### Running tests
```bash
./mvnw test
```

## Endpoint

Single endpoint:

```text
GET /api/finance/data/{resourceType}
```

Valid `{resourceType}` values:
- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

### Latest IDR Rates
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

Example response:
```json
[
  {
    "resourceType": "latest_idr_rates",
    "data": {
      "amount": 1.0,
      "base": "IDR",
      "date": "2024-03-08",
      "rates": {
        "USD": 0.000065
      },
      "USD_BuySpread_IDR": 15482.923076923078,
      "spreadFactor": 0.01209
    }
  }
]
```

### Historical IDR-USD Data
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

Example response:
```json
[
  {
    "resourceType": "historical_idr_usd",
    "data": {
      "amount": 1.0,
      "base": "IDR",
      "start_date": "2024-01-01",
      "end_date": "2024-01-05",
      "rates": {
        "2024-01-01": {
          "USD": 0.000065
        }
      }
    }
  }
]
```

### Supported Currencies
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

Example response:
```json
[
  {
    "resourceType": "supported_currencies",
    "data": {
      "IDR": "Indonesian Rupiah",
      "USD": "United States Dollar"
    }
  }
]
```

## Personalization Note

- GitHub username: `devsid`
- Unicode sum: `639`
- Spread factor: `0.00639`
- Formula: `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00639)`

## Architecture

### Polymorphism Justification
We use the Strategy Pattern here so every Frankfurter resource gets its own implementation. This helps us avoid those massive, ever-growing `if/else` or `switch` blocks. The controller simply picks the right strategy straight from a `Map<String, IDRDataFetcher>` that Spring injects for us. Because of this approach, if we ever need to add a new resource, we just drop in a new strategy class without touching the existing endpoint structure.

### Client Factory
The `WebClient` is created via a `FactoryBean<WebClient>`. This keeps the client configuration centralized instead of scattered all over the codebase. We read the base URL right from `application.yml`, apply timeouts during creation, and set up default headers in one go. We went with this over a standard `@Bean` because building the client is a distinct concern that makes sense to wrap in a dedicated factory.

### Startup Runner Choice
We chose `ApplicationRunner` because the data preload needs to happen only after all Spring beans—including the strategies, repository, and WebClient—are fully ready to go. It's much safer than using `@PostConstruct` for an initialization process that relies on multiple beans. Plus, it's just a clearer way to handle logic specifically meant for the startup lifecycle.

## Implementation Summary

- The controller looks up the correct strategy based on the `resourceType`.
- The service acts as the business flow layer sitting between the controller and the strategies.
- The repository holds an in-memory snapshot of the data.
- We fetch the data from Frankfurter exactly once during startup.
- Once the preload finishes, the repository is sealed. This ensures the data we serve stays immutable.
- All endpoint responses come back as a JSON array containing a single result element per resource.
