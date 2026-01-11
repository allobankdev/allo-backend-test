# Allo Backend Test - Finance API

Aplikasi Spring Boot ini bertugas untuk mengambil dan mengolah data kurs Rupiah (IDR) dari Frankfurter Exchange Rate API.

## 📋 Panduan Setup & Cara Jalanin

### Yang Dibutuhkan
- Java 17 atau yang lebih baru
- Maven

### Langkah-langkah
1. **Clone repository ini:**
   ```bash
   git clone <repository-url>
   cd allo-backend-test
   ```

2. **Build aplikasinya:**
   ```bash
   mvn clean install
   ```

3. **Jalankan aplikasi:**
   ```bash
   mvn spring-boot:run
   ```

4. **Jalankan Test (Unit & Integration):**
   ```bash
   mvn test
   ```

---

## 🚀 Cara Pakai API

Berikut contoh cara ngetes endpoint-nya pakai cURL:

### 1. Cek Kurs Terbaru (Latest Rates)
Endpoint ini menampilkan kurs terbaru dengan base IDR. Di sini juga ada field spesial `USD_BuySpread_IDR` yang dihitung otomatis.
```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```

### 2. Cek Data Historis (Historical Data)
Menampilkan riwayat kurs IDR ke USD dari tanggal 1 Januari 2024 sampai 5 Januari 2024.
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```

### 3. Cek Mata Uang yang Tersedia (Supported Currencies)
Menampilkan daftar semua mata uang yang didukung sama sistem ini.
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```

---

## 👤 Info Personalisasi

Aplikasi ini menghitung *spread factor* unik berdasarkan username GitHub saya:

*   **GitHub Username**: `prasetyahs`
*   **Total Kode ASCII**: 1092
*   **Spread Factor**: `0.00092`
    *   *Rumusnya*: `(1092 % 1000) / 100000.0` = `92 / 100000.0` = `0.00092`

---

## 🛠️ Kenapa Pakai Arsitektur Ini?

Berikut alasan teknis di balik keputusan kodingan yang saya buat:

### 1. Polimorfisme (Strategy Pattern)
Saya pilih **Strategy Pattern** dan interface `IDRDataFetcher` supaya nggak kebanyakan `if-else` atau `switch-case` di Controller.

*   **Gampang Dikembangin**: Kalau besok mau nambah fitur baru (misal: kurs Crypto), saya tinggal bikin class baru yang implement interface tadi. Kode yang lama nggak perlu diubah-ubah, jadi lebih aman dari error (bug).
*   **Kode Lebih Rapi**: Tiap logika pengambilan data punya "rumah" sendiri-sendiri. Controller tugasnya cuma terima request, bukan mikirin cara ambil datanya.

### 2. Client Factory (FactoryBean)
Saya bungkus pembuatan `WebClient` pakai `FrankfurterClientFactoryBean`.

*   **Konfigurasi Terpusat**: Semua settingan ribet kayak Base URL, timeout, atau limit memori diatur di satu tempat aja.
*   **Pemisahan Tugas**: Controller atau Service nggak perlu tau gimana cara bikin koneksi ke API luar. Mereka taunya terima objek `WebClient` yang udah siap pakai. Ini best practice biar kode lebih modular.

### 3. Kenapa Pakai ApplicationRunner?
Saya set supaya data diambil pas startup pakai `StartupDataRunner` (`ApplicationRunner`), bukan pakai `@PostConstruct`.

*   **Lebih Aman**: `ApplicationRunner` jalan cuma kalau semua komponen Spring udah siap 100%. Kalau pakai `@PostConstruct`, kadang ada risiko koneksi database atau network belum siap tapi udah dipanggil.
*   **User Nggak Nunggu**: Karena data udah ditarik semua pas aplikasi nyala dan disimpan di memori *immutable*, user yang hit API bakal dapet respon super cepet (hampir 0ms latency) karena nggak perlu request ke luar lagi tiap kali ada hit.
