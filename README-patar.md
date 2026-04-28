# Allo Bank Backend Developer Take-Home Test

## Author
**GitHub Username:** `patarebenezer`

## Spread Factor Calculation

| Step | Value |
|------|-------|
| Username | `patarebenezer` |
| Unicode Sum | `1384` |
| Sum % 1000 | `384` |
| **Spread Factor** | `0.00384` |

Formula: `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00384)`

---

## Setup & Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Application
```bash
mvn spring-boot:run
```
Aplikasi berjalan di port **8081**.

---

## Endpoint Usage

### 1. Latest IDR Rates
```bash
curl http://localhost:8081/api/finance/data/latest_idr_rates
```

### 2. Historical IDR/USD
```bash
curl http://localhost:8081/api/finance/data/historical_idr_usd
```

### 3. Supported Currencies
```bash
curl http://localhost:8081/api/finance/data/supported_currencies
```

### Run Tests
```bash
mvn test
```

---

## Architectural Rationale

### 1. Polymorphism: Kenapa Strategy Pattern?

Strategy Pattern dipilih karena memungkinkan setiap resource type memiliki
logika fetch dan transformasi sendiri dalam class terpisah, tanpa if/else
di controller. Menambah resource type baru cukup dengan membuat satu class
baru dan menambah satu baris di StrategyConfig — controller dan service
tidak perlu diubah sama sekali (Open/Closed Principle).

### 2. Client Factory: Kenapa FactoryBean?

FactoryBean<WebClient> memberikan tempat khusus untuk merakit WebClient
dengan konfigurasi kompleks: timeout, base URL dari config, dan logging
filter. Berbeda dengan @Bean biasa, FactoryBean memiliki lifecycle hook
afterPropertiesSet() untuk validasi sebelum bean diserahkan ke context,
dan sinyal ke developer bahwa konstruksi bean ini non-trivial.

### 3. Startup Runner: Kenapa ApplicationRunner?

ApplicationRunner berjalan setelah seluruh ApplicationContext selesai
diinisialisasi, memastikan WebClient dari FactoryBean sudah 100% siap.
@PostConstruct berjalan saat bean dibuat — terlalu awal dan berisiko
bean lain belum siap. ApplicationRunner juga memudahkan partial failure
handling: jika satu resource gagal di-fetch, aplikasi tetap bisa serve
resource lainnya.