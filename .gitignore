# IDR Finance Aggregator Service

Tugas teknis ini mengimplementasikan layanan aggregator keuangan menggunakan **Spring Boot** untuk mengambil dan menyimpan data kurs mata uang dari **Frankfurter API** secara *In-Memory*.

---

### 👤 Identitas Pengembang
* **Nama:** Mohamad Basuki
* **WhatsApp/Telp:** 081295332140
* **Username GitHub:** [warungedijakarta-bot](https://github.com/warungedijakarta-bot)
* **Spread Factor:** `0.00854` (0.854%)

---

### 🚀 Fitur Utama
* **In-Memory Caching:** Optimalisasi performa dengan menyimpan data di `ConcurrentHashMap` saat startup menggunakan `ApplicationRunner`.
* **Strategy Design Pattern:** Struktur kode yang bersih untuk memisahkan logika pengambilan data (*Latest*, *Historical*, dan *Currencies*).
* **FactoryBean:** Konfigurasi `WebClient` yang terpusat untuk efisiensi koneksi ke API eksternal.

---

### 🛠 Cara Menjalankan
1.  **Clone Repository:**
    ```bash
    git clone [https://github.com/warungedijakarta-bot/idr-aggregator.git](https://github.com/warungedijakarta-bot/idr-aggregator.git)
    ```
2.  **Persyaratan:** Pastikan **Java 17** dan **Maven** sudah terinstal.
3.  **Run Application:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Akses API:**
    * **Latest Rates:** `http://localhost:8080/api/finance/data/latest_idr_rates`
    * **Historical:** `http://localhost:8080/api/finance/data/historical_idr_usd`
    * **Currencies:** `http://localhost:8080/api/finance/data/supported_currencies`

---

### 📊 Contoh Output & Logika Bisnis
Output pada endpoint `latest_idr_rates` menyertakan kalkulasi khusus **USD_BuySpread_IDR** menggunakan rumus:
`Total = (1 / base_rate_usd) * (1 + spread_factor)`

#### 1. Latest IDR Rates
```json
{
  "amount": 1.0,
  "base": "IDR",
  "date": "2026-03-05",
  "rates": {
    "USD": 0.000059,
    ...
  },
  "USD_BuySpread_IDR": 17093.898305084746
}