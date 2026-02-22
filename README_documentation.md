[.mvn](../../allo-backend-test/.mvn)# AlloBank Finance API


AlloBank Finance API menyediakan data kurs dan mata uang menggunakan integrasi **Frankfurter API**.  
API ini memiliki **satu pintu utama** (`/data/{resourceType}`) yang bisa diakses dengan `resourceType` berbeda.  
Ini memungkinkan API **dynamic routing** menggunakan **Strategy Pattern**.
<br><br>
- `latest_idr_rates` → Kurs USD terbaru dalam IDR dengan spread personal berdasarkan GitHub username.
- `historical_idr_usd` → Data historis kurs IDR ↔ USD.
- `supported_currencies` → Daftar mata uang yang didukung.

---

## 🗂 Resource Types & Sample URLs

| Resource Type           | Service Class                        | Sample URL                                                                 | Description |
|-------------------------|-------------------------------------|---------------------------------------------------------------------------|-------------|
| latest_idr_rates         | `LatestIdrRatesServiceImpl`          | `http://localhost:8080/api/finance/data/latest_idr_rates`                  | Kurs USD terbaru dalam IDR dengan spread personal |
| historical_idr_usd       | `HistoricalIDRUSDServiceImpl`        | `http://localhost:8080/api/finance/data/historical_idr_usd?startDate=2024-01-01&endDate=2024-01-31` | Data historis kurs IDR ↔ USD |
| supported_currencies     | `SupportedCurrenciesServiceImpl`     | `http://localhost:8080/api/finance/data/supported_currencies`              | Daftar mata uang yang didukung |

> **Catatan:** Semua resource diakses melalui **satu endpoint utama** `/data/{resourceType}`, lalu service yang sesuai dipanggil berdasarkan `resourceType` menggunakan **Strategy Pattern**.

## ⚙️ Base Configuration

**Context Path & Port**

```properties
spring.application.name=allobank-finance
server.servlet.context-path=/api/finance
server.port=8080

base.url.frankfurter=https://api.frankfurter.dev/v1
url.latest.base=${base.url.frankfurter}/latest
url.currencies=${base.url.frankfurter}/currencies

github.username=herdiansyah5197
