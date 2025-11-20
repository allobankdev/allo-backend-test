# Getting Started

## Requirement:
  1. Java 21
  2. Internet connection

## Setup/Run Instructions

### **Clone repository**

```bash
git clone https://github.com/AthallahRamadani/allo-backend-test.git
cd allo-backend-test
```
### **Build project**
```bash
mvn clean install
```
### **Run Spring Boot**
```bash
mvn spring-boot:run
```

## Endpoint Usage (cURL Examples)
### Base path:
```shell
/api/finance/data/{resourceType}
```

### Resource types:
   1. historical_idr_usd
   2. latest_idr_rates
   3. supported_currencies

### historical_idr_usd
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

### latest_idr_rates
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

### supported_currencies
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```
all endpoint will take data from in-memory state, not from external api anymore

## **Running test**
```bash
mvn test
```
## **Personalization Note**
### github username
```
AthallahRamadani
```
### spread factor
```
0.00660
```










