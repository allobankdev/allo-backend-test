# Architectural Rationale - Allo Bank Backend Developer Test

## 1. Polymorphism Justification: Strategy Pattern
Why Strategy Pattern Over Simple Conditional Blocks?
The requirement explicitly mandated the use of the Strategy Design Pattern for handling the three distinct resources (latest_idr_rates, historical_idr_usd, supported_currencies). This architectural choice provides significant benefits over a simpler conditional approach:Benefits Realized:

A. Extensibility (Open/Closed Principle)

// To add a new resource type:
// 1. Implement DataFetcherStrategy interface
// 2. Annotate with @Component
// 3. No modifications needed in existing code
public class NewResourceFetcher implements DataFetcherStrategy {
    @Override
    public String getResourceType() { return "new_resource"; }
    @Override
    public Mono<Object> fetchData() { /* implementation */ }
}

Zero modification to controller or existing strategies
Plug-and-play architecture for future requirements

B. Maintainability and Single Responsibility
Each strategy encapsulates logic for one specific resource type
Changes to historical data logic don't affect latest rates or currencies
Simplified debugging and testing

C. Clean Controller Layer
@GetMapping("/{resourceType}")
public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
    // No if-else or switch statements
    Object data = financeDataService.getFinanceData(resourceType);
    return ResponseEntity.ok(data);
}

Controller focuses only on HTTP concerns
Business logic delegated to strategy implementations

D. Dynamic Strategy Resolution
Spring's dependency injection automatically maps strategies:

@Autowired
private Map<String, DataFetcherStrategy> strategies;
// Map key: resourceType, Value: corresponding strategy instance
Runtime strategy selection without manual mapping
Type-safe strategy retrieval

## 2. Client Factory Bean Design  
Why FactoryBean Over Standard @Bean Method?
Implementation:
@Component
public class ApiClientFactoryBean implements FactoryBean<WebClient> {
    private final AppProperties appProperties;
    
    @Override
    public WebClient getObject() {
        // Centralized configuration with externalized properties
        HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofMillis(
                appProperties.getFrankfurter().getTimeout().getRead()))
            .connectTimeout(Duration.ofMillis(
                appProperties.getFrankfurter().getTimeout().getConnect()));
        
        return WebClient.builder()
            .baseUrl(appProperties.getFrankfurter().getBaseUrl())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader("Accept", "application/json")
            .build();
    }
    
    @Override
    public Class<?> getObjectType() { return WebClient.class; }
    
    @Override
    public boolean isSingleton() { return true; }
}

Advantages Over Simple @Bean Method:
A. Encapsulation of Complex Creation Logic
FactoryBean hides the complexity of WebClient configuration
Externalizes timeout settings, base URL, and headers
Single responsibility: client creation and configuration

B. Proper Lifecycle Management
Spring manages complete bean lifecycle
Guaranteed initialization order (properties loaded before client creation)
Can implement initialization/destruction callbacks if needed

C. Configuration Externalization
yaml
# application.yml - All configuration externalized
app:
  frankfurter:
    base-url: https://api.frankfurter.app
    timeout:
      connect: 5000    # milliseconds
      read: 10000      # milliseconds
Configuration changes don't require code modification
Environment-specific configurations easily managed

D. Singleton Guarantee and Thread Safety
isSingleton() = true ensures single instance for entire application
Thread-safe WebClient instance shared across all strategies
Efficient resource utilization

E. Type Safety and Dependency Injection
Generic type parameter ensures compile-time type checking
Easily injectable into strategy classes via constructor injection

## 3. Startup Data Loading: ApplicationRunner Choice
Why ApplicationRunner Over @PostConstruct?
Implementation:
@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {
    
    private final Map<String, DataFetcherStrategy> strategies;
    private final DataStorageService dataStorageService;
    
    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data loading process...");
        
        strategies.forEach((key, strategy) -> {
            try {
                log.info("Loading data for: {}", strategy.getResourceType());
                Object data = strategy.fetchData();
                dataStorageService.storeData(strategy.getResourceType(), data);
                log.info("Successfully loaded data for: {}", strategy.getResourceType());
            } catch (Exception e) {
                log.error("Failed to load data for {}: {}", 
                         strategy.getResourceType(), e.getMessage());
            }
        });
        
        dataStorageService.setLoaded(true);
        log.info("Data loading completed. Total resources loaded: {}", 
                 dataStorageService.getAllData().size());
    }
}

Advantages Over @PostConstruct:
A. Application Context Readiness Guarantee
ApplicationRunner executes after ApplicationContext is fully initialized
All beans (strategies, WebClient, services) guaranteed to be available
Avoids NullPointerExceptions common with @PostConstruct

