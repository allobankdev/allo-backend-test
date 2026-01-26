# Allo Bank Backend Test

## 1. Setup/Run Instructions
- Clone
````
git clone https://github.com/chnh16/allo-backend-test.git
````
- Build and Run Test
````
mvn clean package
````
- Run Application
````
mvn spring-boot:run
````
- Build Docker Image
````
docker build -t backendtest:0.0.1 .
````
- Run Docker Image
````
docker run -p 8080:8080 backendtest:0.0.1
````

## 2. Endpoint Usage
````
curl http://localhost:8080/api/finance/data/{resourceType}
````
Available resource type :
- latest_idr_rates
- historical_idr_usd
- supported_currencies

## 3. Personalization Note
- Github Username : chnh16
- Spread Factor : 520




