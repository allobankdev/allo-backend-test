# Allo Bank Backend Developer Take-Home Test

Project ini menggunakan Springboot, untuk mendapatkan data finance dari Frankfurter Exhange Rate API.


### Steps to Run

1. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Run tests:
   ```bash
   mvn test
   ```

## Endpoint Usage

The application provides a single polymorphic internal API endpoint: `GET /api/finance/data/{resourceType}`.

### 1. Latest IDR Rates
Returns the latest exchange rates with IDR as the base currency, including a calculated `USD_BuySpread_IDR`.
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2. Historical IDR-USD
Returns historical exchange rates between IDR and USD for the range 2024-01-01 to 2024-01-05.
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Supported Currencies
Returns a list of all supported currencies and their codes.
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

## Personalization
- GitHub Username: `codelamps-academy`
- Spread Factor: `0.00721`
  - Calculation:  "codelamps-academy" = 1721. `1721 % 1000 = 721`. `721 / 100000.0 = 0.00721`.

