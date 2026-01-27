# Allo Bank Backend Developer Take-Home Test

**Kandidat:** Hendro Wunga (GitHub: `hendwunga`)

**Posisi:** Backend Developer – Technical Test

## Objektif Proyek

Proyek ini adalah REST API berbasis Spring Boot yang siap produksi untuk mengagregasi data keuangan Rupiah (IDR) dari API publik Frankfurter. Proyek ini mendemonstrasikan konsep Spring tingkat lanjut, kejelasan arsitektur melalui *design patterns*, dan manajemen data *in-memory* yang *thread-safe*.

---

## Rasional Arsitektur

### 1. Strategy Pattern (Polimorfisme)

Alih-alih menggunakan blok kondisional (`if-else` atau `switch`) pada lapisan *service*, proyek ini menerapkan **Strategy Design Pattern**.

* **Open/Closed Principle:** Sumber data baru dapat ditambahkan dengan membuat kelas strategi baru tanpa mengubah *controller*.
* **Resolusi Dinamis:** *Controller* menentukan strategi yang sesuai saat *runtime* menggunakan pencarian berbasis *Map* yang diinjeksi oleh Spring (`Map<String, IDRDataFetcher>`).

### 2. Client Factory (Implementasi FactoryBean)

Instans `WebClient` dikelola melalui kelas kustom `WebClientFactoryBean` yang mengimplementasikan `FactoryBean<T>` milik Spring.

* **Enkapsulasi:** Memusatkan konfigurasi infrastruktur (base URL, *timeout*, *header*).
* **Konfigurasi Bersih:** Pendekatan ini lebih unggul dibandingkan `@Bean` standar untuk pembuatan objek yang kompleks dan abstraksi infrastruktur.

### 3. Ingesti Data saat Startup (ApplicationRunner)

Data ditarik **tepat satu kali** saat aplikasi dijalankan menggunakan `ApplicationRunner` dan disimpan dalam penyimpanan *in-memory*.

* **Siklus Hidup Terprediksi:** Memastikan *context* aplikasi telah sepenuhnya siap sebelum eksekusi, sehingga lebih aman dibandingkan `@PostConstruct`.
* **Immutability (Imutabilitas):** Penyimpanan menggunakan `ConcurrentHashMap` dan dibungkus dalam tampilan `unmodifiableMap` setelah pemuatan data selesai untuk menjamin integritas data selama aplikasi berjalan.

---
## Architecture Diagram

Diagram berikut menggambarkan alur data dan interaksi antar komponen utama dalam sistem, mulai dari proses inisialisasi saat startup hingga penyajian data melalui REST API.

                          ┌──────────────────────────┐
                          │  Frankfurter Public API  │
                          │  (/latest, /currencies,  │
                          │   /2024-01-01..05)       │
                          └──────────────▲───────────┘
                                         │
                                         │ HTTP (WebClient)
                                         │
                         ┌───────────────┴───────────────┐
                         │   WebClientFactoryBean         │
                         │  (creates configured client)  │
                         └───────────────▲───────────────┘
                                         │
         ┌───────────────────────────────┴───────────────────────────────┐
         │                         Strategies                                │
         │  ┌────────────────────┐ ┌────────────────────┐ ┌────────────┐ │   |
         │  │ LatestIdrRates      │ │ HistoricalIdrUsd    │ │ Currencies │   │
         │  │ Strategy            │ │ Strategy            │ │ Strategy   │   │
         │  └───────────▲────────┘ └───────────▲────────┘ └──────▲───────┘   │
         │              │                       │                  │         │
         └──────────────┼───────────────────────┼──────────────────┼─────────┘
                        │                       │                  │
                        │ used by               │                  │
                        ▼                       ▼                  ▼
                 ┌────────────────────────────────────────────────────────┐
                 │                    ApplicationRunner                   │
                 │         (fetches all resources at startup)             │
                 └───────────────────────────▲────────────────────────────┘
                                             │
                                             │ stores
                                             ▼
                              ┌─────────────────────────────┐
                              │     FinanceDataStorage      │
                              │ (ConcurrentHashMap + lock)  │
                              └──────────────▲──────────────┘
                                             │
                                             │ reads
                                             ▼
                                ┌────────────────────────┐
                                │     REST Controller    │
                                │ /api/finance/data/{x}  │
                                └────────────────────────┘

