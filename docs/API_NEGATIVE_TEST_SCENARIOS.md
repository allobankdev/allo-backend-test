# API Negative Test Scenarios

## Purpose

Dokumen ini berisi skenario test API negatif berdasarkan requirement di `README.md` dan desain error handling aplikasi saat ini.
Fokusnya adalah memastikan aplikasi gagal dengan cara yang terkontrol, jelas, dan konsisten.

Base URL lokal saat ini:

```text
http://localhost:8900
```

Endpoint utama:

```text
GET /api/finance/data/{resourceType}
```

## Main Negative Areas from Requirements

Berdasarkan `README.md`, area negatif yang paling penting adalah:

- `resourceType` tidak valid
- external API gagal saat proses startup load
- data tidak tersedia di in-memory store
- data external tidak lengkap untuk kebutuhan kalkulasi `latest_idr_rates`
- aplikasi tidak memenuhi kontrak bahwa fetch hanya terjadi saat startup

## Scenario 1: Invalid Resource Type

### Goal

Memastikan request dengan `resourceType` yang tidak didukung menghasilkan response error yang benar.

### Request

```bash
curl --location 'http://localhost:8900/api/finance/data/invalid_type'
```

### Expected HTTP Status

```text
404 Not Found
```

### Expected Assertions

- response bukan JSON array sukses
- response berbentuk object error
- field `status` bernilai `404`
- field `error` bernilai `Not Found`
- field `message` menjelaskan bahwa `resourceType` tidak didukung
- field `path` berisi `/api/finance/data/invalid_type`

### Example Expected Shape

```json
{
  "timestamp": "2026-03-10T10:15:30Z",
  "status": 404,
  "error": "Not Found",
  "message": "Unsupported resource type: invalid_type",
  "path": "/api/finance/data/invalid_type"
}
```

## Scenario 2: External API Fails During Startup Load

### Goal

Memastikan aplikasi menangani kegagalan dari Frankfurter API saat `ApplicationRunner` melakukan preload data.

### Failure Trigger Examples

- koneksi internet mati saat startup
- host Frankfurter tidak bisa di-resolve
- Frankfurter mengembalikan `4xx` atau `5xx`
- timeout saat request startup

### Expected Application Behavior

- startup preload gagal
- exception dibungkus sebagai `ExternalApiException`
- aplikasi tidak memiliki data valid di in-memory store
- endpoint tidak boleh memberikan response sukses palsu dengan data kosong yang menyesatkan

### Expected Assertions

- aplikasi gagal startup atau menandai data tidak tersedia
- tidak ada kondisi di mana endpoint mengembalikan data seolah-olah sukses padahal preload gagal
- error yang muncul tetap jelas untuk troubleshooting

### Notes

Untuk integration test, skenario ini biasanya divalidasi dengan mock client yang melempar exception saat `StartupDataLoader` berjalan.

## Scenario 3: Data Not Initialized in In-Memory Store

### Goal

Memastikan ketika data untuk suatu `resourceType` belum tersedia di store, endpoint mengembalikan error yang tepat.

### Trigger Condition

- preload belum berhasil mengisi store
- atau key resource tidak ada di store

### Request Example

```bash
curl --location 'http://localhost:8900/api/finance/data/latest_idr_rates'
```

### Expected HTTP Status

```text
502 Bad Gateway
```

### Expected Assertions

- response berbentuk error object
- field `status` bernilai `502`
- field `error` bernilai `Bad Gateway`
- field `message` menjelaskan bahwa data belum diinisialisasi untuk resource tersebut
- field `path` berisi path request yang dipanggil

### Example Expected Shape

```json
{
  "timestamp": "2026-03-10T10:15:30Z",
  "status": 502,
  "error": "Bad Gateway",
  "message": "Data is not initialized for resource type: latest_idr_rates",
  "path": "/api/finance/data/latest_idr_rates"
}
```

## Scenario 4: Latest IDR Rates Missing USD Value

### Goal

Memastikan sistem tetap terkontrol ketika external latest rates response tidak memiliki nilai `USD`, padahal field tersebut dibutuhkan untuk menghitung `USD_BuySpread_IDR`.

### Trigger Condition

- external API mengembalikan `rates`, tetapi key `USD` tidak ada
- atau key `USD` ada tetapi nilainya `null`
- atau nilainya `0`

### Expected Assertions

