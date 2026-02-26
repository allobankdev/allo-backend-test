# IDR Finance Data Service

Aplikasi Spring Boot (Java 17) untuk menyediakan data nilai tukar Rupiah (IDR) menggunakan Frankfurter API.
Dirancang dengan clean architecture, menggunakan Strategy Pattern, serta memuat data ke dalam cache saat startup untuk performa tinggi.

Menjalankan Aplikasi
Prasyarat

* Java 17+
* Maven 3.6+

Build
```bash
    mvn clean package
```
Run
```bash
    mvn spring-boot:run
```

Akses di: http://localhost:8080

Test
```bash 
   mvn test
```
## Endpoint API

Base URL:

/api/finance/data
### 1. Latest IDR Rates
Mengambil kurs IDR terbaru + perhitungan spread_factor.

```bash
curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates
```
### 2. Historical IDR-USD
Mengambil riwayat kurs IDR terhadap USD.
```bash
curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd
```
### 3. Supported Currencies
Mengambil daftar mata uang yang didukung.
```bash
curl -X GET http://localhost:8080/api/finance/data/supported_currencies
```
## Spread Factor (Personalisasi)

Spread dihitung dari username GitLab:

* Username: sahrilsaepuloh
* Hasil: 0.00508

Rumus:

1. Jumlahkan ASCII tiap karakter 
2. % 1000 
3. Bagi 100000

## Penjelasan Arsitektur

### 1. Strategy Pattern
* Supaya tidak perlu pakai banyak if-else di controller.
* Setiap jenis data punya kelas sendiri untuk mengambil datanya.
* Jadi kalau mau tambah fitur baru, cukup tambah kelas baru tanpa mengubah kode lama.
* Kode jadi lebih rapi dan mudah dirawat.

### 2. FactoryBean (Client Factory)

* Digunakan untuk membuat RestTemplate.
* Di dalamnya bisa atur timeout, alamat API, dan cara menangani error.
* Supaya pengaturannya terpusat dan bisa dipakai ulang.
* Kode jadi lebih bersih dan tidak berulang-ulang.

### 3. ApplicationRunner

* Saat aplikasi pertama kali jalan, data langsung diambil dan disimpan di cache.
* Jadi saat ada request, respon lebih cepat.
* Proses ini dijalankan setelah Spring siap, sehingga tidak mengganggu startup.
* Kalau terjadi error, aplikasi tetap bisa berjalan dengan aman.