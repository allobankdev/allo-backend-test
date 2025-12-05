# Allo Bank Backend Developer Take-Home Test

## Deskripsi
Proyek ini adalah solusi untuk take-home test Allo Bank Backend Developer. Tujuannya adalah membuat **Spring Boot REST API** yang mengagregasi data dari **Frankfurter Exchange Rate API** untuk fokus pada mata uang **IDR (Indonesian Rupiah)**.

## Fitur
- Endpoint: `GET /api/finance/data/{resourceType}`
- Resource Types:
    - `latest_idr_rates` → Mengembalikan kurs terbaru IDR dengan perhitungan **USD_BuySpread_IDR**
    - `historical_idr_usd` → Mengembalikan data historis IDR ke USD
    - `supported_currencies` → Mengembalikan daftar semua mata uang yang tersedia
- Strategy Pattern untuk pemisahan logika fetch data
- Data di-load sekali saat startup (ApplicationRunner) ke **in-memory store**
- Thread-safe dan immutable


## Prasyarat
- Java 17
- Maven 3.5.8

## Running
```bash
./mvnw spring-boot:run
