# allo-backend-test (IDR Rate Exchange Feature)

This project is a Spring Boot backend service for handling IDR exchange rates, including latest rates, historical data, and supported currencies. It also provides Swagger documentation for easy API exploration.

---

## 🚀 Setup & Run Instructions

### Prerequisites
- Java 17+
- Maven Wrapper (included in repo)
- Internet connection (for fetching exchange rates)

### Steps
1. **Clone the repository**
    ```bash
    # Clone the repository
    git clone https://github.com/AriAulia/allo-backend-test.git
    
    # Navigate into the project directory
    cd allo-backend-test
    
    # Checkout specific branch
    git checkout feature/idr-rate-exchange
    
    # Build and run the application
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```
2. **API Endpoints**
   1. Latest IDR Rates with Spread Factor
       Endpoint: GET /api/finance/data/latest_idr_rates
       Description: Returns the latest IDR exchange rates with applied spread factor.
       example request:
    ```bash
    curl http://localhost:9990/api/finance/data/latest_idr_rates
    ```

   2. Historical IDR Rates
       Endpoint: GET /api/finance/data/historical_idr_rates
       Description: Returns historical IDR exchange rates for a specified date range.
       example request:
    ```bash
    curl "http://localhost:9990/api/finance/data/historical_idr_rates?start_date=2023-01-01&end_date=2023-01-31"
    ```
   
   3. Supported Currencies
       Endpoint: GET /api/finance/data/supported_currencies
       Description: Returns a list of supported currencies for exchange rates.
       example request:
    ```bash
    curl http://localhost:9990/api/finance/data/supported_currencies
    ```
3. **Personaliza Note**
    GitUsername: AriAulia
    Spread factor: 0.0084