📌 Deskripsi

Aplikasi ini adalah REST API berbasis Spring Boot untuk mengagregasi data nilai tukar mata uang dari API publik Frankfurter. Sistem ini menggunakan pendekatan Strategy Pattern, FactoryBean, dan ApplicationRunner untuk memastikan arsitektur yang scalable dan maintainable.
---

# ⚙️ 1. Setup & Run Instructions

## 🔹 Clone Repository

```bash
git clone https://github.com/cahyamaullna/allo-backend-test.git
cd REPO
```

## 🔹 Build Project

```bash
mvn clean install
```

## 🔹 Jalankan Aplikasi

```bash
mvn spring-boot:run
```

## 🔹 Menjalankan Unit Test

```bash
mvn test
```

---

# 🌐 2. Endpoint Usage

Base URL:

```bash
http://localhost:8080/api/finance/data
```

---

## 🔹 1. Latest IDR Rates

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
```

---

## 🔹 2. Historical IDR → USD

```bash
curl http://localhost:8080/api/finance/data/historical_idr_usd
```

---

## 🔹 3. Supported Currencies

```bash
curl http://localhost:8080/api/finance/data/supported_currencies
```

---

# 🧠 3. Personalization Note (Spread Factor)

## 🔹 GitHub Username

```text
cahyamaullna
```

## 🔹 Perhitungan

Total ASCII:

```text
1264
```

Rumus:

```text
Spread Factor = (1264 % 1000) / 100000
              = 0.00264
```

---

## 🔹 Formula Final

```text
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00264)
```


---

# 🏗️ Arsitektur Singkat

* Strategy Pattern → handling multiple resource tanpa if/else
* FactoryBean → konfigurasi WebClient terpusat
* ApplicationRunner → load data saat startup
* In-memory immutable store → performa tinggi & thread-safe

---

# ✅ Catatan

* Data hanya di-load sekali saat startup
* API eksternal tidak dipanggil setiap request
* Pastikan koneksi internet aktif saat pertama run

---

# 👨‍💻 Author

Nama: Muhamad Cahya Maulana
GitHub: https://github.com/cahyamaullna
