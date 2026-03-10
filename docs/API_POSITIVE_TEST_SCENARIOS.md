# API Positive Test Scenarios

## Purpose

Dokumen ini berisi skenario test API positif berdasarkan requirement di `README.md`.
Fokus dokumen ini adalah memastikan endpoint utama memenuhi kebutuhan fungsional yang diminta oleh take-home test.

Base URL lokal saat ini:

```text
http://localhost:8900
```

Endpoint utama:

```text
GET /api/finance/data/{resourceType}
```

Nilai `resourceType` yang valid:

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

## Preconditions

Sebelum menjalankan skenario positif:

1. aplikasi berhasil startup tanpa error
2. `ApplicationRunner` berhasil preload data ke in-memory store
3. koneksi ke Frankfurter API tersedia saat startup
4. `app.github-username` sudah diisi pada `application.yml`

## Scenario 1: Get Latest IDR Rates

### Goal

Memastikan endpoint mengembalikan data latest rate berbasis IDR dari in-memory store dan menyertakan hasil kalkulasi `USD_BuySpread_IDR`.

### Request

```bash
curl --location 'http://localhost:8900/api/finance/data/latest_idr_rates'
```

### Expected HTTP Status

```text
200 OK
```

### Expected Assertions

- response berbentuk JSON array
- array berisi minimal 1 item
- item pertama memiliki `resourceType = "latest_idr_rates"`
- payload memiliki field `base`
- payload memiliki field `date`
- payload memiliki field `amount`
- payload memiliki field `rates`
- payload `rates` mengandung key `USD`
- payload memiliki field `USD_BuySpread_IDR`
- payload memiliki field `spreadFactor`
- nilai `USD_BuySpread_IDR` bertipe numerik
- nilai `spreadFactor` bertipe numerik
- nilai `USD_BuySpread_IDR` dihitung dari `USD` dan spread factor username GitHub

### Example Expected Shape

```json
[
  {
    "resourceType": "latest_idr_rates",
    "payload": {
      "base": "IDR",
      "date": "2024-01-05",
      "amount": 1.0,
      "rates": {
        "USD": 0.000064
      },
      "USD_BuySpread_IDR": 15744.531250,
      "spreadFactor": 0.00765
    }
  }
]
```

## Scenario 2: Get Historical IDR to USD Data

### Goal

Memastikan endpoint mengembalikan historical data untuk rentang tanggal yang diminta pada soal, dengan response tetap berupa unified JSON array.

### Request

```bash
curl --location 'http://localhost:8900/api/finance/data/historical_idr_usd'
```

### Expected HTTP Status

```text
200 OK
```

### Expected Assertions

- response berbentuk JSON array
- array berisi minimal 1 item
- item pertama memiliki `resourceType = "historical_idr_usd"`
- payload memiliki field `base`
- payload memiliki field `amount`
- payload memiliki field `rates`
- payload `rates` berbentuk map time-series
- payload `rates` minimal mengandung tanggal dalam rentang `2024-01-01` sampai `2024-01-05`
- pada tiap tanggal yang tersedia, terdapat key `USD`

### Example Expected Shape

```json
[
  {
    "resourceType": "historical_idr_usd",
    "payload": {
      "base": "IDR",
      "amount": 1.0,
      "rates": {
        "2024-01-01": {
          "USD": 0.000064
        },
        "2024-01-02": {
          "USD": 0.000064
        }
      }
    }
  }
]
```

## Scenario 3: Get Supported Currencies

### Goal

Memastikan endpoint mengembalikan daftar mata uang yang didukung dari resource `/currencies`, dengan format unified JSON array.

### Request

```bash
curl --location 'http://localhost:8900/api/finance/data/supported_currencies'
```

### Expected HTTP Status

```text
200 OK
```

### Expected Assertions

- response berbentuk JSON array
- array berisi minimal 1 item
- item pertama memiliki `resourceType = "supported_currencies"`
- payload berbentuk object/map
- payload mengandung minimal beberapa currency code umum seperti `USD`, `EUR`, atau `IDR`
- value dari payload adalah nama currency dalam bentuk string

### Example Expected Shape

```json
[
  {
    "resourceType": "supported_currencies",
    "payload": {
      "USD": "US Dollar",
      "IDR": "Indonesian Rupiah",
      "EUR": "Euro"
    }
  }
]
```

## Scenario 4: Response Uses Unified JSON Array for All Valid Resources

### Goal

Memastikan ketiga resource valid tetap mengikuti kontrak response yang sama seperti yang diminta pada README.

### Request Set

```bash
curl --location 'http://localhost:8900/api/finance/data/latest_idr_rates'
curl --location 'http://localhost:8900/api/finance/data/historical_idr_usd'
curl --location 'http://localhost:8900/api/finance/data/supported_currencies'
```

### Expected Assertions

- semua request valid mengembalikan `200 OK`
- semua response berbentuk JSON array
- setiap item array memiliki field:
  - `resourceType`
  - `payload`
- `resourceType` sesuai dengan nilai path variable yang diminta

## Scenario 5: Data Served from Startup-Loaded In-Memory Store

### Goal

Memastikan perilaku endpoint sesuai constraint bahwa data tidak diambil ulang dari external API pada setiap request.

### Test Method

Lakukan request yang sama dua kali setelah aplikasi berhasil startup:

```bash
curl --location 'http://localhost:8900/api/finance/data/latest_idr_rates'
curl --location 'http://localhost:8900/api/finance/data/latest_idr_rates'
```

### Expected Assertions

- kedua request mengembalikan `200 OK`
- response shape tetap konsisten
- tidak ada indikasi request baru ke external API saat endpoint dipanggil
- data dibaca dari cache/in-memory store yang sudah terisi saat startup

## Scenario 6: Strategy Routing Works for All Valid Resource Types

### Goal

Memastikan pemetaan `resourceType` ke strategy yang sesuai berjalan benar.

### Valid Inputs

- `latest_idr_rates`
- `historical_idr_usd`
- `supported_currencies`

### Expected Assertions

- masing-masing input valid mengembalikan data resource yang benar
- tidak terjadi cross-response
- `latest_idr_rates` tidak mengembalikan historical/currencies payload
- `historical_idr_usd` tidak mengembalikan latest/currencies payload
- `supported_currencies` tidak mengembalikan latest/historical payload

## Positive Test Summary Matrix

| No | Scenario | Resource Type | Expected Status | Focus |
|---|---|---|---|---|
| 1 | Get latest IDR rates | `latest_idr_rates` | `200` | latest data + spread calculation |
| 2 | Get historical IDR/USD | `historical_idr_usd` | `200` | historical time-series |
| 3 | Get supported currencies | `supported_currencies` | `200` | currencies master data |
| 4 | Unified JSON array contract | all valid values | `200` | consistent response shape |
| 5 | Served from in-memory store | `latest_idr_rates` | `200` | startup-loaded cache behavior |
| 6 | Strategy routing correctness | all valid values | `200` | correct polymorphic dispatch |

## Notes for Next Step

Setelah skenario positif ini, skenario negatif yang paling penting untuk dibuat adalah:

- invalid `resourceType`
- startup load gagal karena external API error
- data belum tersedia di in-memory store
- external API mengembalikan response tidak lengkap untuk perhitungan `USD_BuySpread_IDR`
