# 💱 Allo Bank IDR Rate Aggregator

> Take-home test solution for Backend Developer position - Allo Bank

Aplikasi Spring Boot untuk aggregasi data exchange rate dari Frankfurter API dengan fokus pada Indonesian Rupiah (IDR).

---

## 📋 Fitur

- ✅ Single endpoint: `GET /api/finance/data/{resourceType}`
- ✅ Strategy Pattern untuk handling multi-resource
- ✅ FactoryBean untuk konfigurasi HTTP client
- ✅ Data preloading saat startup (in-memory cache)
- ✅ Personalized spread calculation berdasarkan GitHub username
- ✅ Dynamic query parameters untuk historical data

---

## 🚀 Cara Menjalankan

### Prerequisites

```
✅ Java 17
✅ Maven 3.8+
✅ Git
```

### Step by step

```bash
# 1. Clone repository
git clone <your-fork-url>
cd demo

# 2. Set GitHub username (untuk kalkulasi spread)
# Pilihan A: via environment variable
export GITHUB_USERNAME=your-github-username

# Pilihan B: edit langsung di application.yml
# app.github-username: your-github-username

# 3. Build dan jalankan aplikasi
./mvnw spring-boot:run

# 4. Aplikasi akan running di http://localhost:8080
```

---

## 📡 API Endpoint

### Base URL

```
http://localhost:8080/api/finance/data
```

### 1. Health Check

```bash
curl http://localhost:8080/api/finance/data/health
```

```json
{
  "status": "ready",
  "resources": ["latest_idr_rates", "historical_idr_usd", "supported_currencies"]
}
```

### 2. Latest IDR Rates (dengan Spread Calculation)

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

```json
{
  "base": "IDR",
  "date": "2024-01-15",
  "rates": {
    "USD": 0.000064,
    "USD_BuySpread_IDR": 15640.25,
    "EUR": 0.000058,
    "..."
  }
}
```

### 3. Historical IDR-USD (Default Date Range)

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

```json
[
  { "date": "2024-01-01", "IDR_to_USD": 0.000064 },
  { "date": "2024-01-02", "IDR_to_USD": 0.000063 }
]
```

### 4. Historical IDR-USD (Custom Date Range)

```bash
# Custom tanggal
curl "http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-01-10&endDate=2024-01-12"

# Custom currency pair
curl "http://localhost:8080/api/finance/data/historical_idr_usd?from=IDR&to=EUR"

# Full custom params
curl "http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-02-01&endDate=2024-02-07&from=IDR&to=SGD"
```

#### Query Parameters

| Parameter   | Required | Default    | Description              |
|-------------|----------|------------|--------------------------|
| `startDate` | No       | 2024-01-01 | Start date (YYYY-MM-DD)  |
| `endDate`   | No       | 2024-01-05 | End date (YYYY-MM-DD)    |
| `from`      | No       | IDR        | Base currency            |
| `to`        | No       | USD        | Target currency          |

### 5. Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

```json
[
  { "code": "AED", "name": "United Arab Emirates Dirham" },
  { "code": "IDR", "name": "Indonesian Rupiah" },
  { "code": "USD", "name": "United States Dollar" }
]
```

### Error Handling

```bash
curl http://localhost:8080/api/finance/data/invalid_resource
```

```json
{
  "error": "Unknown resource: invalid_resource",
  "available": ["latest_idr_rates", "historical_idr_usd", "supported_currencies"]
}
```

---

## 👤 Personalization: Spread Factor

| GitHub Username |
|-----------------|
| `your-github-username` |

### Kalkulasi Spread Factor

**Rumus:** `(Sum of ASCII values % 1000) / 100000.0`

Contoh untuk username `"alice"`:

```
a = 97, l = 108, i = 105, c = 99, e = 101
Sum = 97 + 108 + 105 + 99 + 101 = 510
Spread Factor = (510 % 1000) / 100000.0 = 0.00510
```

### Final Formula untuk `USD_BuySpread_IDR`

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```

Contoh:

```
Rate_USD dari API  = 0.000064   → 1 IDR = 0.000064 USD
1 / 0.000064       = 15625      → 1 USD = 15625 IDR
Spread Factor      = 0.00510
USD_BuySpread_IDR  = 15625 * (1 + 0.00510) = 15704.69
```

### Cara Verifikasi

```bash
# 1. Cek log aplikasi saat startup
# Cari baris: 🔐 Spread Calc: username='...', asciiSum=..., factor=...

# 2. Hitung manual via terminal
echo -n "your-github-username" | od -An -td1 | tr ' ' '\n' | grep -v '^$' | paste -sd+ | bc

# 3. Bandingkan dengan response API
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq '.rates.USD_BuySpread_IDR'
```

---

## ⚙️ Konfigurasi

File konfigurasi aplikasi: `src/main/resources/application.yml`

```yaml
server:
  port: 8080

frankfurter:
  base-url: https://api.frankfurter.dev/v1
  timeout: 5000
  historical:
    start: 2024-01-01
    end: 2024-01-05
    from: IDR
    to: USD

app:
  github-username: ${GITHUB_USERNAME}
```

| Property | Value | Keterangan |
|---|---|---|
| `server.port` | `8080` | Port aplikasi berjalan |
| `frankfurter.base-url` | `https://api.frankfurter.dev/v1` | Base URL Frankfurter API |
| `frankfurter.timeout` | `5000` | Timeout HTTP client (ms) |
| `frankfurter.historical.start` | `2024-01-01` | Default start date historical |
| `frankfurter.historical.end` | `2024-01-05` | Default end date historical |
| `frankfurter.historical.from` | `IDR` | Default base currency historical |
| `frankfurter.historical.to` | `USD` | Default target currency historical |
| `app.github-username` | `${GITHUB_USERNAME:your-username}` | Fill yourGitHub username |

---

## 🧪 Testing

### Run All Tests via Script

```bash
# Beri izin eksekusi
chmod +x test-api.sh

# Jalankan test script
./test-api.sh
```

### Manual Testing by cURL

```bash
# Health check
curl http://localhost:8080/api/finance/data/health

# Latest rates
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Historical (default)
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Historical (custom)
curl "http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-01-10&endDate=2024-01-12"

# Supported currencies
curl http://localhost:8080/api/finance/data/supported_currencies
```

### Run Unit Testing

```bash
# Run semua test
./mvnw test

# Run spesifik test class
./mvnw test -Dtest=SpreadCalculatorTest
./mvnw test -Dtest=DemoApplicationTests
```