---

## Kalkulasi Spread yang Dipersonalisasi

Diterapkan pada *resource* `latest_idr_rates` untuk menghasilkan kolom `USD_BuySpread_IDR`.

* **GitHub Username:** `hendwunga`
* **Logika Kalkulasi:**
* Jumlah nilai ASCII: `h(104)+e(101)+n(110)+d(100)+w(119)+u(117)+n(110)+g(103)+a(97) = 951`
* **Rumus Spread Factor:** `(Total % 1000) / 100000.0`
* **Hasil Faktor:** `0.00951` (Nilai saat *runtime* mungkin menyesuaikan berdasarkan deteksi *username* lingkungan sistem).


* **Rumus Akhir:** `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)`

---

## Penggunaan API

**Endpoint:** `GET /api/finance/data/{resourceType}`

| Resource Type | Deskripsi | Sumber Data (Frankfurter) |
| --- | --- | --- |
| `latest_idr_rates` | Kurs terbaru + kalkulasi **USD Buy Spread**. | `/latest?base=IDR` |
| `historical_idr_usd` | Data histori IDR ke USD untuk **01-01-2024 s/d 05-01-2024**. | `/2024-01-01..2024-01-05` |
| `supported_currencies` | Daftar semua **simbol mata uang** yang didukung. | `/currencies` |

### Perintah cURL:

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## Response API

### 1. Latest IDR Rates + Spread

**Request**

```http
GET http://localhost:8080/api/finance/data/latest_idr_rates
```

**Response**

```json
{
  "originalData": {
    "amount": 1.0,
    "base": "IDR",
    "date": "2026-01-26",
    "rates": {
      "AUD": 8.6E-5,
      "BRL": 3.2E-4,
      "CAD": 8.2E-5,
      "CHF": 4.6E-5,
      "CNY": 4.1E-4,
      "CZK": 0.00122,
      "DKK": 3.8E-4,
      "EUR": 5.0E-5,
      "GBP": 4.4E-5,
      "HKD": 4.6E-4,
      "HUF": 0.01923,
      "ILS": 1.9E-4,
      "INR": 0.00547,
      "ISK": 0.00732,
      "JPY": 0.00919,
      "KRW": 0.08629,
      "MXN": 0.00104,
      "MYR": 2.4E-4,
      "NOK": 5.8E-4,
      "NZD": 1.0E-4,
      "PHP": 0.00353,
      "PLN": 2.1E-4,
      "RON": 2.6E-4,
      "SEK": 5.4E-4,
      "SGD": 7.6E-5,
      "THB": 0.00186,
      "TRY": 0.00259,
      "USD": 6.0E-5,
      "ZAR": 9.6E-4
    }
  },
  "usdBuySpreadIdr": 16682.833333333332,
  "spreadFactor": 9.7E-4
}
```

---

### 2. Historical IDR → USD

**Request**

```http
GET http://localhost:8080/api/finance/data/historical_idr_usd
```

**Response**

```json
{
  "amount": 1.0,
  "base": "IDR",
  "start_date": "2023-12-29",
  "end_date": "2024-01-05",
  "rates": {
    "2023-12-29": { "USD": 6.5E-5 },
    "2024-01-02": { "USD": 6.4E-5 },
    "2024-01-03": { "USD": 6.4E-5 },
    "2024-01-04": { "USD": 6.4E-5 },
    "2024-01-05": { "USD": 6.4E-5 }
  }
}
```

---

### 3. Supported Currencies

**Request**

```http
GET http://localhost:8080/api/finance/data/supported_currencies
```

**Response**

