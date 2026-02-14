# FRANKFURTER EXCHANGE RATE APPS
## MENJALANKAN APLIKASI
### Prasyarat (Prerequisites)
Sebelum memulai, pastikan komputer/laptop anda sudah terinstall :

* Java Development Kit (JDK)
* Git
* Maven (Opsional, sudah tersedia via ./mvnw)

### Langkah-langkah setup
1. Clone repository
```
git clone https://github.com/allobankdev/allo-backend-test.git
cd allo-backend-test
```
2. Pindah branch
```
git checkout feat/arief-frankfruter-api
```
3. Install dependensi dan compile project
```
./mvnw clean install
```
4. Run aplikasi
```
java -jar {path ke file jar} (biasanya di dalam folder target)
```
Maka app akan berjalan pada port 8081

### Menjalankan test pada aplikasi
Jalankan command berikut
```
./mvnw test
```
Setelah selesai, hasil pengujian (test report) dapat dilihat pada direktori:
> target/surefire-reports/
### Endpoint usage
note : baseURL yang saya gunakan disini http://localhost:8081
* Mendapatkan history terakhir rate IDR
```
{baseURL}/api/finance/data/latest_idr_rates
```
* Mendapatkan rate IDR to USD, dalam range tertentu
```
{baseURL}/api/finance/data/historical_idr_usd
```
* Mendapatkan semua list supported currencies
```
{abseURL}/api/finance/data/currencies
```
### Additional Notes
github username ariefw96 and spread factor is 0.00749

## Architectural Rationale
* **Polymorphism Justification**

  Strategy pattern lebih baik digunakan daripada if-else block dikarenakan untuk menambah resource baru kita tidak perlu merombak service layer, hanya perlu menambah implementasi dari "IDRDataFetcher" sesuai dengan kebutuhan. Hal ini berbanding lurus dengan prinsip **extensibility** dan karena untuk implementasi logic nya terpisah untuk masing-masing logic dan tidak tercampur satu sama lain efeknya adalah logic tersebut lebih mudah untuk di-_maintain_ sesuai dengan prinsip **maintainability**. Perubahan kode pada suatu resource tidak akan mengganggu resource yang lain.

* **Client Factory**

  Di sini saya menggunakan FactoryBean karena jika nanti ada kebutuhan membuat RestTemplate lain dengan konfigurasi berbeda, kita cukup membuat class baru yang mengimplementasikan FactoryBean<RestTemplate> tanpa harus mengubah konfigurasi yang sudah ada. Jadi, kita tidak perlu menyentuh kode lama yang sudah berjalan. Ini sejalan dengan konsep **extensibility** dan **maintainability**, karena aplikasi jadi lebih mudah dikembangkan dan dirawat. Singkatnya, menggunakan FactoryBean membuat aplikasi lebih siap untuk kebutuhan tambahan di masa depan tanpa risiko merusak konfigurasi yang sudah ada.

* **Startup Runner Choice**

  Kita menggunakan ApplicationRunner daripada @PostConstruct karena runner dijalankan setelah seluruh aplikasi dan semua @Bean benar-benar siap digunakan. Jadi saat kita menyimpan cache ketika aplikasi pertama kali berjalan, kita bisa memastikan semua komponen yang dibutuhkan sudah aktif dan tidak ada yang masih dalam proses inisialisasi. Dengan begitu, risiko aplikasi gagal start karena ada bean yang belum siap bisa dihindari.
    
 

