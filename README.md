# Allo Backend Test — Finance Aggregator Service

Dokumen ini dibuat untuk menjelaskan desain arsitektur, komponen utama, alur data, dan keputusan teknikal dalam pembuatan API agregator ini.  
Aplikasi ini bertugas mengambil data dari Frankfurter API, mengolah sesuai aturan, menyimpannya ke in-memory store, lalu mengekspose-nya melalui endpoint:
### GET /api/finance/data/{resourceType}

---

## 1. Supported Resource Types

| ResourceType               | Description                           |
|----------------------------|---------------------------------------|
| `latest_idr_rates`         | Kurs hari ini dengan IDR sebagai base |
| `historical_idr_usd`       | Data historis IDR terhadap USD        |
| `supported_currencies`     | List Valas Tersedia                   |

---

## 2. High-Level Architecture
                      ┌─────────────────────────┐
                      │ Frankfurter API         │
                      └───────────┬─────────────┘
                                  │ RestClient
                                  ▼
    ┌────────────────────────────────────────────────────────┐
    │                    Strategy Factory                    │
    │  LatestRateStrategy                                    │
    │  DateQueryStrategy                                     │
    │  CurrenciesStrategy                                    │
    └────────────────────────┬───────────────────────────────┘
                             ▼           
                   FinanceDataPreloader   
                             │
                             │
                             ▼
                   FinanceDataStore (Cache)
                             ▲
                             │
                      FinanceDataService
                             ▲
                             │
                  FinanceDataController


---

## 3. Component Summary

### 3.1 FactoryBean (RestClient)
- Bertugas membuat bean RestClient **tanpa `@Bean`**.
- Memakai base URL + timeout dari `application.yml`.
- Digunakan oleh semua Strategy.

### 3.2 Strategy Pattern
Setiap `resourceType` mempunyai strategy terpisah:
- `LatestRateStrategy`
- `DateQueryStrategy`
- `CurrenciesStrategy`

Pemilihan strategy dilakukan oleh `StrategyRegistry`, **bukan oleh controller**.

### 3.3 Preloader (ApplicationRunner)
- Berjalan saat startup aplikasi.
- Memanggil semua strategy.
- Menyimpan hasil data ke `AgregatorDataStore`.

### 3.4 In-Memory Store (`AgregatorDataStore`)
Cache menggunakan `EnumMap<ResourceType, Object>`:
- Aman meskipun aplikasi menjalankan banyak thread (Thread-safe)
- data yang tidak dapat diubah oleh flow aplikasi (Immutable snapshot)
- Data hanya diisi pada startup

### 3.5 Validator
`ResourceTypeValidator` memastikan bahwa input resourceType ada di enum.  
Jika tidak, akan throw Exception.

---

## 4. API Documentation

### Endpoint

### Path Parameters

| Name         | Type   | Required | Description                         |
|--------------|--------|----------|-------------------------------------|
| resourceType | String | Yes      | Must be one of the supported enums  |

### Examples

#### `latest_idr_rates`
```json
{
  "USD_BuySpread_IDR": 16693.0000033386,
  "amount": 1.0,
  "base": "IDR",
  "date": "2025-11-21T00:00:00.000Z",
  "rates": {
    "AUD": 0.000093,
    "BGN": 0.0001,
    "BRL": 0.00032,
    "CAD": 0.000084,
    "CHF": 0.000048,
    "CNY": 0.00043,
    "CZK": 0.00126,
    "DKK": 0.00039,
    "EUR": 0.000052,
    "GBP": 0.000046,
    "HKD": 0.00047,
    "HUF": 0.01991,
    "ILS": 0.0002,
    "INR": 0.00536,
    "ISK": 0.00764,
    "JPY": 0.00938,
    "KRW": 0.08819,
    "MXN": 0.0011,
    "MYR": 0.00025,
    "NOK": 0.00061,
    "NZD": 0.00011,
    "PHP": 0.00352,
    "PLN": 0.00022,
    "RON": 0.00026,
    "SEK": 0.00057,
    "SGD": 0.000078,
    "THB": 0.00194,
    "TRY": 0.00254,
    "USD": 0.000060,
    "ZAR": 0.00104
  }
}
```

