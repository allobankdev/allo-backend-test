# Allo Bank Backend Test — Polymorphic Finance API (Frankfurter)

Implementasi Spring Boot untuk tes mandiri Backend Allo Bank: **1 endpoint polymorphic** yang menyajikan data kurs dari **Frankfurter API** (public, tanpa API key) dengan fokus pada **IDR**. Data diambil **sekali saat startup** lalu disajikan dari **in-memory immutable storage**.

## Tech Stack
- Java 17
- Spring Boot 3.5.x
- Spring Web + Spring WebFlux (WebClient)
- Lombok
- JUnit 5 (unit test)

---

## Fitur Utama (Sesuai Soal)
1. **Satu endpoint internal**:
   - `GET /api/finance/data/{resourceType}`

2. `resourceType` yang didukung:
   - `latest_idr_rates` → Frankfurter: `/latest?base=IDR` + transform `"USD_BuySpread_IDR"`
   - `historical_idr_usd` → Frankfurter: `/2024-01-01..2024-01-05?from=IDR&to=USD`
   - `supported_currencies` → Frankfurter: `/currencies`

3. **Strategy Pattern**:
   - Interface `IDRDataFetcher`
   - 3 implementasi strategi (Latest, Historical, Supported)
   - Controller memilih strategi via **Map injection** (bean name = resourceType), tanpa switch-case manual untuk memilih strategi

4. **FactoryBean untuk WebClient**:
   - WebClient dibuat oleh `FactoryBean<WebClient>` (`WebClientFactory`)
   - Base URL dan timeout diambil dari `application.yml`

5. **Preload sekali saat startup + immutable storage**
   - Data diambil menggunakan `ApplicationRunner`
   - Data disimpan di `DataStorageService` dalam bentuk **immutable snapshot** (thread-safe)

6. **Unit test**
   - Unit test untuk ketiga strategi `IDRDataFetcher` (tanpa network real)

---

## Konfigurasi (`application.yml`)
Pastikan konfigurasi seperti ini (contoh):

```yml
spring:
  application:
    name: allobank

server:
  port: 8080

frankfurter:
  api:
    base-url: https://api.frankfurter.app
    connect-timeout: 5000
    read-timeout: 5000

github:
  username: RiskyAdit06
```

---

## Cara Menjalankan
### 1) Jalankan test
```bash
mvn test
```

### 2) Jalankan aplikasi
```bash
mvn spring-boot:run
```

Aplikasi akan berjalan di:
- `http://localhost:8080`

---

## Endpoint
### 1) Latest IDR Rates + Spread
`GET /api/finance/data/latest_idr_rates`

- Mengambil data dari: `GET https://api.frankfurter.app/latest?base=IDR`
- Menambahkan field computed: `"USD_BuySpread_IDR"` (lihat rumus di bawah)
- Response: **array JSON** unified (List of items)

### 2) Historical IDR→USD
`GET /api/finance/data/historical_idr_usd`

- Mengambil data dari:
  `GET https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD`
- Transformasi minimal
- Response: **array JSON** unified

> Catatan: Frankfurter terkadang mengembalikan `start_date` yang bisa lebih awal dari tanggal yang di-request (karena ketersediaan data). Namun request yang dipakai tetap sesuai soal.

### 3) Supported Currencies
`GET /api/finance/data/supported_currencies`

- Mengambil data dari: `GET https://api.frankfurter.app/currencies`
- Transformasi minimal
- Response: **array JSON** unified

---

## Format Response (Unified Array)
Semua resourceType menghasilkan array item dengan struktur umum:

```json
[
  {
    "resourceType": "latest_idr_rates",
    "key": "USD",
    "value": 0.000060,
    "meta": {
      "date": "2025-12-17",
      "base": "IDR"
    }
  }
]
```

- `resourceType`: jenis data
- `key`: identifier (currency code / date / computed field)
- `value`: nilai utama (rate / string nama mata uang / computed value)
- `meta`: metadata tambahan (date, base, spreadFactor, dll.)

---