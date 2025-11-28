# Allo Backend Test - solution

## Requirements
- Java 17+
- Maven

## Build & run
1. edit `src/main/resources/application.yml` and set `github.username: yourusername`
2. mvn clean package
3. java -jar target/allo-backend-test-1.0.0.jar
   or mvn spring-boot:run

On startup the app will fetch 3 resources from Frankfurter API and store them in memory.

## Endpoints
GET /api/finance/data/latest_idr_rates
GET /api/finance/data/historical_idr_usd
GET /api/finance/data/supported_currencies

Example:
curl http://localhost:8080/api/finance/data/latest_idr_rates

## Spread Factor calculation
Sum of Unicode values of your lowercase GitHub username:
SpreadFactor = (sum % 1000) / 100000.0

E.g. for username `renta10`:
r = 114
e = 101
n = 110
t = 116
a = 97
1 = 49
0 = 48

114 + 101 + 110 + 116 + 97 + 49 + 48 = 635
(635 % 1000) / 100000.0 = 0.00635

