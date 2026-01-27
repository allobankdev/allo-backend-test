# Allo Bank – IDR Finance Data Aggregator

This project is a Spring Boot application developed as part of the **Allo Bank Backend Developer Take-Home Test**.

The application exposes a **single polymorphic REST API endpoint** that aggregates financial data related to Indonesian Rupiah (IDR) from the public **Frankfurter Exchange Rate API**.  
All external data is fetched **once at application startup** and served from an **in-memory, thread-safe store**.

---

##  Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web (MVC)
- Spring WebFlux (WebClient)
- Maven
- Lombok

---

## Features

- Single REST endpoint with polymorphic behavior
- Strategy Pattern for resource-based data fetching
- Custom `FactoryBean` for WebClient construction
- Startup data loading using `ApplicationRunner`
- Thread-safe and immutable in-memory data storage
- No repeated external API calls per request

---

## External API

Data is retrieved from the public Frankfurter Exchange Rate API:

