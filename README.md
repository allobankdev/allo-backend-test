# Allo Bank Finance API — Take-Home Test

> **Candidate:** thaufaniqbal
> **GitHub:** [@thaufaniqbal](https://github.com/thaufaniqbal)

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2.5 |
| Language | Java 17 |
| HTTP Client | WebClient (Spring WebFlux) |
| Persistence | Spring Data JPA + H2 (in-memory SQL) |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito, AssertJ, WireMock |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Observability | Spring Boot Actuator, MDC Trace Filter |

---

## Personalisasi: Spread Factor

**GitHub Username:** `thaufaniqbal`

| Karakter | Unicode Value |
|----------|--------------|
| t | 116 |
| h | 104 |
| a | 97 |
| u | 117 |
| f | 102 |
| a | 97 |
| n | 110 |
| i | 105 |
| q | 113 |
| b | 98 |
| a | 97 |
| l | 108 |
| **Total Sum** | **1264** |

```
Spread Factor = (1264 % 1000) / 100000.0
             = 264 / 100000.0
             = 0.00264
```

**Formula:**
```
USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00264)
```

---

## Setup & Cara Menjalankan

### Prerequisites
- Java 17+
- Maven 3.8+

### Clone & Build

```bash
git clone https://github.com/thaufaniqbal/allo-bank-finance.git
cd allo-bank-finance
mvn clean install -DskipTests
```

### Jalankan Aplikasi

```bash
mvn spring-boot:run
```

Saat startup, `ApplicationRunner` otomatis fetch semua data dari Frankfurter API dan menyimpannya ke in-memory store:

```
=== Starting financial data initialization ===
Fetching 3 resource(s): [latest_idr_rates, historical_idr_usd, supported_currencies]
Successfully loaded and persisted resource: latest_idr_rates
Successfully loaded and persisted resource: historical_idr_usd
Successfully loaded and persisted resource: supported_currencies
FinanceDataStore sealed. 3 resource(s) loaded.
=== Financial data initialization complete: 3/3 resources loaded successfully ===
```

### Jalankan Tests

```bash
# Semua test (unit + integration — fully offline, pakai WireMock)
mvn test

# Unit tests saja
mvn test -Dtest="LatestIdrRatesFetcherTest,HistoricalIdrUsdFetcherTest,SupportedCurrenciesFetcherTest,SpreadCalculatorTest,FinanceControllerTest,FinanceDataServiceTest,FinanceDataStoreTest"

# Integration tests saja
mvn test -Dtest="FinanceDataStartupRunnerIntegrationTest"
```

### URL yang Tersedia

| URL | Keterangan |
|-----|------------|
| `http://localhost:8099/api-docs` | OpenAPI JSON spec |
| `http://localhost:8099/actuator/health` | Health check |
| `http://localhost:8099/actuator/info` | App info |
| `http://localhost:8099/h2-console` | H2 Database console |

**H2 Console credentials:**
- JDBC URL: `jdbc:h2:mem:allobank`
- Username: `sa`
- Password: *(kosong)*

---

## Endpoint

### Base URL
```
http://localhost:8099/
```

### Single Endpoint
```
GET /api/finance/data/{resourceType}
```

---

### 1. Latest IDR Rates

```bash
curl -X GET http://localhost:8099/api/finance/data/latest_idr_rates \
  -H "Accept: application/json" | jq .
```

**Response (HTTP 200):**
```json
{
  "success": true,
  "message": "OK",
  "timestamp": "2024-01-05T08:00:00.000Z",
  "data": {
    "resourceType": "latest_idr_rates",
    "fetchedAt": "2024-01-05T08:00:00.000Z",
    "data": {
      "amount": "1",
      "base": "IDR",
      "date": "2024-01-05",
      "rates": {
        "USD": 0.000064,
        "EUR": 0.000059
      }
    },
    "usdBuySpreadIdr": 15666.75,
    "spreadFactor": 0.00264
  }
}
```

---

### 2. Historical IDR/USD Rates

```bash
curl -X GET http://localhost:8099/api/finance/data/historical_idr_usd \
  -H "Accept: application/json" | jq .
```

**Response (HTTP 200):**
```json
{
  "success": true,
  "message": "OK",
  "timestamp": "2024-01-05T08:00:01.000Z",
  "data": {
    "resourceType": "historical_idr_usd",
    "fetchedAt": "2024-01-05T08:00:01.000Z",
    "data": {
      "amount": "1",
      "base": "IDR",
      "start_date": "2024-01-01",
      "end_date": "2024-01-05",
      "rates": {
        "2024-01-02": { "USD": 0.000064 },
        "2024-01-03": { "USD": 0.000065 },
        "2024-01-05": { "USD": 0.000063 }
      }
    }
  }
}
```

---

### 3. Supported Currencies

```bash
curl -X GET http://localhost:8099/api/finance/data/supported_currencies \
  -H "Accept: application/json" | jq .
```

**Response (HTTP 200):**
```json
{
  "success": true,
  "message": "OK",
  "timestamp": "2024-01-05T08:00:02.000Z",
  "data": {
    "resourceType": "supported_currencies",
    "fetchedAt": "2024-01-05T08:00:02.000Z",
    "data": {
      "AUD": "Australian Dollar",
      "IDR": "Indonesian Rupiah",
      "USD": "US Dollar",
      "EUR": "Euro"
    }
  }
}
```

---

### Error Responses

**Resource type tidak valid (HTTP 404):**
```bash
curl -X GET http://localhost:8099/api/finance/data/invalid_type | jq .
```
```json
{
  "success": false,
  "message": "Resource type 'invalid_type' not found. Valid types are: latest_idr_rates, historical_idr_usd, supported_currencies",
  "timestamp": "2024-01-05T08:00:05.000Z"
}
```

**HTTP method salah (HTTP 405):**
```bash
curl -X POST http://localhost:8099/api/finance/data/latest_idr_rates | jq .
```
```json
{
  "success": false,
  "message": "Request method 'POST' is not supported",
  "timestamp": "2024-01-05T08:00:06.000Z"
}
```

---

## Struktur Project

```
src/
├── main/
│   ├── java/com/allobank/finance/
│   │   ├── FinanceApplication.java               # Entry point
│   │   ├── client/
│   │   │   └── WebClientFactoryBean.java         # FactoryBean<WebClient>
│   │   ├── config/
│   │   │   ├── AppProperties.java                # @ConfigurationProperties
│   │   │   ├── FetcherConfig.java                # Strategy Map<String, IDRDataFetcher>
│   │   │   ├── FrankfurterProperties.java        # Config untuk external API
│   │   │   └── OpenApiConfig.java                # Swagger/OpenAPI metadata
│   │   ├── controller/
│   │   │   └── FinanceController.java            # GET /api/finance/data/{resourceType}
│   │   ├── dto/
│   │   │   ├── ApiResponse.java                  # Generic response wrapper
│   │   │   ├── FinanceDataResponse.java           # Unified response model
│   │   │   └── FrankfurterDto.java               # DTO untuk Frankfurter API
│   │   ├── entity/
│   │   │   └── FinanceDataCache.java             # JPA entity
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java       # @RestControllerAdvice
│   │   │   └── ResourceNotFoundException.java    # Custom 404 exception
│   │   ├── fetcher/
│   │   │   ├── IDRDataFetcher.java               # Strategy Interface
│   │   │   ├── LatestIdrRatesFetcher.java        # Strategy #1 + spread calculation
│   │   │   ├── HistoricalIdrUsdFetcher.java      # Strategy #2
│   │   │   ├── SupportedCurrenciesFetcher.java   # Strategy #3
│   │   │   └── SpreadCalculator.java             # Spread factor logic
│   │   ├── filter/
│   │   │   └── MdcTraceFilter.java               # MDC traceId per request
│   │   ├── repository/
│   │   │   └── FinanceDataCacheRepository.java   # JPA repository
│   │   ├── runner/
│   │   │   └── FinanceDataStartupRunner.java     # ApplicationRunner
│   │   └── service/
│   │       ├── FinanceDataService.java            # Business logic
│   │       └── FinanceDataStore.java             # Thread-safe in-memory store
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/allobank/finance/
    │   ├── controller/
    │   │   └── FinanceControllerTest.java
    │   ├── fetcher/
    │   │   ├── LatestIdrRatesFetcherTest.java
    │   │   ├── HistoricalIdrUsdFetcherTest.java
    │   │   ├── SupportedCurrenciesFetcherTest.java
    │   │   └── SpreadCalculatorTest.java
    │   ├── service/
    │   │   ├── FinanceDataServiceTest.java
    │   │   └── FinanceDataStoreTest.java
    │   └── integration/
    │       └── FinanceDataStartupRunnerIntegrationTest.java
    └── resources/
        └── application-test.yml
```

---

## Penjelasan Arsitektur

### 1. Kenapa Pakai Strategy Pattern?

Kalau pakai conditional if/else biasa:

```java
// Cara lama — tiap tambah resource type harus ubah class ini
public FinanceDataResponse getData(String resourceType) {
    if (resourceType.equals("latest_idr_rates")) {
        return fetchLatest();
    } else if (resourceType.equals("historical_idr_usd")) {
        return fetchHistorical();
    } else if (resourceType.equals("supported_currencies")) {
        return fetchCurrencies();
    }
    throw new ResourceNotFoundException(...);
}
```

Masalahnya setiap kali ada resource type baru, kita harus ubah class yang sudah ada dan sudah di-test. Rawan bug.

Solusinya pakai Strategy Pattern:

```java
// Interface sebagai kontrak
public interface IDRDataFetcher {
    FinanceDataResponse fetch();
    String getResourceType();
}

// Tiap strategy berdiri sendiri
@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    @Override public String getResourceType() { return "latest_idr_rates"; }
    @Override public FinanceDataResponse fetch() { ... }
}

// FetcherConfig otomatis build map-nya, controller bebas dari conditional
@Bean
public Map<String, IDRDataFetcher> fetcherMap(List<IDRDataFetcher> fetchers) {
    return fetchers.stream()
        .collect(Collectors.toMap(IDRDataFetcher::getResourceType, Function.identity()));
}
```

Kalau mau tambah resource type baru, tinggal buat satu class baru. Tidak ada code lama yang perlu diubah.

---

### 2. Kenapa Pakai FactoryBean, bukan @Bean Biasa?

Dengan `@Bean` biasa konfigurasinya flat dan inline. Pakai `FactoryBean<WebClient>` kita bisa pisahkan semua konfigurasi Netty, timeout, dan connector ke dalam satu class tersendiri yang lebih terorganisir dan mudah di-test secara independen.

```java
@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    @Override
    public WebClient getObject() throws Exception {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMs())
            .responseTimeout(Duration.ofMillis(properties.getReadTimeoutMs()))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(...))
                    .addHandlerLast(new WriteTimeoutHandler(...)));

        return WebClient.builder()
            .baseUrl(properties.getBaseUrl())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override public Class<?> getObjectType() { return WebClient.class; }
    @Override public boolean isSingleton() { return true; }
}
```

`isSingleton() = true` karena WebClient itu stateless dan thread-safe, jadi satu instance cukup untuk semua fetcher.

---

### 3. Kenapa Pakai ApplicationRunner, bukan @PostConstruct?

`@PostConstruct` berjalan saat bean diinisialisasi, sebelum ApplicationContext sepenuhnya siap. Risikonya JPA transaction manager dan Netty belum tentu sudah ready, bisa muncul `LazyInitializationException` atau `TransactionRequiredException`.

`ApplicationRunner` dijamin berjalan setelah semua bean selesai diinisialisasi, jadi JPA dan WebClient sudah pasti siap dipakai.

```java
@Component
public class FinanceDataStartupRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Map.Entry<String, IDRDataFetcher> entry : fetcherMap.entrySet()) {
            FinanceDataResponse response = entry.getValue().fetch();
            financeDataStore.put(entry.getKey(), response);
            cacheRepository.save(...);
        }
        financeDataStore.seal();
    }
}
```

Kalau salah satu resource gagal di-fetch, runner tetap lanjut ke resource berikutnya. Store di-seal tetap, jadi data yang berhasil dimuat masih bisa dilayani.

---

### 4. Thread-Safe Immutable Store

```java
@Component
public class FinanceDataStore {

    // ConcurrentHashMap: thread-safe untuk concurrent read dan write saat loading
    private final ConcurrentHashMap<String, FinanceDataResponse> store = new ConcurrentHashMap<>();
    
    // AtomicBoolean: lock-free, thread-safe untuk sealed flag
    private final AtomicBoolean sealed = new AtomicBoolean(false);

    public void put(String key, FinanceDataResponse value) {
        if (sealed.get()) {
            throw new IllegalStateException("FinanceDataStore is sealed. No writes allowed.");
        }
        store.put(key, value);
    }

    public void seal() {
        sealed.compareAndSet(false, true);
    }

    public Map<String, FinanceDataResponse> getAll() {
        return Collections.unmodifiableMap(store);
    }
}
```

Setelah `seal()` dipanggil, tidak ada write yang bisa masuk. Semua read aman tanpa locking karena `ConcurrentHashMap` menjamin visibility-nya.

---

### 5. API Versioning

```
GET /api/v1/finance/data/{resourceType}
```

Pakai URI versioning (`/v1`) supaya langsung keliatan dari URL-nya, mudah dibaca di log, dan kompatibel dengan semua HTTP client tanpa perlu custom header. Ini juga jadi standar umum di fintech API seperti Midtrans dan Xendit.

karena kebutuhan untuk allo bank test, itu harus di sesuaikan menjadi 

```
GET /api/finance/data/{resourceType}
```
---

### 6. MDC Request Tracing

```java
@Component
@Order(1)
public class MdcTraceFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        try {
            MDC.put("traceId", traceId);
            httpResponse.setHeader("X-Trace-Id", traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Wajib clear untuk mencegah ThreadLocal leak di thread pool
        }
    }
}
```

Setiap log otomatis menyertakan `[traceId]`:
```
2024-01-05 08:00:01 [http-nio-8099-exec-1] [a3f9b2c1d4e56789] INFO  FinanceController - GET /api/finance/data/latest_idr_rates
```

`traceId` yang sama juga dikembalikan lewat response header `X-Trace-Id`, jadi client bisa korelasikan request mereka dengan log di server.
