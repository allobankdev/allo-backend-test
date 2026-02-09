# IDR Rate Aggregator
### Allo Bank Backend Developer Take-Home Test

------------------------------------------------------------------------

## 📌 Ikhtisar Proyek

Aplikasi Spring Boot ini mengumpulkan data finansial dari Frankfurter Exchange Rate API publik dan menyediakannya melalui satu endpoint REST polimorfik.

Aplikasi ini mendemonstrasikan:
- Strategy Design Pattern
- Kustom Spring FactoryBean
- Ingesti data saat startup menggunakan ApplicationRunner
- Penyimpanan in-memory yang thread-safe dan immutable
- Penanganan error yang terstruktur
- Arsitektur siap produksi (production-ready)

------------------------------------------------------------------------

# 🚀 Instruksi Persiapan & Menjalankan

## 1️⃣ Clone Repositori
git clone https://github.com/ray-code2/allo-backend-test.git
cd idr-rate-aggregator

## 2️⃣ Build Proyek
mvn clean install

## 3️⃣ Jalankan Aplikasi
mvn spring-boot:run

Aplikasi akan berjalan di: http://localhost:8080

## 4️⃣ Jalankan Pengujian (Test)
mvn test

------------------------------------------------------------------------

# 🌐 API Internal

Endpoint tunggal:
GET /api/finance/data/{resourceType}

Tipe sumber daya (resource types) yang tersedia:
- latest_idr_rates
- historical_idr_usd
- supported_currencies

------------------------------------------------------------------------

# 🧪 Contoh Penggunaan Endpoint (cURL)

## 1️⃣ Kurs IDR Terbaru
curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates'

## 2️⃣ Historis IDR -> USD
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd'

## 3️⃣ Mata Uang yang Didukung
curl --location 'http://localhost:8080/api/finance/data/supported_currencies'

## 4️⃣ Tipe Resource Tidak Valid (Penanganan Error)
curl --location 'http://localhost:8080/api/finance/data/invalid_type'

Contoh respon error:
{
    "status": 400,
    "error": "Bad Request",
    "message": "Resource type tidak valid: invalid_type",
    "timestamp": "2026-02-09T15:08:49.067324Z"
}

------------------------------------------------------------------------

# 💡 Catatan Personalisasi

### Username GitHub
ray-code2

### Kalkulasi Spread Factor
1. Ubah username ke huruf kecil -> ray-code2
2. Hitung jumlah nilai ASCII dari semua karakter
3. Gunakan rumus: Spread Factor = (Total % 1000) / 100000.0

### Nilai Terhitung
- Total ASCII: 839
- Spread Factor: 0.00839

### Rumus Akhir yang Digunakan
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)

------------------------------------------------------------------------

# 🏗 Rasional Arsitektur

## 1️⃣ Mengapa Strategy Pattern?
Digunakan untuk menghilangkan ketergantungan ketat antara kontroler dan logika pengambilan data, mematuhi Open/Closed Principle, dan memudahkan ekstensi fitur tanpa mengubah kode yang sudah stabil.

## 2️⃣ Mengapa FactoryBean untuk API Client?
Mengkapsulasi logika instansiasi yang kompleks, sentralisasi konfigurasi (URL, timeout), dan memisahkan konstruksi objek dari logika bisnis utama.

## 3️⃣ Mengapa ApplicationRunner?
Berjalan setelah Spring context siap sepenuhnya, menjamin semua strategi terinjeksi sebelum memuat data, dan memberikan kontrol siklus hidup yang lebih bersih untuk lingkungan produksi.

------------------------------------------------------------------------

# 🧠 Thread Safety & Immutability
- Data dimuat sekali saat startup.
- Disimpan dalam Collections.unmodifiableMap (immutable).
- Referensi ditandai sebagai volatile.
- Metode loadAllData() disinkronisasi (synchronized).
- Tidak ada panggilan API eksternal selama runtime request.

------------------------------------------------------------------------

Dibuat pada: 2026-02-09
Dibuat oleh: Raymond Tjahyadi