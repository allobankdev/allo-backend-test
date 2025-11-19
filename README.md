# Allo Bank Backend Test – Hann6

**Personal USD → IDR Buy Spread**: **+177.0 IDR**  
Calculation: `50 + Math.abs("Hann6".hashCode()) % 201 = 177`


## Features Implemented
- Clean layered architecture (client / service / controller / dto / model / handler)
- Thread-safe in-memory cache using Spring-managed `ConcurrentHashMap`
- Automatic data preloading from `https://api.frankfurter.app` on startup
- Personal spread +177 IDR correctly applied to latest USD rate
- `FactoryBean` implementation (explicitly required)
- Type-safe JSON responses using Java 17 `record` DTOs
- Global exception handling with `@RestControllerAdvice`
- Proper SLF4J + Logback logging
- Health check endpoint `/actuator/health`
- Unit and context-load tests (all green)

## API Endpoints

| Method | URL                              | Description                                      |
|-------|----------------------------------|--------------------------------------------------|
| GET   | `/api/finance/data/latest`       | Latest rates (base IDR) + personal spread        |
| GET   | `/api/finance/data/historical`   | Historical rates 2024-01-01 to 2024-01-05          |
| GET   | `/api/finance/data/currencies`   | List of supported currencies                     |
| GET   | `/actuator/health`               | Returns "UP"                                     |

## Running the Application

### Prerequisites
- Java 17 or higher
- Git
- Maven (Maven Wrapper included)

### Step-by-Step Instructions

```bash
# 1. Clone the repository
git clone https://github.com/Hann6/allo-backend-test.git
cd allo-backend-test

# 2. Verify Java version
java -version
# → should display version 17.x

# 3. Start the application (recommended)
./mvnw spring-boot:run

# Windows users:
# mvnw.cmd spring-boot:run

# Or using regular Maven
# mvn spring-boot:run