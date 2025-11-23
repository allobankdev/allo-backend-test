# Allo Bank Backend Developer Take-Home Test

Thank you for applying to our team! This take-home test is designed to evaluate your practical skills in building **production-ready** Spring Boot applications within a finance domain, focusing on architectural patterns and complex data handling.

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.9+

### Clone & Build
```bash
git clone <your-fork-url>
cd allo-backend-test
mvn clean package
```

### Run the App
```bash
# Option 1: Using Spring Boot plugin
mvn spring-boot:run

# Option 2: Using the packaged jar
java -jar target/idr-rate-aggregator-0.0.1-SNAPSHOT.jar
```

The application starts on http://localhost:8080

### Run Tests and Coverage
```bash
mvn test
# Generate JaCoCo HTML report
mvn test jacoco:report
# Open report (MacOS path shown)
open target/site/jacoco/index.html
```

### Configuration
External service base URL, timeout, and GitHub username are configured in `src/main/resources/application.yml`:
```yaml
frankfurter:
  base-url: https://api.frankfurter.app
  timeout-ms: 5000
app:
  github:
    username: sriHayati
```
You can override these via environment variables or a different profile if needed.

### Postman Collection
Use `postman_collection.json` in the project root to try the APIs quickly.

## 📝 Objective

Your task is to create a single Spring Boot REST API endpoint capable of aggregating data from multiple, distinct resources provided by the public, keyless **Frankfurter Exchange Rate API**. The primary focus is on handling Indonesian Rupiah (IDR) data.

The focus of this test is not just functional correctness, but demonstrating clean code, advanced Spring concepts, thread-safe design, and architectural clarity.

## I. Core Task: The Polymorphic API

### 1. External API Integration (Frankfurter API)

* **Base URL (Public):** `https://api.frankfurter.app/`.

* You must integrate with three distinct data resources to enforce the architectural pattern:

   1.  `/latest?base=IDR` (The latest rates relative to IDR)

   2.  **Historical Data:** Query a specific, small time series (e.g., `/2024-01-01..2024-01-05?from=IDR&to=USD`). **Note:** *Use the date range provided in this example unless a different range is communicated separately.*

   3.  `/currencies` (The list of all supported currency symbols)

### 2. Internal API Endpoint

You must expose **one single endpoint** in your application: ```GET /api/finance/data/{resourceType}```

Where `{resourceType}` can be one of the three strings: `latest_idr_rates`, `historical_idr_usd`, or `supported_currencies`.

#### Endpoint Usage (cURL Examples)
```bash
# Latest IDR rates (with computed USD_BuySpread_IDR field)
curl -s http://localhost:8080/api/finance/data/latest_idr_rates | jq

# Historical IDR→USD small time series
curl -s http://localhost:8080/api/finance/data/historical_idr_usd | jq

# Supported currencies
curl -s http://localhost:8080/api/finance/data/supported_currencies | jq
```
Response is a unified JSON array tailored to each resource type.

### 3. Required Functionality & Business Logic

* **Resource Handling:** Your service must correctly map the three incoming `resourceType` values to the correct data fetching strategies.

* **Data Load:** All three resources should be fetched from the external API.

* **Data Transformation (Latest IDR Rates only) - Unique Calculation:** For the **`latest_idr_rates`** resource, you must calculate and include a new field, `"USD_BuySpread_IDR"`. This is the Rupiah selling rate to USD after applying a banking spread/margin.

  **The Spread Factor Must Be Unique :**

   1.  **Input:** Your GitHub username (e.g., `johndoe47`).
   2.  **Calculation:** Calculate the sum of the Unicode (ASCII) values of all characters in your lowercase GitHub username string.
   3.  **Spread Factor Derivation:** `Spread Factor = (Sum of Unicode Values % 1000) / 100000.0`
       *(This will yield a unique factor between 0.00000 and 0.00999, ensuring a personalized result.)*

  **Final Formula:** `USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)` (where `Rate_USD` is the value from the API when `base=IDR`).

* **Other Resources:** The `historical_idr_usd` and `supported_currencies` resources can return their data with minimal transformation, but the final output must be a unified JSON array of results.

### Personalization Note
- GitHub username (from configuration): `sriHayati` → lowercase `srihayati`
- Unicode sum: s(115)+r(114)+i(105)+h(104)+a(97)+y(121)+a(97)+t(116)+i(105) = 974
- Spread Factor = (974 % 1000) / 100000.0 = `0.00974`

This factor is applied to compute `USD_BuySpread_IDR` for the `latest_idr_rates` resource:
`USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00974)`

## II. Architectural Constraints

Meeting the core task is only one part of the solution. The following constraints must be strictly adhered to and will be heavily weighted during evaluation:

### Constraint A: The Strategy Pattern

The logic for handling the three different resources (`latest_idr_rates`, `historical_idr_usd`, `supported_currencies`) must be implemented using the **Strategy Design Pattern**.

1.  Define a clear **Strategy Interface** (e.g., `IDRDataFetcher`).

2.  Implement **three concrete strategy classes** (one for each resource).

3.  The main `Controller` should dynamically select the correct strategy implementation using a map-based lookup injected by Spring, avoiding any manual `if/else` or `switch` logic in the controller layer.

### Constraint B: Client Factory Bean

The instance of your chosen external API client (`WebClient` or `RestTemplate`) **must be defined and created within a custom implementation of Spring's `FactoryBean<T>` interface**.

