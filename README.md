# Setup/Run Instructions
   1. Select the folder where you want to clone the project.
   2. Right click to open the CMD or git Bash.
   3. type: git clone https://github.com/Dhanuaji/allo-backend-test (into your specified folder path)
   4. type: cd allo-backend-test
   5. type: ./mvnw clean install
   6. type: ./mvnw spring-boot:run
   7. Spring boot application already running.

# Endpoint Usage
  ## Using CMD or GIT Bash
     1. type: curl http://localhost:8080/api/finance/data/latest_idr_rates (for fetch latest idr rates to other currencies rates collection)
     2. type: curl http://localhost:8080/api/finance/data/historical_idr_usd (for fetch historical data of idr to usd rates)
     3. type: curl http://localhost:8080/api/finance/data/supported_currencies (for fetch all collection of supported currencies data available) 

# Perzonalization Notes
   1. Github username: Dhanuaji
   2. ASCII SUM = 836
   3. Spread Factor => (836 % 1000) / 100000.0 = 0.00836

# Architectural Rationale
   1. Polymorphism Justification
      Untuk menangani tiga jenis resource (latest_idr_rates, historical_idr_usd, supported_currencies), saya menggunakan Strategy Pattern alih‑alih conditional block (if/else atau switch) di service layer.
      - Extensibility: Dengan Strategy Pattern, menambah resource baru cukup membuat implementasi IDataFetcher baru dan mendaftarkannya sebagai @Component. Tidak perlu mengubah service atau controller.
      - Maintainability: Logika tiap resource terpisah dalam kelas masing‑masing, sehingga lebih mudah diuji, dipelihara, dan dibaca. Conditional block akan cepat menjadi panjang dan sulit dikelola.
      - Polymorphism: Controller dan service cukup bergantung pada interface (IDataFetcher), sehingga kode lebih fleksibel dan loosely coupled.
   2. Client Factory
      Saya menggunakan FactoryBean untuk membuat instance WebClient yang dipakai semua strategi.
      - Centralized Configuration: FactoryBean memungkinkan konfigurasi base URL, headers, dan timeout di satu tempat, sehingga konsisten di seluruh aplikasi.
      - Externalized Properties: Base URL diambil dari application.yml menggunakan @Value, sehingga mudah diubah tanpa menyentuh kode.
      - Lifecycle Control: FactoryBean memberi kontrol penuh atas bagaimana client dibuat dan dikelola. Ini lebih baik daripada sekadar @Bean karena FactoryBean bisa mengatur detail instansiasi dan menjamin singleton sesuai kebutuhan.
   3. Startup Runner Choice
      Untuk melakukan data ingestion awal, saya memilih ApplicationRunner dibandingkan @PostConstruct.
      - Timing Guarantee: ApplicationRunner dijalankan setelah Spring context selesai diinisialisasi, sehingga semua dependency sudah siap. @PostConstruct bisa berjalan terlalu dini sebelum bean lain siap.
      - Production Readiness: Dengan ApplicationRunner, saya bisa memastikan data dari ketiga resource di‑fetch sekali saat startup, lalu disimpan di in‑memory store sebelum aplikasi menerima request.
      - Thread Safety & Immutability: Data store diisi sekali oleh ApplicationRunner, lalu dibuat immutable. Ini menjamin konsistensi data dan mencegah race condition.
