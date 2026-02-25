# Finance API – Allo Bank Take-Home Test

> **Oleh:** Andry Ramadhan
> **Role:** Backend Developer Candidate

---

## 📋 Deskripsi Proyek

REST API berbasis Spring Boot yang mengagregasi data nilai tukar Rupiah Indonesia (IDR) dari [Frankfurter Exchange Rate API](https://api.frankfurter.app/) publik. Seluruh data diambil **sekali saat startup** dan disimpan di **in-memory store yang thread-safe dan immutable**, sehingga setiap request HTTP dilayani dari cache — tanpa panggilan eksternal per request.

---

## 🔧 Prasyarat

| Tool     | Versi Minimum                                              |
| -------- | ---------------------------------------------------------- |
| Java     | 21                                                         |
| Maven    | 3.9+                                                       |
| Internet | Diperlukan saat startup (untuk fetch data Frankfurter API) |

---

## 🚀 Cara Menjalankan

### 1. Clone & Masuk ke Direktori

```bash
git clone <url-repository>
cd finance
```

### 2. Build Proyek

```bash
./mvnw clean package -DskipTests
```

### 3. Jalankan Aplikasi

```bash
./mvnw spring-boot:run
```

Atau dengan JAR yang sudah dibuild:

```bash
java -jar target/finance-0.0.1-SNAPSHOT.jar
```

Aplikasi akan berjalan di **http://localhost:8080**.

Saat startup, Anda akan melihat log seperti:

```
=== DataIngestionRunner: Memulai inisialisasi data dari Frankfurter API ===
=== DataIngestionRunner: Data berhasil dimuat dalam XXX ms ===
```

### 4. Jalankan Tests

```bash
# Unit tests saja (tanpa koneksi internet)
./mvnw test -Dgroups=unit

# Semua test (termasuk integration test, membutuhkan internet)
./mvnw test
```

---

## 📡 Penggunaan Endpoint

**Base URL:** `http://localhost:8080`

### 1. Kurs IDR Terbaru (dengan Spread Perbankan)

```bash
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq .
```

**Contoh Respons:**

```json
{
  "status": "success",
  "message": "Data berhasil diambil",
  "data": [
    {
      "resourceType": "latest_idr_rates",
      "data": {
        "amount": 1.0,
        "base": "IDR",
        "date": "2025-02-25",
        "rates": {
          "USD": 0.000062,
          "EUR": 0.000058,
          "...": "..."
        },
        "USD_BuySpread_IDR": 16138.23
      }
    }
  ]
}
```

### 2. Data Historis IDR → USD (2024-01-01 s.d. 2024-01-05)

```bash
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq .
```

**Contoh Respons:**

```json
{
  "status": "success",
  "message": "Data berhasil diambil",
  "data": [
    {
      "resourceType": "historical_idr_usd",
      "data": {
        "amount": 1.0,
        "base": "IDR",
        "start_date": "2024-01-01",
        "end_date": "2024-01-05",
        "rates": {
          "2024-01-02": { "USD": 0.000064 },
          "2024-01-03": { "USD": 0.000064 },
          "2024-01-04": { "USD": 0.000065 },
          "2024-01-05": { "USD": 0.000064 }
        }
      }
    }
  ]
}
```

### 3. Daftar Mata Uang yang Didukung

```bash
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq .
```

**Contoh Respons:**

```json
{
  "status": "success",
  "message": "Data berhasil diambil",
  "data": [
    {
      "resourceType": "supported_currencies",
      "data": {
        "AUD": "Australian Dollar",
        "EUR": "Euro",
        "IDR": "Indonesian Rupiah",
        "USD": "US Dollar",
        "...": "..."
      }
    }
  ]
}
```

### 4. Resource Type Tidak Dikenal (404)

```bash
curl -s http://localhost:8080/api/finance/data/unknown | jq .
```

```json
{
  "status": "error",
  "httpCode": 404,
  "message": "Resource type tidak ditemukan: 'unknown'. Nilai yang valid: latest_idr_rates, historical_idr_usd, supported_currencies"
}
```

---

## 🔢 Personalisasi: GitHub Username & Spread Factor

| Item                     | Nilai                                        |
| ------------------------ | -------------------------------------------- |
| **GitHub Username**      | `ramandry12`                                 |
| **Karakter (lowercase)** | `r a m a n d r y 1 2`                        |
| **Nilai Unicode**        | 114+97+109+97+110+100+114+121+49+50          |
| **Total Unicode Sum**    | **961**                                      |
| **Kalkulasi**            | `(961 % 1000) / 100000.0` = `961 / 100000.0` |
| **Spread Factor**        | **0.00961**                                  |

**Formula USD_BuySpread_IDR:**

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00961)
```

Contoh jika `Rate_USD = 0.000064` (IDR/USD dari API):

```
= (1 / 0.000064) * 1.00961
= 15625.0 * 1.00961
= 15775.16
```

---

## 🏛️ Alasan Arsitektur (Architectural Rationale)

### 1. Polymorphism Justification – Strategy Pattern

**Mengapa Strategy Pattern, bukan conditional block?**

Strategy Pattern digunakan untuk memisahkan _logika penanganan setiap resource type_ ke dalam kelas-kelas terpisah yang memiliki tanggung jawab tunggal (Single Responsibility Principle). Berikut perbandingannya:

| Aspek                | Conditional (if/else)                          | Strategy Pattern                                                      |
| -------------------- | ---------------------------------------------- | --------------------------------------------------------------------- |
| **Extensibility**    | Setiap resource type baru = ubah kode yang ada | Tambah kelas baru, tanpa menyentuh kelas lama (Open/Closed Principle) |
| **Maintainability**  | Logika semua resource tercampur satu file      | Setiap resource punya kelas sendiri, mudah dicari & dipahami          |
| **Testability**      | Harus mock semua cabang sekaligus              | Setiap strategi di-test secara independen dan terisolasi              |
| **Controller Layer** | Controller tahu detail setiap resource         | Controller hanya tahu interface, buta terhadap implementasi           |

Spring mendukung Strategy Pattern secara native: semua bean yang mengimplementasikan `IDRDataFetcher` otomatis dikumpulkan ke dalam `Map<String, IDRDataFetcher>` berdasarkan nama bean-nya (`@Component("latest_idr_rates")`). Controller cukup melakukan lookup `map.get(resourceType)` — tidak ada `if/else` atau `switch` sama sekali.

### 2. Client Factory – FactoryBean

**Mengapa FactoryBean, bukan `@Bean` biasa?**

`FactoryBean<WebClient>` dipilih karena memberikan kontrol penuh atas _lifecycle_ dan _konfigurasi_ pembuatan client:

- **Separation of Concerns:** Seluruh logika konfigurasi client (timeout, base URL, header default) terenkapsulasi dalam satu kelas yang didedikasikan, bukan tersebar di `@Configuration`.
- **Externalizable Config:** `FrankfurterProperties` diinjeksikan ke `FactoryBean`, memastikan Base URL dan timeout selalu dibaca dari `application.yml` — tidak ada nilai hardcoded.
- **Singleton Guarantee:** `isSingleton() = true` memastikan satu instance `WebClient` dipakai ulang di seluruh aplikasi, efisien secara resource.
- **Testability:** `WebClientFactoryBean` dapat diinstansiasi dan dikonfigurasi secara independen dalam test tanpa perlu menjalankan Spring context.

Dibanding `@Bean` biasa yang hanya mengembalikan objek langsung, `FactoryBean` memungkinkan logika konstruksi yang lebih kompleks dan dapat dikontrol penuh oleh programmer.

### 3. Startup Runner Choice – ApplicationRunner vs @PostConstruct

**Mengapa ApplicationRunner, bukan `@PostConstruct`?**

| Aspek                 | `@PostConstruct`                                                     | `ApplicationRunner`                                              |
| --------------------- | -------------------------------------------------------------------- | ---------------------------------------------------------------- |
| **Timing**            | Dipanggil selama inisialisasi bean (sebelum context refresh selesai) | Dipanggil setelah context refresh _penuh_ – semua bean siap      |
| **Dependency Safety** | Berisiko race condition jika ada dependensi yang belum siap          | Semua dependensi pasti sudah terbuat dan terkonfigurasi          |
| **Error Handling**    | Exception bisa menyebabkan bean gagal dibuat saja                    | Kegagalan langsung menghentikan startup – lebih eksplisit        |
| **Testability**       | Sulit di-mock dalam `@SpringBootTest`                                | Mudah diverifikasi via integration test dengan `@SpringBootTest` |
| **Args Access**       | Tidak ada akses ke command-line args                                 | Dapat mengakses `ApplicationArguments` jika diperlukan           |

Dengan `ApplicationRunner`, kita yakin bahwa saat endpoint pertama kali menerima request, `FinanceDataStore` _pasti_ sudah terisi. Kegagalan `run()` yang menyebabkan exception secara eksplisit menghentikan startup, mencegah aplikasi berjalan dalam kondisi data kosong.

---

## 📁 Struktur Proyek

```
src/main/java/com/allobank/finance/
├── FinanceApplication.java              # Entry point
├── config/
│   ├── FrankfurterProperties.java       # @ConfigurationProperties
│   ├── WebClientFactoryBean.java        # FactoryBean<WebClient> (Constraint B)
│   └── AppConfig.java                   # Mendaftarkan FactoryBean
├── controller/
│   └── FinanceController.java           # GET /api/finance/data/{resourceType}
├── exception/
│   ├── ExternalApiException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── ApiResponse.java
│   ├── FinanceDataResult.java           # Java Record (immutable)
│   ├── HistoricalRateResponse.java
│   └── LatestRateResponse.java
├── runner/
│   └── DataIngestionRunner.java         # ApplicationRunner (Constraint C)
├── service/
│   └── FinanceDataService.java          # Orchestrator
├── store/
│   └── FinanceDataStore.java            # Thread-safe store (Constraint C)
└── strategy/
    ├── IDRDataFetcher.java              # Strategy Interface (Constraint A)
    └── impl/
        ├── LatestIdrRatesFetcher.java   # + USD_BuySpread_IDR calculation
        ├── HistoricalIdrUsdFetcher.java
        └── SupportedCurrenciesFetcher.java

src/test/java/com/allobank/finance/
├── FinanceApplicationTests.java
├── runner/
│   └── DataIngestionRunnerIntegrationTest.java
└── strategy/
    ├── LatestIdrRatesFetcherTest.java
    ├── HistoricalIdrUsdFetcherTest.java
    └── SupportedCurrenciesFetcherTest.java
```