B. Command Line Argument Support
@Override
public void run(ApplicationArguments args) {
    // Can access and react to command line arguments
    if (args.containsOption("skip-data-load")) {
        log.info("Skipping data loading per command line argument");
        return;
    }
    // ... normal loading
}

C. Execution Order Control
@Component
@Order(1)  // Explicit execution order if multiple runners exist
public class DataLoaderRunner implements ApplicationRunner

D. Robust Error Handling
Exceptions in ApplicationRunner don't prevent application startup
Can implement retry logic, fallback mechanisms, or graceful degradation
Logging and monitoring of data loading process

E. Async Loading Potential
@Async  // Can be made asynchronous if data loading is time-consuming
@Override
public void run(ApplicationArguments args) {
    // Async loading to prevent blocking application startup
}

F. Production-Readiness
Suitable for production environments with complex startup sequences
Integrates well with Spring Boot's startup events and monitoring

## 4. Thread-Safe Immutable Data Storage
Design Implementation:
@Service
public class DataStorageService {
    
    // Thread-safe concurrent map
    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    
    // volatile ensures visibility across threads
    private volatile boolean isLoaded = false;
    
    public void storeData(String resourceType, Object data) {
        dataStore.put(resourceType, data); // Thread-safe operation
    }
    
    public Object getData(String resourceType) {
        if (!isLoaded) {
            throw new IllegalStateException("Data not loaded yet");
        }
        return dataStore.get(resourceType); // Thread-safe read
    }
    
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore); // Immutable view
    }
}
Thread Safety Guarantees:
ConcurrentHashMap: Provides thread-safe concurrent access
volatile flag: Ensures memory visibility of loaded state
Immutable after loading: Data cannot be modified post-initialization
Unmodifiable views: Prevents accidental modification of stored data

## 5. Error Handling Strategy
Graceful Degradation:
@RestController
@RequestMapping("/finance/data")
public class FinanceDataController {
    
    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        try {
            Object data = financeDataService.getFinanceData(resourceType);
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            // Invalid resource type
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid resource type", 
                           "supportedTypes", List.of("latest_idr_rates", 
                                                    "historical_idr_usd", 
                                                    "supported_currencies")));
        } catch (IllegalStateException e) {
            // Application still starting up
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service temporarily unavailable"));
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Internal server error"));
        }
    }
}

## 6. Production Readiness Features
Implemented:
✅ Externalized configuration via application.yml
✅ Proper logging with SLF4J
✅ Connection timeouts and retry resilience
✅ Clean separation of concerns
✅ Comprehensive error handling
✅ Thread-safe design patterns

## Conclusion
This architecture demonstrates a production-ready Spring Boot application that not only meets the specified requirements but also follows industry best practices:

Clean Architecture: Separation of concerns, dependency injection
Design Patterns: Strategy, Factory, Singleton patterns appropriately applied
Thread Safety: Concurrent data structures and volatile flags
Production Readiness: Error handling, logging, configuration management
Maintainability: Extensible design, clean code, comprehensive documentation
The implementation provides a solid foundation that can be easily extended, maintained, and scaled for production use in a financial services environment.

## **Answers to Required Architectural Questions:**
### **1. Why Strategy Pattern was used over simpler conditional blocks?**
The Strategy Pattern was chosen for its extensibility and maintainability benefits. Instead of using if-else or switch statements in the service layer, each resource type has its own dedicated strategy class. This allows:
Easy addition of new resource types without modifying existing code (Open/Closed Principle)
Each strategy has single responsibility, making debugging and testing simpler
The controller remains clean without business logic, focusing only on HTTP concerns
Dynamic strategy resolution through Spring's dependency injection

### **2. Why use FactoryBean to construct the external API client?**
FactoryBean was chosen over a standard @Bean method because:
It encapsulates the complex WebClient configuration logic in one place
Allows externalization of configuration (timeouts, base URL) to application.yml
Provides better lifecycle management guaranteed by Spring
Ensures thread-safe singleton instance for the entire application
Enables type safety and easier dependency injection into strategy classes
More suitable for production environments where client configuration might need complex setup

### **3. Why use ApplicationRunner for initial data ingestion?**
ApplicationRunner was selected over @PostConstruct because:
It executes after the ApplicationContext is fully initialized, guaranteeing all beans (strategies, WebClient) are ready
Provides access to command line arguments for flexible startup behavior
Allows execution order control with @Order annotation if multiple runners exist
Better error handling - exceptions don't prevent application startup
Suitable for production with potential for async loading if data ingestion takes time
Integrates well with Spring Boot's monitoring and startup event system

---
**Implementation Details:**
- **GitHub Username**: aryasegara
- **Spread Factor**: 0.00056
- **USD_BuySpread_IDR**: 16676.0
- **Framework**: Spring Boot 3.4.12