#### `/historical_idr_usd`
```json
{
  "amount": 1.0,
  "base": "IDR",
  "end_date": "2024-01-05T00:00:00.000Z",
  "rates": {
    "2023-12-29T00:00:00.000Z": {
      "USD": 0.000065
    },
    "2024-01-02T00:00:00.000Z": {
      "USD": 0.000064
    },
    "2024-01-03T00:00:00.000Z": {
      "USD": 0.000064
    },
    "2024-01-04T00:00:00.000Z": {
      "USD": 0.000064
    },
    "2024-01-05T00:00:00.000Z": {
      "USD": 0.000064
    }
  },
  "start_date": "2023-12-29T00:00:00.000Z"
}
```

#### `/supported_currencies`
```json
{
  "AUD": "Australian Dollar",
  "BGN": "Bulgarian Lev",
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

## 5. Spread Factor Calculation
Spread Factor dihitung berdasarkan username GitHub:

### Jumlahkan ASCII dari seluruh karakter username.
```spreadFactor = (sum % 1000) / 100000```

### Digunakan untuk menghitung:
```USD_BuySpread_IDR = (1 / USD_rate) * (1 + spreadFactor)```

## 6. Configuration
Cek ke application.yml untuk melihat value yang dibutuhkan aplikasi

## 7. Scripts

### Jalankan Aplikasi
```
mvn clean install
java -jar target/finance-aggregator.jar
```

### cUrl Command
- ```curl -X GET "http://localhost:8080/api/finance/data/latest_idr_rates"```
- ```curl -X GET "http://localhost:8080/api/finance/data/historical_idr_usd"```
- ```curl -X GET "http://localhost:8080/api/finance/data/supported_currencies"```

## 8. Unit Tests
Dilakukan dengan membuat test file untuk setiap komponen utama dari aplikasi, yaitu:
- Controller (package controller)
- Calculation (package helper)
- Service (package service)
- strategy (package strategy)

## 9. Architecture Rationale
### 9.1. Strategy Pattern
Strategy Design Pattern memungkinkan code yang lebih clean, tidak banyak if-else di dalam controller. 
Poin utamanya adalah setiap strategy berdiri sendiri, tidak bergantung kepada strategy lain.
Ini menjadikan code yang sesuai dengan prinsip "Open-closed Principle", dimana kode terbuka untuk pengembangan, namun tertutup untuk modifikasi.
Dan Strategy Design Pattern juga sejalan dengan prinsip "Single Responsibility Principle" dimana setiap kode hanya mempunyai sebuah responsibility
(dalam kasus ini, handle setiap resource type yang diminta downstream).

### 9.2. Factory Bean
Sejauh yang saya baca, keutamaan factory bean adalah menjadikan kode yang bersih dan siap untuk di-inject.
Terbuka untuk diskusi perihal ini :)

### 9.3. Preloading + In-memory store
Dikarenakan aplikasi ini adalah aplikasi yang meminta data yang sama, maka preloading + in-memory store menjadi solusi terbaik untuk penyimpanan.
Aplikasi tidak perlu hit external setiap kali API di-hit. Cukup hit sekali saat inisiasi, dan data disimpan di memory untuk membuat pengambilan data menjadi lebih efisien.
Apabila ada pengembangan dikemudian hari untuk membuat lebih banyak data yang parameterized (tanggal, nama valas, jumlah uang, dll.), saya akan menimbang untuk data diambil menjadi setiap hari saat tengah malam dengan cron job, alih-alih hanya sekali saat inisiasi.

### 10. Possible Enhancement
- Salah satu yang akan saya lakukan adalah seperti yang saya bilang di poin 9.3 mengenai preload yang diubah menjadi scheduled update di tengah malam.
- Tidak menutup kemungkinan untuk membuatnya menjadi lebih canggih lagi dengan Redis, walau mungkin jadi overkill :).

### 11. Author
Made with Love.
sincerely, `ChikoHakles`