### A. Setup
```
-- 1. clone repository
git clone https://github.com/sukrosono/allo-backend-test.git
cd /sukrosono
-- 2. install spring boot artifact and project dependency
mvn clean install
-- 3. Run test
mvn test
-- 4. run the microservice
mvn spring-boot:run

```




### B. Endpoint
1. latest_idr_rates
```
curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates'
```
1. historical_idr_usd
```
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd'
```
1. supported_currencies
```
curl --location 'http://localhost:8080/api/finance/data/supported_currencies'
```

### C. Note
github username: sukrosono with spead factor `0.000110`

