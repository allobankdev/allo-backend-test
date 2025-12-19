# Currency Rate Aggregator (IDR Focus)
## Overview

This Spring Boot application aggregates multiple financial data resources from the public **Frankfurter Exchange Rate API**, with a focus on **Indonesian Rupiah (IDR)**.
All external data is fetched **once at application startup** and served via a **single polymorphic REST endpoint** using clean architectural patterns.
---
## Tech Stack
- Java 21
- Spring Boot (REST API)
- Spring WebClient
- Maven
- JUnit 5 & Mockito

---

## How to Run

### Run Tests
```bash
mvn clean test
```
### Run Application
```bash
mvn spring-boot:run
```
Application will start at: http://localhost:8080

---
## API Endpoint
| Resource Type          | Description                                              |
| ---------------------- | -------------------------------------------------------- |
| `latest_idr_rates`     | Latest IDR exchange rates with calculated USD buy spread |
| `historical_idr_usd`   | Historical IDR → USD exchange rates                      |
| `supported_currencies` | List of supported currencies                             |

Example Request:
```declarative
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

## Personalization
```
Github: noobsemaj / kzulfazriawan
Email: kzulfazriawan@gmail.com
```