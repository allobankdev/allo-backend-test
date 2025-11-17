## 📖 Setup & Run Instructions

```bash
# Clone the repository
git clone https://github.com/AriAulia/allo-backend-test.git
cd allo-backend-test

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Latest IDR rates with spread factor
curl http://localhost:9990/api/finance/data/latest_idr_rates

# Historical IDR to USD rates
curl http://localhost:9990/api/finance/data/historical_idr_usd

# Supported currencies
curl http://localhost:9990/api/finance/data/supported_currencies

# Run Swagger
http://localhost:9990/swagger/
```
 