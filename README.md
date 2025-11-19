# IDR Rate Aggregator API

A Spring Boot application that aggregates financial data from the Frankfurter API for IDR (Indonesian Rupiah) conversions.

## Setup & Run

1.  **Clone:** `git clone <repo-url>`
2.  **Build:** `./mvnw clean install`
3.  **Run:** `./mvnw spring-boot:run`

The application will fetch data immediately upon startup.

## Endpoints

| Method | URL | Description                                           |
| :--- | :--- |:------------------------------------------------------|
| GET | `/api/finance/data/latestIDRRate` | IDR based rates with personalized spread calculation. |
| GET | `/api/finance/data/historyIDRToUSD` | Historical data for Nov 1-5, 2025.                    |
| GET | `/api/finance/data/supportedCurrencies` | List of all currency symbols.                         |

## Personalization Logic

**GitHub Username:** `nikods761` (Replace in `IdrLatestRatesStrategy.java`)

**Spread Factor Calculation:**
1.  Sum of ASCII values of `nikods761`.
2.  Formula: `(Sum % 1000) / 100000.0`
3.  Example Result (if sum is 1234): `0.00234`

## Testing

Run unit and integration tests via:
`./mvnw test`

## Architectural Rationale
The following sections explain the design choices made in the solution.

1. Polymorphism Justification

   Why Strategy Pattern? Handling multiple resource types (latest_idr, historical, currencies) inside a single Service method using if/else or switch statements,  the core service logic would need modification, increasing the risk of regression bugs.

2. Client Factory

   Why FactoryBean<RestTemplate>? While a simple @Bean method works for basic instantiation, a FactoryBean provides a formalized structural pattern for complex object construction.

    It encapsulates the configuration logic (headers, codecs for large payloads, timeout definitions) within a dedicated class rather than polluting a generic @Configuration class.
    And if the application scales and requires multiple clients for different endpoints with similar configs, the FactoryBean can be easily parameterized or extended.

3. Startup Runner Choice

    Why ApplicationRunner over @PostConstruct?
   @PostConstruct runs as soon as the specific bean is initialized. At this stage, other dependent beans or AOP proxies might not be fully ready, leading to subtle timing bugs. ApplicationRunner triggers only after the entire Spring ApplicationContext is fully refreshed and ready.
