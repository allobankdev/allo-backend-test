# Finance API

A Spring Boot REST API that aggregates exchange rate data from the Frankfurter API.

## Requirements
- Java 25
- Maven

## Setup & Run

### Run the application
```bash
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

## Endpoint Usage

### Latest IDR Rates
```bash
curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates'
```

### Historical IDR USD
```bash
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd'
```

### Supported Currencies
```bash
curl --location 'http://localhost:8080/api/finance/data/supported_currencies'
```

## Personalization Note

- **GitHub Username:** `Zidan-Kharisma-Sakana`
- **Unicode Sum:** `sum of lowercase chars = 2095`
- **Spread Factor:** `(2095 % 1000) / 100000.0 = 0,00095`

## Architectural Rationale

### 1. Polymorphism Justification (Strategy Pattern)

Strategi pattern umumnya digunakan untuk 

**Ekstensibilitas** — Ketika developer perlu menambahkan resource type baru, kita tinggal membuat implementasi `FinanceResourceHandler` baru dan mendaftarkannya sebagai Spring `@Component`. Developer tidak perlu menyentuh registry, service, controller, maupun runner sama sekali.

**Maintainability** — Dengan memisahkan logika setiap resource ke dalam kelasnya masing-masing, kita bisa mengubah cara kerja satu resource tanpa khawatir merusak resource lain.

**Single Responsibility** — Setiap handler yang saya buat memiliki tanggung jawab penuh atas siklus hidup resourcenya sendiri: fetch dari API, simpan ke repository, dan ambil dari store.

Bila saya menggunakan `if/else`, service layer pasti terus membesar setiap ada resource baru dan sulit dimaintain.

### 2. Client Factory Bean

`FactoryBean<RestClient>` digunakan dibanding `@Bean` karena beberapa pertimbangan:

**Konstruksi yang kompleks** — `RestClient` yang saya buat butuh beberapa langkah konfigurasi: base URL, connect/read timeout, dan default header. Dengan `FactoryBean`, kita bisa mengenkapsulasi semua kompleksitas ini di satu tempat yang jelas.
**Full Control pada Lifecycle** — `FactoryBean` memberi saya kontrol lebih detail atas bagaimana bean dibuat, cocok untuk kasus di mana proses instantiasi membutuhkan logika khusus. Kita bisa pakai isSingleton() (whether getObject return object yang sama atau tidak) dan DisposableBean (buat tutup koneksi)

### 3. Startup Runner Choice

`ApplicationRunner` digunakan dibandingkan `@PostConstruct` karena:
**Seluruh context sudah siap dan tidak memblokir inisialisasi bean lain** — `@PostConstruct` berjalan tepat setelah bean itu sendiri selesai diinisialisasi. Artinya jika saya meletakkan proses fetch di sana, proses tersebut akan memblokir inisialisasi bean-bean lain, yang berpotensi memperlambat startup aplikasi secara keseluruhan. Dengan `ApplicationRunner`, proses fetch baru berjalan setelah semua bean selesai diinisialisasi, sehingga tidak ada bean yang terganggu dan startup tetap berjalan lancar.