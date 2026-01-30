1. Setup and Run Instructions

Masuk ke folder project: idr-rate-aggregator

Build dan download library: mvn clean install

Jalankan semua test (Unit & Integration): mvn test

Jalankan aplikasi: mvn spring-boot:run

2. Endpoint Usage (cURL)

Cek Harga Terbaru (Latest): curl -X GET http://localhost:8080/api/finance/data/latest_idr_rates

Cek Data Historis (History): curl -X GET http://localhost:8080/api/finance/data/historical_idr_usd

Cek Daftar Mata Uang (Currencies): curl -X GET http://localhost:8080/api/finance/data/supported_currencies

3. Personalization Note

GitHub Username: tengkuraafi44

Total Nilai Unicode: 1273

Rumus: (1273 % 1000) / 100000.0

Spread Factor: 0.00273