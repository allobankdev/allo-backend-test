# Allo Backend Test - IDR Rate Aggregator

Aplikasi Spring Boot untuk mengagregasi data kurs mata uang dari [Frankfurter API](https://www.frankfurter.app/). Aplikasi ini mengambil data kurs terbaru (dengan kalkulasi *spread* khusus), riwayat kurs historis, dan daftar mata uang yang didukung saat startup, kemudian menyimpannya dalam *in-memory store*.

## Teknologi Utama
- Java 17
- Spring Boot 3.4.1
- Spring WebFlux (WebClient untuk konsumsi API eksternal)
- Lombok
- Maven

## Fitur
1. **Latest IDR Rates**: Mengambil kurs terbaru dengan basis IDR dan menghitung `USD_BuySpread_IDR` menggunakan faktor spread dinamis berdasarkan *username* GitHub.
2. **Historical IDR to USD**: Mengambil data kurs historis IDR ke USD untuk periode awal Januari 2024.
3. **Supported Currencies**: Menyediakan daftar seluruh mata uang yang didukung oleh sistem.
4. **Startup Data Loading**: Menggunakan `ApplicationRunner` untuk memuat data dari API luar saat aplikasi dijalankan, memastikan data siap sebelum permintaan API pertama masuk.

## Prasyarat
- Java Development Kit (JDK) 17 atau versi yang lebih baru.
- Apache Maven 3.6 atau versi yang lebih baru.

## Langkah-langkah Menjalankan Aplikasi

### 1. Clone Repository
Buka terminal atau command prompt, lalu jalankan perintah berikut:
```bash
git clone -b feat/idr-rate-aggregator https://github.com/irhendra09/allo-backend-test.git
cd allo-backend-test
```

### 2. Konfigurasi (Opsional)
Konfigurasi `src/main/resources/application.properties` (atau gunakan environment variables):
```properties
# Base URL untuk API Frankfurter
app.api.base-url=https://api.frankfurter.app
# Username GitHub digunakan untuk kalkulasi faktor spread
app.github.username=irhendra09
```

### 3. Build Aplikasi
Gunakan Maven untuk melakukan build dan mengunduh dependensi:
```bash
mvn clean install
```

### 4. Jalankan Aplikasi
Anda dapat menjalankan aplikasi langsung menggunakan Maven:
```bash
mvn spring-boot:run
```
Atau dengan menjalankan file JAR yang telah di-build:
```bash
java -jar target/allo-backend-test-0.0.1-SNAPSHOT.jar
```

Aplikasi akan berjalan di `http://localhost:8080`.

## Dokumentasi API

### Ambil Data Keuangan
Endpoint ini mengembalikan data keuangan berdasarkan `resourceType` yang diminta dari *in-memory store*.

- **URL:** `/api/finance/data/{resourceType}`
- **Method:** `GET`
- **Path Variables:**
  - `latest_idr_rates`: Mendapatkan kurs terbaru IDR ke berbagai mata uang dengan kalkulasi *USD Buy Spread*.
  - `historical_idr_usd`: Mendapatkan riwayat kurs IDR ke USD (Januari 2024).
  - `supported_currencies`: Mendapatkan daftar mata uang yang didukung.

#### 1. Contoh Request (Latest Rates)
```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```
**Response Sukses:**
```json
{
  "amount": 1.0,
  "base": "IDR",
  "date": "2024-03-14",
  "rates": {
    "AUD": 0.000084,
    "BRL": 0.00031,
    "CAD": 0.000081,
    "CHF": 0.000047,
    "USD": 0.000064
  },
  "USD_BuySpread_IDR": 16406.25
}
```

#### 2. Contoh Request (Historical Rates)
```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```
**Response Sukses:**
```json
[
  {
    "date": "2023-12-29",
    "rate_USD": 0.000065
  },
  {
    "date": "2024-01-02",
    "rate_USD": 0.000064
  }
]
```

#### 3. Contoh Request (Supported Currencies)
```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```
**Response Sukses:**
```json
[
  {
    "code": "AUD",
    "name": "Australian Dollar"
  },
  {
    "code": "BRL",
    "name": "Brazilian Real"
  }
]
```

## Kalkulasi Spread Factor
Aplikasi ini menggunakan algoritma khusus untuk menentukan biaya tambahan (*spread*) pada kurs USD berdasarkan username GitHub yang dikonfigurasi:

1.  **Username**: `irhendra09` (default).
2.  **Algoritma**: Menjumlahkan seluruh nilai ASCII dari setiap karakter username (case-insensitive), lalu dihitung dengan rumus: `(Total_ASCII % 1000) / 100000.0`.
3.  **Nilai untuk `irhendra09`**:
    - Total ASCII: `105 + 114 + 104 + 101 + 110 + 100 + 114 + 97 + 48 + 57 = 950`.
    - **Spread Factor**: `(950 % 1000) / 100000.0` = **0.0095**.
4.  **Implementasi**: Nilai ini ditambahkan ke kurs asli (`1 / rate_usd`) untuk menghasilkan `USD_BuySpread_IDR`.

## Struktur Proyek
- `config/`: Konfigurasi `WebClient` dan Bean lainnya.
- `controller/`: REST endpoint.
- `exception/`: Penanganan error global dan kustom.
- `runner/`: Logika inisialisasi data saat startup.
- `service/`: Penyimpanan data in-memory.
- `strategy/`: Implementasi pola *Strategy* untuk pengambilan data API yang berbeda.
- `util/`: Utilitas kalkulasi (seperti spread factor).
