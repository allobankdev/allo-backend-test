# Finance Data Aggregator

## Setup/Run Instructions
1. Clone: `git clone [repository-url]`
2. Build: `./gradlew clean build`
3. Configure: Edit `application.yml` and set `app.github.username: `
4. Run: `./gradlew bootRun` (starts on http://localhost:8080)
5. Test: `./gradlew test`

## Endpoint Usage

# Latest IDR rates with spread calculation
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Historical IDR to USD rates
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Supported currencies
curl http://localhost:8080/api/finance/data/supported_currencies


## Personalization Note
**GitHub Username:** andityadimas  
**Spread Factor:** 0.00272

**Calculation:**
- Username: andityadimas
- Lowercase: andityadimas
- Unicode sum: 1272
- 1272 % 1000 = 272
- 272 / 100000 = 0.00272