* This `FactoryBean` should be responsible for externalizing the API Base URL via `@Value` or `@ConfigurationProperties` and applying any initial configuration (e.g., timeouts, shared headers).

* ***You may not define the client as a simple `@Bean` in a `@Configuration` class.***

### Constraint C: Startup Data Runner & Immutability

The aggregated data for **ALL three resources** must be fetched **exactly once on application startup** and loaded into an in-memory store.

1.  Use a Spring Boot **`ApplicationRunner`** or **`CommandLineRunner`** component to initiate the data fetching process.

2.  The API endpoint (`GET /api/finance/data/{resourceType}`) must serve the data from this **in-memory store**, not by making a new call to the external API on every request.

3.  The in-memory storage mechanism (e.g., a service holding the data) must be designed to be **thread-safe** and ensure the data is **immutable** once the `ApplicationRunner` has finished loading it.

## III. Production Readiness & Deliverables

Your final solution must demonstrate production quality through code, testing, and communication.

### 1. Robustness & Best Practices

* Graceful **Error Handling** for network failures or 4xx/5xx responses from the external API.

* Proper use of **Configuration Properties** (e.g., `application.yml`) for external service URLs.

* Clear separation of concerns (Controller, Service, Model/DTO, etc.).

### 2. Testing

* **Unit Tests** for all three `IDRDataFetcher` strategy implementations, ensuring data calculation and transformation logic is covered (using mock clients for external calls).

* **Integration Tests** to verify the `ApplicationRunner` successfully initializes and loads the data into the in-memory store before the application context is ready.

### 3. Documentation

A clear `README.md` is mandatory. It must include:

* **Setup/Run Instructions:** Clear steps to clone, build, and run the application and tests.

* **Endpoint Usage:** Example cURL commands to test the three different resource types.

* **Personalization Note:** Clearly state your GitHub username and show the exact **Spread Factor** (e.g., `0.00765`) calculated by your function.
* ---

* ### 🛠️ Architectural Rationale

  This section should contain a brief, but detailed, explanation answering the following questions:

   1.  **Polymorphism Justification:** Explain *why* the Strategy Pattern was used over a simpler conditional block in the service layer for handling the multi-resource endpoint. Discuss the benefits in terms of **extensibility** and **maintainability**.

   2.  **Client Factory:** Explain the specific role and benefit of using a **`FactoryBean`** to construct the external API client. Why is this preferable to defining the client using a standard `@Bean` method in this scenario?

   3.  **Startup Runner Choice:** Justify the choice of using an `ApplicationRunner` (or `CommandLineRunner`) for the initial data ingestion over a simpler `@PostConstruct` method.

## 🛠️ Architectural Rationale — Implementasi pada Proyek Ini

1) Mengapa Strategy Pattern?
- Endpoint menerima tiga resource berbeda dengan aturan transformasi yang berbeda. Dengan Strategy Pattern, tiap resource diwakili oleh implementasi `IDRDataFetcher` yang terpisah (`LatestIdrRatesFetcher`, `HistoricalIdrUsdFetcher`, `SupportedCurrenciesFetcher`).
- Seleksi strategi dilakukan secara dinamis melalui map-based lookup yang di-inject Spring di Controller, tanpa if/else atau switch, sehingga mudah ditambah resource baru tanpa menyentuh controller/service lain.
- Keterpisahan ini meningkatkan testability (unit test per strategi) dan menjaga Single Responsibility, sehingga maintainability meningkat.

2) Mengapa FactoryBean untuk WebClient?
- `FrankfurterWebClientFactoryBean` bertanggung jawab membuat dan mengkonfigurasi `WebClient` sekali, menggunakan `FrankfurterProperties` untuk base URL dan timeout. Ini menyatukan pembuatan client dan konfigurasi eksternal di satu tempat yang tervalidasi.
- Berbeda dengan sekadar `@Bean`, `FactoryBean` memberi fleksibilitas lifecycle, lazy creation, dan enkapsulasi kompleksitas instansiasi sehingga konfigurasi tetap bersih dan mudah diuji.

3) Mengapa ApplicationRunner untuk preloading data?
- Kebutuhan bisnis: seluruh data dari ketiga resource harus diambil tepat sekali saat startup, lalu disajikan dari in-memory store secara immutable dan thread-safe.
- `ApplicationRunner` memastikan proses ini terjadi setelah konteks Spring siap dan dependency terinisialisasi sempurna, lebih dapat diandalkan dibanding `@PostConstruct` yang dapat berjalan terlalu awal pada fase bean initialization.
- Dengan pendekatan ini, endpoint tidak memanggil API eksternal per request dan latensi menjadi konsisten.

## IV. Submission & Review Process

1.  **Fork** this repository.

2.  Implement your solution on a dedicated feature branch (e.g., `feat/idr-rate-aggregator`).

3.  When complete, submit your solution via a **Pull Request (PR)** back to the main repository.

**Your PR will be evaluated on the following:**

* **Commit History:** Clean, atomic, and descriptive commit messages (e.g., "feat: Implement IDR latest rates strategy," "fix: Correctly calculate IDR spread in tests").

* **PR Description:** The description must clearly summarize the solution and **must contain the full answers** to the three "Architectural Rationale" questions from Section III.

* **Code Review Readiness:** The code should be well-structured and ready for immediate review.

Good luck!