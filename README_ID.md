# Solusi Technical Test Backend Developer Allo Bank

Repositori ini berisi implementasi Spring Boot untuk technical test dengan menggunakan data API Frankfurter dan satu endpoint polimorfik.

## Stack

- Java 17
- Spring Boot 3.3.5
- Maven
- JUnit 5 + Mockito

## Setup dan Menjalankan Aplikasi

1. Clone repositori

```bash
git clone <url-fork-anda>
cd allo-backend-test
```

2. Jalankan aplikasi

```bash
mvn spring-boot:run
```

3. Jalankan test

```bash
mvn test
```

## Penggunaan Endpoint

Endpoint utama:

```text
GET /api/finance/data/{resourceType}
```

Nilai `resourceType` yang didukung:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

Contoh perintah cURL:

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

## Catatan Personalisasi

- Username GitHub yang digunakan: `intikom`
- Total nilai ASCII: `763`
- Formula Spread Factor: `(763 % 1000) / 100000.0`
- Nilai Spread Factor: `0.00763`

Resource `latest_idr_rates` menambahkan field berikut:

```text
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```

## Ringkasan Arsitektur

- Interface strategy `IDRDataFetcher` dengan 3 implementasi konkret:
  - `LatestIdrRatesFetcher`
  - `HistoricalIdrUsdFetcher`
  - `SupportedCurrenciesFetcher`
- `IDRDataFetcherRegistry` membangun lookup map strategy yang di-inject oleh Spring.
- `FinanceDataPreloadRunner` (`ApplicationRunner`) mengambil ketiga resource tepat satu kali saat startup.
- `FinanceDataStore` menyimpan data in-memory yang immutable dan hanya diinisialisasi sekali menggunakan `AtomicReference`.
- `RestTemplateFactoryBean` membuat client `RestTemplate` Frankfurter melalui `FactoryBean<T>`.
- `FinanceDataController` hanya menyajikan data in-memory dari `GET /api/finance/data/{resourceType}`.

## Alasan Arsitektural

1. Justifikasi Polimorfisme

Strategy Pattern memisahkan perilaku pengambilan data per resource ke kelas masing-masing dan mencegah pertumbuhan logika kondisional di controller/service. Saat resource baru ditambahkan, kita cukup menambah kelas strategy baru dan mendaftarkannya melalui Spring, sehingga lebih mudah dikembangkan serta dipelihara.

2. Client Factory

`FactoryBean<RestTemplate>` memusatkan proses pembuatan/konfigurasi client (root URL, timeout) dalam satu komponen khusus. Ini memberi kontrol lifecycle/pembuatan yang jelas dan memisahkan concern konstruksi client dari business logic, sekaligus memenuhi requirement untuk tidak menggunakan pembuatan client `@Bean` sederhana.

3. Pemilihan Startup Runner

`ApplicationRunner` lebih tepat dibanding `@PostConstruct` karena dieksekusi setelah inisialisasi context dan wiring dependency selesai. Pendekatan ini lebih aman untuk orkestrasi startup, lebih jelas dari sisi operasional, dan lebih mudah diuji pada integration test.

## Konfigurasi

`src/main/resources/application.yml`:

```yaml
frankfurter:
  api:
    base-url: https://api.frankfurter.app
    historical-range: 2024-01-01..2024-01-05
    github-username: intikom
    connect-timeout-millis: 5000
    read-timeout-millis: 5000
```

Ubah `github-username` jika diperlukan; nilai spread factor akan otomatis mengikuti nilai tersebut.
