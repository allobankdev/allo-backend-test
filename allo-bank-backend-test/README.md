# Allo Bank Backend Developer Take-Home Test

Terima kasih sudah menilai aplikasi ini! Repository ini adalah solusi untuk take-home test Allo Bank yang berfokus pada pengolahan data keuangan IDR menggunakan Spring Boot.

---

🛠 Setup & Run Instructions
1. Clone Repository
```bash
   git clone <repository-url>
```

2. Build & Run
  ```bash
./mvnw clean install
```
 
```bash
  ./mvnw spring-boot:run
```

Requirements

* Java 17+

* Maven 3.5.8+

* Internet connection (hanya untuk initial data fetch, mocked in tests)

📌 Endpoint Usage

Endpoint utama:

    GET /api/finance/data/{resourceType}
resourceType options:
* latest_idr_rates
* historical_idr_usd
* supported_currencies

Contoh cURL:

* curl http://localhost:8089/api/finance/data/latest_idr_rates
* curl http://localhost:8089/api/finance/data/historical_idr_usd
* curl http://localhost:8089/api/finance/data/supported_currencies


🧑‍💻 Personalization

GitHub Username: tisnandanurhidayat

Spread Factor Calculation

Formula:

    Spread Factor = (sum of ASCII values of lowercase username % 1000) / 100000.0
    Example Spread Factor: 0.00765

    Formula untuk USD_BuySpread_IDR
    USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)

🏗 Architectural Rationale
1. Polymorphism Justification (Strategy Pattern)

Strategy Pattern digunakan untuk memisahkan logika pengambilan data pada tiga resource:

* latest_idr_rates

* historical_idr_usd

* supported_currencies

Keuntungan:

* Mudah diperluas tanpa mengubah code yang sudah ada

* Maintainable

* Menghindari if/else atau switch yang kompleks di controller

* Cleaner separation of concerns

2. Client Factory (FactoryBean)

FactoryBean digunakan untuk membuat instance RestTemplate (atau WebClient) dengan konfigurasi khusus seperti:

* Base URL

* Timeout

* Global headers

Keuntungan:

* Client lebih konsisten

* Konfigurasi dapat diatur lewat application.yml

* Lebih fleksibel dibanding mendefinisikan @Bean biasa

* Memisahkan creation logic dari application logic

3. Startup Runner Choice (ApplicationRunner)

ApplicationRunner digunakan untuk:

* Preload seluruh data dari tiga resource

* Menyimpan data ke in-memory store yang thread-safe dan immutable

* Memastikan API tidak memanggil external API setiap request

Keuntungan:

* Lebih aman daripada @PostConstruct

* Menjamin data tersedia sebelum aplikasi menerima request

* Sesuai dengan production-grade initialization flow

✅ Testing
Unit Tests

Semua strategy class diuji menggunakan mock RestTemplate

Memverifikasi:

* Transformasi data

* Perhitungan USD_BuySpread_IDR

Integration Tests

* Memastikan ApplicationRunner preload data sebelum context fully started
* Store terisi sebelum API digunakan

Mocking External API

* Tidak ada API nyata yang dipanggil selama testing

📄 Notes

* Semua resourceType mengembalikan JSON array yang konsisten

* Supported currencies dan historical data menggunakan transformasi minimal

* Latest IDR Rates memiliki perhitungan unik: USD_BuySpread_IDR

* Data disajikan dari in-memory store, bukan hasil panggilan API realtime