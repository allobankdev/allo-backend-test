# Technical Flow

## Purpose

Dokumen ini menjelaskan alur teknis aplikasi dari request yang masuk ke controller sampai data dikembalikan ke client, termasuk flow startup yang menjadi inti dari soal take-home ini.

## High-Level Architecture

Komponen utama:

- `FinanceDataController`: entry point HTTP
- `FinanceDataService`: application boundary untuk akses data per resource
- `InMemoryFinanceDataStore`: penyimpanan immutable in-memory
- `StartupDataLoader`: preload semua data saat startup
- `IDRDataFetcher` implementations: strategy untuk setiap resource type
- `FrankfurterClient`: adapter ke external API
- `FrankfurterClientFactoryBean`: factory khusus untuk membangun HTTP client

## Startup Flow

Saat aplikasi Spring Boot dijalankan:

1. Spring melakukan bootstrap context.
2. `FrankfurterProperties` dan `AppProperties` dibaca dari `application.yml`.
3. `FrankfurterClientFactoryBean` membuat satu instance `RestClient` yang sudah memiliki:
   - base URL Frankfurter
   - default header `Accept: application/json`
   - timeout awal
4. `FrankfurterClient` menerima `RestClient` tersebut sebagai dependency.
5. Semua implementasi `IDRDataFetcher` diregistrasikan sebagai Spring bean:
   - `LatestIdrRatesFetcher`
   - `HistoricalIdrUsdFetcher`
   - `SupportedCurrenciesFetcher`
6. `StartupDataLoader` dieksekusi sebagai `ApplicationRunner`.
7. `StartupDataLoader` memanggil semua strategy satu kali saat startup.
8. Masing-masing strategy melakukan fetch ke external API melalui `FrankfurterClient`.
9. Hasil raw response ditransformasikan menjadi `List<FinanceDataItem>`.
10. Semua hasil digabung ke map berdasarkan `resourceType`.
11. `InMemoryFinanceDataStore` menyimpan hasil akhir dengan atomic replacement.

Setelah langkah ini selesai, endpoint tidak perlu lagi memanggil external API.

## Request Flow

Untuk request:

```http
GET /api/finance/data/{resourceType}
```

alur teknisnya:

1. Request masuk ke `FinanceDataController`.
2. Controller meneruskan `resourceType` ke `FinanceDataService`.
3. `DefaultFinanceDataService` memvalidasi apakah `resourceType` terdaftar di map strategy.
4. Jika tidak terdaftar, service melempar `ResourceTypeNotFoundException`.
5. Jika terdaftar, service mengambil data dari `InMemoryFinanceDataStore`.
6. Store mengembalikan data immutable yang sudah di-load saat startup.
7. Controller membungkus hasil ke `ResponseEntity.ok(...)`.
8. Spring meng-serialize response menjadi JSON array.

## Strategy Flow Detail

### 1. `LatestIdrRatesFetcher`

Flow:

1. Call `/latest?base=IDR`
2. Terima response rates terbaru
3. Ambil nilai `USD` dari map `rates`
4. Hitung `spreadFactor` dari username GitHub via `SpreadFactorCalculator`
5. Hitung `USD_BuySpread_IDR` dengan formula soal
6. Return satu `FinanceDataItem` dengan payload gabungan raw data + calculated fields

### 2. `HistoricalIdrUsdFetcher`

Flow:

1. Call endpoint historical range
2. Ambil data time-series IDR ke USD
3. Bungkus ke payload unified
4. Return satu `FinanceDataItem`

### 3. `SupportedCurrenciesFetcher`

Flow:

1. Call `/currencies`
2. Ambil seluruh currency symbol dan display name
3. Bungkus ke payload unified
4. Return satu `FinanceDataItem`

## Error Handling Flow

Error utama ditangani oleh `GlobalExceptionHandler`.

### `ResourceTypeNotFoundException`

- Terjadi jika path variable tidak cocok dengan resource yang didukung
- Response: `404 Not Found`

### `ExternalApiException`

- Terjadi jika external API gagal saat proses fetch
- Dalam desain saat ini, error paling relevan muncul saat startup loading
- Response: `502 Bad Gateway`

### `DataNotInitializedException`

- Terjadi jika data belum tersedia di store
- Response: `502 Bad Gateway`

## Why This Flow Fits the Test

Desain ini cocok dengan requirement soal karena:

- endpoint hanya satu
- pemilihan perilaku resource menggunakan Strategy Pattern
- client external dibuat lewat `FactoryBean`
- fetch dilakukan sekali saat startup
- request setelah startup hanya membaca dari memory
- store menggunakan atomic replacement dan immutable snapshot

## Suggested Improvement Before Final Submission

- Tambahkan logging terstruktur pada startup load dan external API failure
- Rapikan response contract agar lebih konsisten untuk tiga resource
- Tambahkan OpenAPI examples untuk setiap resource secara terpisah
- Tambahkan integration test untuk error path dan unsupported `resourceType`
