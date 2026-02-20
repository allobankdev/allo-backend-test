# IDR Rate Aggregator Service

Aplikasi **Spring Boot REST API** untuk mengagregasi data nilai tukar **Indonesian Rupiah (IDR)** dari **Frankfurter Exchange Rate API** dengan pendekatan arsitektur yang bersih dan siap produksi.

---

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat
- Java 21
- Maven 3.8+
- Git


### Menjalankan Secara Lokal
```bash
  git clone https://github.com/Hafidhalmarogi07/allo-backend-test
  cd allo-backend-test
  mvn clean install
  mvn spring-boot:run
```
### Menjalankan Test
```bash
  mvn test
```
### 🔌 Penggunaan Endpoint API

Base endpoint:
```
  GET /api/finance/data/{resourceType}
```

### 1. Latest IDR Rates
```
   curl http://localhost:8080/api/finance/data/latest_idr_rates
```
### 2. Historical IDR ke USD
```
   curl http://localhost:8080/api/finance/data/historical_idr_usd
```
### 3. Daftar Mata Uang
```
   curl http://localhost:8080/api/finance/data/supported_currencies
```
### 🧮 Catatan Personalisasi

**GitHub Username**: Hafidhalmarogi07

**Perhitungan Spread Factor**:

```
 Spread Factor = (Jumlah nilai ASCII username % 1000) / 100000
```

* **Hasil Spread Factor: 0.00765**

Rumus yang digunakan:

```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
```

### 🛠️ Alasan Arsitektur
**1. Strategy Pattern**

Digunakan untuk menangani beberapa jenis resource (latest, historical, currencies) tanpa if-else atau switch.
Pendekatan ini membuat kode lebih mudah dikembangkan, dirawat, dan diuji.

**2. FactoryBean untuk API Client**

FactoryBean digunakan untuk membangun client API eksternal agar konfigurasi (base URL, timeout, dsb) terpusat dan lifecycle object lebih terkontrol dibandingkan @Bean biasa.

**3. ApplicationRunner**

ApplicationRunner digunakan untuk memuat data sekali saat aplikasi startup sehingga:

* Data siap sebelum endpoint diakses

* Tidak ada pemanggilan API eksternal berulang

* Lebih aman dibanding @PostConstruct

### ✅ Ringkasan

* Satu endpoint polimorfik

* Strategy Pattern tanpa conditional logic

* Data in-memory yang thread-safe dan immutable

* External API client via FactoryBean

* Inisialisasi data saat startup