# AlloBank Finance API (Take-Home)

Implementasi Spring Boot sesuai spesifikasi:
- **1 endpoint**: `GET /api/finance/data/{resourceType}`
- **Strategy Pattern** (3 strategy): `latest_idr_rates`, `historical_idr_usd`, `supported_currencies`
- **WebClient dibuat via `FactoryBean<WebClient>`** (bukan `@Bean`)
- **Fetch data sekali saat startup** via `ApplicationRunner`
- **In-memory store immutable & thread-safe** (initialized exactly once)

## Prasyarat
- Java **17**
- Maven 3.x

## Cara Menjalankan

1) Edit konfigurasi:
`src/main/resources/application.yml`

Pastikan:
- `github.username` diisi dengan **GitHub username kamu**
- `frankfurter.base-url` tetap `https://api.frankfurter.app`

2) Run:
```bash
mvn spring-boot:run
```

Aplikasi jalan di:
- `http://localhost:8080`

## Endpoint

### 1) Latest IDR Rates + Spread
```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq
```

### 2) Historical IDR→USD (2024-01-01..2024-01-05)
```bash
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq
```

### 3) Supported Currencies
```bash
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq
```

## Personalization (Spread Factor)
Spread Factor dihitung dari GitHub username (lowercase):
1. Hitung jumlah nilai ASCII semua karakter username.
2. `Spread Factor = (sum % 1000) / 100000.0`

Formula output:
`USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)`

> Rate_USD diambil dari Frankfurter `/latest?base=IDR` (nilai `rates.USD`)

## Testing

Jalankan test:
```bash
mvn test
```

Yang diuji:
- Unit test 3 strategy (transformasi data + spread)
- Integration test memastikan `ApplicationRunner` menginisialisasi in-memory store saat startup

## Architectural Rationale (Ringkas)

### 1) Kenapa Strategy Pattern?
Karena endpoint punya 3 *resourceType* dengan aturan berbeda. Strategy membuat kode:
- mudah ditambah (misal resource baru) tanpa menyentuh controller
- terpisah dan teruji per resource (single responsibility)
- lebih maintainable daripada if/else panjang di service/controller

### 2) Kenapa FactoryBean untuk WebClient?
FactoryBean cocok untuk:
- membuat objek client yang butuh konfigurasi khusus (baseUrl, timeout)
- memastikan client dibuat melalui mekanisme Spring lifecycle yang konsisten
- memisahkan konfigurasi client dari class konfigurasi biasa (`@Bean` method dilarang oleh soal)

### 3) Kenapa ApplicationRunner?
ApplicationRunner berjalan setelah Spring context siap, sehingga:
- dependency injection sudah lengkap
- aman untuk proses startup ingestion (lebih jelas daripada `@PostConstruct`)
- cocok untuk memuat data sekali lalu dipakai read-only dari in-memory store
