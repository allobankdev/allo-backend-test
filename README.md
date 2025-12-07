# Allo Bank Backend Developer Take-Home Test

🛠️ Dokumentasi
•	Cara setup, build, run aplikasi & test
1.	Clone repo
2.	Konfigurasi application.properties
3.	Build project menggunakan maven
mvn clean install
4.	Jalankan aplikasi 
mvn spring-boot:run
•	Contoh cURL untuk ketiga resource
1.	latest_idr_rates
curl --location 'http://localhost:8080/api/finance/data/latest_idr_rates'
2.	historical_idr_usd
curl --location 'http://localhost:8080/api/finance/data/historical_idr_usd'
3.	supported_currencies
curl --location 'http://localhost:8080/api/finance/data/supported_currencies'
•	Username GitHub Anda
https://github.com/fadhilah0602/allo-backend-test
•	Spread Factor unik Anda
fadhilah0602 : 1017
Total = 102 + 97 + 100 + 104 + 105 + 108 + 97 + 104 + 48 + 54 + 48 + 50

Spread Factor = (TotalUnicode % 1000) / 100000.0
              = 17 / 100000.0 
              = 0.00017

🛠️ Penjelasan Arsitektur
1. Polymorphism Justification
Mengapa Strategy Pattern digunakan dibanding if/else?
Karena requirement mensyaratkan arsitektur yang bersih, fleksibel, dan extensible.
Dengan Strategy Pattern:
1)	Extensibility
Jika suatu hari API baru ditambahkan hanya menambahkan class dengan implements ke IDRDataFetcher tanpa mengubah controller dan service lain.
2)	Maintainability
Setiap resource memiliki:
•	URL berbeda
•	Data parsing berbeda
•	Transformasi berbeda .
Strategy membuat ini terpisah rapi.
3)	Clean Architecture & SRP (Single Responsibility Principle)
Controller hanya bertugas melakukan fetchData sesuai dengan resource yang dibuat Bukan mengatur logic fetching, error handling, atau transformasi.
Sesuai prinsip SOLID – Open/Closed Principle.
4)	polymorphism
kemampuan sebuah objek untuk memiliki banyak bentuk (many forms) dan merespons suatu pemanggilan method dengan cara yang berbeda-beda, tergantung implementasinya.
2. Client Factory
Penggunaan FactoryBean memberikan fleksibilitas lebih besar dibandingkan mendefinisikan client eksternal menggunakan @Bean biasa. FactoryBean memungkinkan Spring mengeksekusi logic pembuatan objek sebelum objek tersebut diregistrasikan ke ApplicationContext, sehingga cocok untuk skenario di mana client eksternal:
•	membutuhkan konfigurasi dinamis (misalnya base URL, credential, timeout),
•	memiliki proses inisialisasi yang kompleks,
•	memerlukan validasi eksternal sebelum instance dibuat,
•	perlu menghasilkan instance dengan tipe yang berbeda berdasarkan konfigurasi tertentu,
•	atau harus dapat diubah tanpa memodifikasi class yang menggunakan client tersebut (loose coupling).
Dengan FactoryBean, kita dapat mengenkapsulasi seluruh creation logic, sehingga konstruktor class utama tidak mengetahui detail bagaimana client dibangun.

3. Startup Runner
Alasan memakai ApplicationRunner/CommandLineRunner dibanding @PostConstruct.
Aplikasi ini menggunakan ApplicationRunner karena:
•	Dijalankan setelah seluruh Spring Context siap, sehingga aman untuk memanggil API eksternal, load konfigurasi, atau inisialisasi resource.
•	@PostConstruct berjalan terlalu awal, saat bean baru selesai dibuat dan dependency lain mungkin belum siap.
•	Runner memberikan lifecycle yang lebih jelas dan predictable, hanya berjalan satu kali saat startup aplikasi.
•	Mendukung ApplicationArguments, sehingga konfigurasi startup lebih fleksibel.
•	Sesuai best practice Spring Boot modern, lebih mudah diuji dan lebih maintainable dibanding @PostConstruct.


