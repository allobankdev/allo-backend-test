# IDR Rate Aggregator

REST API berbasis Spring Boot yang mengagregasi data nilai tukar Rupiah (IDR)
dari [Frankfurter API](https://api.frankfurter.app) publik.

---

## 👤 Informasi

- **GitHub Username:** furqonfajri
- **Perhitungan Spread Factor:**
  - Username: `furqonfajri`
  - Jumlah nilai ASCII: f(102) + u(117) + r(114) + q(113) + o(111) + n(110) + f(102) + a(97) + j(106) + r(114) + i(105) = **1191**
  - Spread Factor: `(1191 % 1000) / 100000.0` = **0.00191**
  - Formula: `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00191)`

---

## 🚀 Cara Setup & Menjalankan Aplikasi

### Prasyarat

- Java 21
- Maven 3.9.11

### Clone & Build

```bash
git clone https://github.com/furqonfajri/idr-rate-aggregator.git
cd idr-rate-aggregator
./mvnw clean install -DskipTests
```

### Menjalankan Aplikasi

```bash
./mvnw spring-boot:run
```

Aplikasi akan berjalan di **http://localhost:8081**

Saat startup, ketiga data resource akan otomatis diambil dari Frankfurter API
dan disimpan di memori sebelum menerima request apapun.

### Menjalankan Tests

```bash
./mvnw test
```

---

## 📡 Penggunaan Endpoint

### Base URL

```
http://localhost:8081/api/finance/data/{resourceType}
```

### 1. Latest IDR Rates

```bash
curl http://localhost:8081/api/finance/data/latest_idr_rates
```

**Response:**

```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "date": "2026-04-02",
    "amount": 1.0,
    "base": "IDR",
    "rates": {
      "USD": 5.9e-5,
      "EUR": 5.1e-5
    },
    "USD_BuySpread_IDR": 16981.525423728814
  },
  "timestamp": "2026-04-06T11:39:37.278"
}
```

### 2. Historical IDR/USD Rates

```bash
curl http://localhost:8081/api/finance/data/historical_idr_usd
```

**Response:**

```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "amount": 1.0,
    "base": "IDR",
    "start_date": "2023-12-29",
    "end_date": "2024-01-05",
    "rates": {
      "2024-01-02": { "USD": 6.4e-5 },
      "2024-01-03": { "USD": 6.4e-5 }
    }
  },
  "timestamp": "2026-04-06T11:40:24.570"
}
```

### 3. Supported Currencies

```bash
curl http://localhost:8081/api/finance/data/supported_currencies
```

**Response:**

```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "USD": "United States Dollar",
    "IDR": "Indonesian Rupiah",
    "EUR": "Euro"
  },
  "timestamp": "2026-04-06T11:41:00.568"
}
```

### 4. Resource Type Tidak Valid (400 Bad Request)

```bash
curl http://localhost:8081/api/finance/data/invalid_type
```

**Response:**

```json
{
  "success": false,
  "message": "Invalid resourceType: 'invalid_type'. Valid values: [historical_idr_usd, latest_idr_rates, supported_currencies]",
  "data": null,
  "timestamp": "2026-04-06T11:41:41.054"
}
```

---

## 🏗️ Alasan Arsitektur

### 1. Polimorphism — Mengapa Strategy Pattern?

Strategy Pattern dipilih dibandingkan blok `if/else` atau `switch` biasa dengan alasan berikut:

**Ekstensibilitas:** Menambahkan resource type baru hanya membutuhkan pembuatan class baru yang mengimplementasikan interface `IDRDataFetcher` dan diberi anotasi `@Component`. Tidak ada kode yang sudah ada yang perlu dimodifikasi — ini mengikuti prinsip Open/Closed.

**Kemudahan Perawatan:** Setiap class fetcher memiliki satu tanggung jawab. `LatestIDRRatesFetcher` hanya menangani logika rates terbaru, `HistoricalIDRUsdFetcher` hanya menangani data historis, dan seterusnya. Hal ini membuat kode lebih mudah dibaca, diuji, dan di-debug secara independen.

**Tidak Ada Logika Kondisional di Controller:** Spring secara otomatis meng-inject semua bean `IDRDataFetcher` ke dalam `Map` dengan key dari `getResourceType()`. Controller cukup melakukan map lookup — tanpa `if/else` atau `switch` sama sekali, membuat kode lebih bersih dan tidak rentan terhadap kesalahan.

---

### 2. Client Factory — Mengapa FactoryBean dibanding @Bean biasa?

Penggunaan `FactoryBean<WebClient>` memberikan beberapa keuntungan dibanding method `@Bean` standar:

**Enkapsulasi Logika Konstruksi yang Kompleks:** `WebClientFactoryBean` mengenkapsulasi semua detail konstruksi WebClient — konfigurasi timeout, penanganan redirect, filter logging, dan injeksi base URL — dalam satu class yang kohesif, bukan sebuah method di dalam configuration class.

**Desain OOP yang Lebih Baik:** `FactoryBean` adalah sebuah interface, artinya factory itu sendiri adalah Spring-managed bean dengan lifecycle-nya sendiri. Dapat diuji secara independen, di-subclass, atau diganti tanpa menyentuh konfigurasi lain.

**Pemisahan Concern:** Configuration class seharusnya mendeklarasikan bean apa yang ada, bukan bagaimana objek kompleks dibangun. `FactoryBean` memisahkan concern "cara membangun" ke dalam class tersendiri yang berdedikasi, sesuai dengan prinsip Single Responsibility.

---

### 3. Startup Runner — Mengapa ApplicationRunner dibanding @PostConstruct?

**ApplicationRunner berjalan setelah ApplicationContext sepenuhnya siap:** `@PostConstruct` berjalan saat inisialisasi bean, sebelum context sepenuhnya dimulai. Artinya bean lain seperti WebClient dan properties mungkin belum sepenuhnya terinisialisasi, berisiko menyebabkan `NullPointerException` atau konfigurasi yang tidak lengkap.

**Penanganan Error yang Lebih Baik:** Jika `ApplicationRunner.run()` melempar exception, Spring Boot akan menghentikan startup secara graceful dan mencatat pesan kegagalan yang jelas. Kegagalan `@PostConstruct` terkadang bisa tertelan atau menyebabkan error startup yang tidak jelas.

**Akses ke Application Arguments:** `ApplicationRunner` menerima `ApplicationArguments`, sehingga lebih fleksibel untuk kebutuhan masa depan seperti mengaktifkan/menonaktifkan pemuatan data startup melalui command-line flag.

**Pemisahan yang Lebih Bersih:** `ApplicationRunner` adalah komponen berdedikasi dengan satu tujuan yang jelas — menjalankan logika setelah startup. `@PostConstruct` mencampur logika inisialisasi ke dalam lifecycle method bean, yang lebih sulit diuji dan dipahami.

---

## 🗂️ Struktur Project

```
src/
├── main/java/com/allobank/idr_rate_aggregator/
│   ├── config/
│   │   ├── WebClientFactoryBean.java
│   │   ├── FrankfurterProperties.java
│   │   └── SpreadProperties.java
│   ├── controller/
│   │   └── FinanceController.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── LatestRatesResponse.java
│   │   ├── HistoricalRatesResponse.java
│   │   └── CurrenciesResponse.java
│   ├── model/
│   │   └── FinanceData.java
│   ├── strategy/
│   │   ├── IDRDataFetcher.java
│   │   ├── LatestIDRRatesFetcher.java
│   │   ├── HistoricalIDRUsdFetcher.java
│   │   └── SupportedCurrenciesFetcher.java
│   ├── store/
│   │   └── FinanceDataStore.java
│   ├── runner/
│   │   └── DataInitializerRunner.java
│   └── service/
│       └── FinanceService.java
└── test/java/com/allobank/idr_rate_aggregator/
    ├── strategy/
    │   ├── LatestIDRRatesFetcherTest.java
    │   ├── HistoricalIDRUsdFetcherTest.java
    │   └── SupportedCurrenciesFetcherTest.java
    └── runner/
        └── DataInitializerRunnerTest.java
```
