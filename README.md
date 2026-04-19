# Allo Bank Backend Assessment - Java Developer

This application provides foreign exchange (forex) rate information against IDR (Indonesian Rupiah). It includes features for calculating Buy Spreads and retrieving Historical Forex Data.

## I. Setup & Run Instruction

### 1. System Required

* Java 21 or higher
* Maven 3.9+

### 2. Build The Application

#### How To Clone
git clone `https://github.com/tiknopreno/allo-backend-test.git`
cd allo-backend-test
mvn clean install

### 3. Run the Application
mvn spring-boot:run

### 4. Run Test
mvn test

## II. Endpoint Usage (cURL Examples)
### 1. Latest IDR (Caculate with spread)
#### a. Fetches the all latest rates
curl -X GET `"http://localhost:8080/api/finance/data/latest_idr_rates"`

#### b. Fetches the latest rates and applies the Buy Spread calculation based on the target currency.
curl -X GET `"http://localhost:8080/api/finance/data/latest_idr_rates?target=USD"`


### 2. Historical IDR Rates
Fatches historical forex data for the last 5 days
#### a. Fetches the all hisotry
curl -X GET `"http://localhost:8080/api/finance/data/historical_idr_usd"`

#### b. Fethces history base on target
curl -X GET `"http://localhost:8080/api/finance/data/historical_idr_usd?target=USD"`

### 3 Historical IDR Rates
Retrieves a list of all supported currency symbols.
curl -X GET `"http://localhost:8080/api/finance/data/supported_currencies"`

## III. Interactive Documentation (Swagger)
Once the application is running, you can access the Swagger UI at:
`http://localhost:8080/swagger-ui/index.html`

