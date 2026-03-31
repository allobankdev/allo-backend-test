# Allo Bank Backend Developer Take-Home Test

## Summary
This Pull Request provides a fully working Spring Boot REST API satisfying all core integration rules and strict architecture constraints. The project adopts `WebFlux` for non-blocking API interactions and computes custom dynamic spreads based on IDR metrics securely.

### Personalization Note
- **GitHub Username:** `tech-enthusiast-168`
- **Spread Factor Calculation:** Sum of ASCII values = `1765`. `1765 % 1000 = 765`. `765 / 100000.0 = 0.00765`
- **Spread Factor:** `0.00765`

## Architectural Rationale

**1. Polymorphism Justification:**  
The Strategy Pattern (`DataFetcherStrategy.java`) was chosen over procedural `if/else` or `switch` blocks within the service layer to ensure the Open/Closed Principle (OCP). By allowing Spring Framework to dynamically map implementations through injected beans (`Map<String, DataFetcherStrategy>`), adding a new external integration source requires zero changes to the `FinanceController` layer, significantly elevating maintainability and code extensibility.

**2. Client Factory:**  
Constructing the `WebClient` directly through `FrankfurterClientFactoryBean` strictly externalizes external connectivity configurations (i.e., timeouts, connection schemas) out of typical `@Bean` contexts. This guarantees that instantiation configurations remain highly decoupled, ensuring lifecycle independence and allowing straightforward future additions (such as dynamic bean substitution or connection proxying based on environment flags) completely devoid of standard Spring component cross-contamination.

**3. Startup Runner Choice:**  
The implementation utilizes an `ApplicationRunner` (`InitialDataRunner.java`) instead of a traditional `@PostConstruct` hook. `@PostConstruct` is inherently bound to the bean initialization phase, which can lead to severe cyclic dependency faults and delays overall dependency resolution. In contrast, `ApplicationRunner` rigidly fires *only after* the full Spring application context has successfully started, preventing race conditions and granting total certainty that `DataStoreService` is definitively active and accessible before attempting memory ingestion operations.