```json
{
  "AUD": "Australian Dollar",
  "BRL": "Brazilian Real",
  "CAD": "Canadian Dollar",
  "CHF": "Swiss Franc",
  "CNY": "Chinese Renminbi Yuan",
  "CZK": "Czech Koruna",
  "DKK": "Danish Krone",
  "EUR": "Euro",
  "GBP": "British Pound",
  "HKD": "Hong Kong Dollar",
  "HUF": "Hungarian Forint",
  "IDR": "Indonesian Rupiah",
  "ILS": "Israeli New Sheqel",
  "INR": "Indian Rupee",
  "ISK": "Icelandic Króna",
  "JPY": "Japanese Yen",
  "KRW": "South Korean Won",
  "MXN": "Mexican Peso",
  "MYR": "Malaysian Ringgit",
  "NOK": "Norwegian Krone",
  "NZD": "New Zealand Dollar",
  "PHP": "Philippine Peso",
  "PLN": "Polish Złoty",
  "RON": "Romanian Leu",
  "SEK": "Swedish Krona",
  "SGD": "Singapore Dollar",
  "THB": "Thai Baht",
  "TRY": "Turkish Lira",
  "USD": "United States Dollar",
  "ZAR": "South African Rand"
}
```

---

## Error Handling

API ini dirancang dengan mekanisme error handling terpusat menggunakan Spring `@ControllerAdvice`.

Jenis error yang dikembalikan:

| HTTP Status | Kondisi |
|-------------|----------|
| **404 Not Found** | `resourceType` tidak dikenal atau tidak terdaftar pada strategy map |
| **503 Service Unavailable** | Gagal mengambil data dari Frankfurter API saat startup |
| **500 Internal Server Error** | Kesalahan internal yang tidak terduga |

Contoh response error:

```json
{
  "timestamp": "2026-01-27T07:49:53.491534609Z",
  "message": "Resource not found: Resource type 'latest_idr_rate' is not supported.",
  "error": "NOT_FOUND",
  "status": 404
}
```

---

## Cakupan Pengujian

### Unit Tests

* **Strategy Tests:** Memvalidasi transformasi data, kalkulasi *spread*, dan *mocking* API eksternal untuk semua implementasi strategi.
* **Spread Calculator Test:** Memastikan jumlah ASCII dan derivasi faktor dilakukan dengan akurat.

### Integration Tests

* **Startup Data Initialization Test (WireMock-based):**
  Menggunakan **WireMock** untuk mensimulasikan seluruh endpoint Frankfurter API (`/latest`, `/currencies`, `/{date-range}`), sehingga:

    - Aplikasi **tidak melakukan HTTP call ke internet** saat testing.
    - Proses `ApplicationRunner` dapat diuji secara end-to-end (client → strategy → storage).
    - Data dipastikan **berhasil dimuat sekali saat startup** dan tersimpan di `FinanceDataStorage`.
    - Test bersifat **deterministik, cepat, dan bebas flakiness jaringan**.

  Test ini memverifikasi bahwa ketiga resource berikut tersedia sebelum aplikasi siap melayani request:

    - `latest_idr_rates`
    - `historical_idr_usd`
    - `supported_currencies`

---

## Memulai (Getting Started)

Aplikasi ini menggunakan **Maven Wrapper**, sehingga Anda tidak perlu menginstal Maven secara manual. Cukup pastikan **Java 17** sudah terpasang.

### 1. Clone Repository

Pastikan Git sudah terinstal, lalu jalankan:

```bash
git clone https://github.com/hendwunga/allo-backend-test.git
cd allo-backend-test
```
### 2. Prasyarat

* **Java:** 17 (LTS)
* **Sistem Operasi:** Linux, Windows, atau macOS

### 3. Build & Run (Instruksi Lintas Platform)

#### **Di Linux / macOS:**

Buka terminal dan jalankan:

```bash
# Menjalankan Testing
./mvnw clean test

# Menjalankan Aplikasi
./mvnw spring-boot:run

```

#### **Di Windows (Command Prompt / PowerShell):**

Buka CMD atau PowerShell di folder proyek dan jalankan:

```bash
# Menjalankan Testing
mvnw.cmd clean test

# Menjalankan Aplikasi
mvnw.cmd spring-boot:run

```
---
