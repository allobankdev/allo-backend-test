# Allo Bank Backend Developer Take-Home Test Tri Setyadi

// repo
git clone https://github.com/allobankdev/allo-backend-test.git
$ cd allo-backend-test
git checkout feat/idr-rate-aggregator

// install
You can use the Maven wrapper or Maven installed locally.
mvn clean install
mvn spring-boot:run

// api endpoint
Base URL
http://localhost:8081/api/finance/data/{resourceType}

Available resourceType values:
- latest_idr_rates =	Latest exchange rates from IDR to all supported currencies with USD_BuySpread_IDR
- historical_idr_usd =	Historical exchange rates from IDR to USD for a fixed date range
- supported_currencies =	List of all supported currencies and their full names

curl
curl -X GET http://localhost:8081/api/finance/data/latest_idr_rates
curl -X GET http://localhost:8081/api/finance/data/historical_idr_usd
curl -X GET http://localhost:8081/api/finance/data/supported_currencies

// GitHub Username & Spread Factor
ASCII Sum Calculation:
s=115, e=101, t=116, y=121, a=97, d=100, i=105, t=116, r=114, i=105
Total = 1090

Spread Factor Formula:
Spread Factor = (ASCII Sum % 1000) / 100000.0
              = (1090 % 1000) / 100000.0
              = 90 / 100000.0
              = 0.00090

USD_BuySpread_IDR Calculation Example:
Rate_USD = 0.00006
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
                  = 16681.666666666668

Architectural Rationale :
- Polymorphism Justification (Strategy Pattern)
Each resource (latest_idr_rates, historical_idr_usd, supported_currencies) is handled by a dedicated strategy class implementing IDRDataFetcher.
- Client Factory (WebClient via FactoryBean)
The WebClient instance is constructed using a custom FactoryBean, not a simple @Bean.
- Startup Runner Choice (ApplicationRunner)
FinanceDataLoader implements ApplicationRunner to fetch all data at startup.