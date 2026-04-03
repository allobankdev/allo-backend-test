## Ringkasan

PR ini berisi implementasi REST API untuk mengagregasi data nilai tukar IDR dari Frankfurter API sesuai dengan requirement.

Data yang disediakan mencakup:

* Kurs terbaru terhadap IDR
* Data historis IDR ke USD
* Daftar mata uang yang tersedia

Seluruh data diambil satu kali saat aplikasi start dan disimpan di memory, sehingga endpoint tidak melakukan request ulang ke API eksternal.

---

## Fitur yang Diimplementasikan

* Endpoint:

  * `GET /api/finance/data/{resourceType}`

* Resource yang didukung:

  * `latest_idr_rates`
  * `historical_idr_usd`
  * `supported_currencies`

* Penambahan field:

  * `USD_BuySpread_IDR` (khusus latest rate)

---

## Pendekatan Arsitektur

### Strategy Pattern

Digunakan untuk memisahkan logic pengambilan data berdasarkan resource.

Setiap resource memiliki implementasi sendiri dari interface `IDRDataFetcher`, sehingga tidak perlu menggunakan if/else di controller.

Pendekatan ini memudahkan jika ingin menambahkan resource baru ke depannya.

---

### FactoryBean untuk WebClient

WebClient dibuat menggunakan FactoryBean agar konfigurasi seperti base URL dan timeout terpusat.

Dengan cara ini, pembuatan client tidak tersebar di berbagai class dan lebih mudah dikelola.

---

### ApplicationRunner

Digunakan untuk load data dari external API saat aplikasi start.

Alasan penggunaan:

* memastikan data hanya diambil sekali
* menghindari pemanggilan API berulang
* memastikan semua dependency sudah siap sebelum proses fetch

---

## Response Design

Response dibuat konsisten menggunakan wrapper:

```json
[
  {
    "resourceType": "latest_idr_rates",
    "data": { ... }
  }
]
```

Pendekatan ini memudahkan parsing di sisi client dan menjaga konsistensi antar endpoint.

---

## Error Handling & Timeout

* Menambahkan timeout pada pemanggilan WebClient
* Menangani error dari external API secara graceful
* Menambahkan GlobalExceptionHandler untuk menangani error umum

Contoh response saat error:

```json
{
  "error": "Failed to fetch data",
  "message": "TimeoutException"
}
```

---

## Testing

Unit test mencakup:

* Strategy (latest, historical, currencies)
* SpreadUtil
* DataLoader

WebClient dimock agar test tidak bergantung ke API eksternal.

---

## Cara Menjalankan

```bash
mvn clean install
mvn spring-boot:run
```

---

## Contoh Penggunaan

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

## Personalisasi Spread

GitHub Username: **haidir**

Spread dihitung menggunakan:
(sum ASCII username % 1000) / 100000.0

---

## Catatan

* Data disimpan dalam bentuk immutable setelah load
* Menggunakan BigDecimal untuk menjaga presisi perhitungan
* Endpoint hanya membaca dari memory (tidak hit API lagi)

---

## Penutup

Implementasi ini dibuat dengan fokus pada clean architecture, maintainability, dan testability.