- aplikasi tidak crash dengan error aritmatika yang tidak tertangani
- perilaku fallback harus konsisten dan terdokumentasi
- jika desain memilih fallback value, response tetap valid dan jelas
- jika desain memilih error, error harus eksplisit dan dapat ditrack

### Current Design Note

Implementasi saat ini pada `LatestIdrRatesFetcher` cenderung mengembalikan `USD_BuySpread_IDR = 0` ketika `USD` tidak tersedia atau bernilai nol.
Karena itu, negative test untuk skenario ini harus memastikan fallback tersebut memang konsisten dan tidak menghasilkan exception tersembunyi.

## Scenario 5: Historical Response Missing Expected Time-Series Range

### Goal

Memastikan aplikasi tidak silently menyatakan data historical valid jika response external tidak sesuai rentang soal.

### Trigger Condition

- response historical kosong
- tanggal yang dikembalikan tidak ada dalam rentang `2024-01-01..2024-01-05`
- map tanggal ada, tetapi tidak mengandung `USD`

### Expected Assertions

- aplikasi tidak crash
- response atau test assertion harus mendeteksi bahwa data tidak memenuhi requirement
- skenario ini minimal harus tertangkap di unit/integration test walaupun endpoint tetap `200`

### Notes

Ini penting karena requirement README spesifik meminta penggunaan range contoh tersebut.
Jadi walaupun API technical call berhasil, secara requirement bisnis hasilnya tetap bisa dianggap gagal.

## Scenario 6: Supported Currencies Response Invalid or Empty

### Goal

Memastikan aplikasi menangani response `/currencies` yang kosong atau tidak valid.

### Trigger Condition

- response body kosong
- response body `null`
- response body tidak dapat di-deserialize menjadi map currency

### Expected Assertions

- aplikasi tidak mengembalikan response sukses yang misleading
- jika preload gagal karena deserialization atau client error, harus dipetakan ke jalur error yang jelas
- jika map kosong dianggap valid secara teknis, test harus tetap menandai bahwa requirement bisnis belum terpenuhi

## Scenario 7: Repeated Request Must Not Trigger Re-fetch to External API

### Goal

Memastikan endpoint tidak melanggar constraint bahwa external API dipanggil hanya sekali saat startup.

### Trigger Method

1. startup aplikasi
2. panggil endpoint yang sama beberapa kali
3. amati apakah client external dipanggil lagi

### Expected Assertions

- repeated request tidak memicu call baru ke Frankfurter API
- repeated request hanya membaca dari in-memory store
- jika ada mocking/spying di integration test, verify external client dipanggil hanya saat startup

### Why This Is Negative

Jika endpoint memanggil external API lagi saat request berlangsung, maka itu adalah pelanggaran requirement arsitektur walaupun response mungkin tetap `200`.

## Scenario 8: FactoryBean or Client Misconfiguration

### Goal

Memastikan misconfiguration pada external client tidak menghasilkan perilaku yang diam-diam salah.

### Trigger Examples

- `frankfurter.base-url` kosong
- path endpoint salah
- timeout sangat kecil sehingga request selalu gagal

### Expected Assertions

- aplikasi gagal secara jelas saat startup load atau saat inisialisasi client
- error cukup informatif untuk troubleshooting
- tidak ada kondisi sukses palsu

## Negative Test Summary Matrix

| No | Scenario | Expected Outcome | Focus |
|---|---|---|---|
| 1 | Invalid resource type | `404` | unsupported input |
| 2 | External API startup failure | startup fails or data unavailable | robustness |
| 3 | Data not initialized in store | `502` | in-memory readiness |
| 4 | Latest rates missing USD | controlled fallback or explicit error | spread calculation safety |
| 5 | Historical range invalid/missing | detected as requirement failure | data completeness |
| 6 | Supported currencies invalid/empty | controlled failure or flagged invalid | data integrity |
| 7 | Endpoint re-fetches external API | test must fail | architecture constraint |
| 8 | Factory/client misconfiguration | clear startup/config failure | configuration correctness |

## Recommended Next Coding Targets

Setelah dokumen ini, test code yang paling prioritas untuk diimplementasikan adalah:

1. controller/service integration test untuk invalid `resourceType`
2. startup loader integration test untuk external API failure
3. unit test `LatestIdrRatesFetcher` untuk `USD` null atau nol
4. verification bahwa repeated request tidak memanggil client external lagi
