# AlloBank Java Backend Test

## Setup / Run Instructions

1. **Clone the repository**

```bash
git clone <repo-url>
cd allobank
```

2. **Build the project**

```bash
./mvnw clean package
```

3. **Run the application**

```bash
./mvnw spring-boot:run
```

4. **Run tests**

```bash
./mvnw test
```

---

## Endpoint Usage

1. **Latest IDR Rates**

```bash
curl -X GET http://localhost:8080/fetch/latest_idr_rates
```

2. **Historical IDR-USD Rates**

```bash
curl -X GET http://localhost:8080/fetch/historical_idr_usd
```

3. **Supported Currencies**

```bash
curl -X GET http://localhost:8080/fetch/supported_currencies
```

---

## Personalization Note

* **GitHub Username:** pleaseLetMeJoin
* **Spread Factor:** 0.00666
