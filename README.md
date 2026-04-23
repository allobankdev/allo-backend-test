# Allo Bank Backend Developer Take-Home Test

Welcome, and thank you for your interest in joining Allo Bank Engineering!

This challenge is intentionally open-ended. There is no skeleton, no guided steps, and no single correct answer. We want to see how you think, how you structure a solution, and what you consider important in production-grade code.

---

## The Challenge: Split Bill API

Build a **Spring Boot REST API** that helps a group of people manage shared expenses and calculate who owes whom at the end.

Think of a real scenario: a group trip, a team lunch, a shared apartment. People take turns paying for things, and at the end someone needs to figure out the fairest way to settle up.

**Your API should, at minimum, support:**

1. Creating a bill group with a name and a list of participants
2. Adding expenses to a group — who paid, how much, and who it was for
3. Retrieving a settlement summary — a clear breakdown of who owes whom and how much

Everything else is up to you.

---

## Technical Requirements

These are non-negotiable:

- **Java 17+**, **Spring Boot**, **Maven**
- **`BigDecimal`** for all monetary values — no `float` or `double`
- **A `Dockerfile`** using a multi-stage build (see `Dockerfile.template` in this repo)
- At least **one unit test** covering your settlement calculation logic
- A **`README.md`** in your submission with:
  - How to build and run your project
  - Example `curl` commands for each endpoint
  - Your **GitHub username** and your calculated **service charge** value (see Personalization section below)
  - Answer to the submission question (see below)

---

## Personalization

Every settlement response must include two additional fields: `service_charge_pct` and `service_charge_amount`.

The `service_charge_pct` is unique to you and is calculated as follows:

1. Take your GitHub username in **lowercase**
2. Sum the Unicode (ASCII) values of all characters
3. `service_charge_pct = (sum % 10)` — this gives a value between 0 and 9 (representing a percentage)

**Example:** GitHub username `johndoe47`
- Unicode sum: `106+111+104+110+100+111+101+52+55` = `850`
- `service_charge_pct = 850 % 10` = **0** (0%)

The `service_charge_amount` is this percentage applied to the total group expenses.

Include both fields in your settlement response. This value must be computed in code — do not hardcode it.

---

## Show Your Skills

The minimum requirements get you through the door. What you build beyond that is how you stand out.

Some directions to explore — pick what interests you, or invent your own:

- **Multiple split strategies** — equal split, split by percentage, split by exact amount per person
- **Settlement optimization** — minimize the total number of transactions needed to settle all debts
- **Payment recording** — mark a debt as paid and update outstanding balances
- **Expense categories** — tag expenses (food, transport, accommodation) and show per-category summaries
- **Audit trail** — track when expenses and payments were added

There is no bonus point checklist. We are looking at the quality of what you choose to build, not the quantity.

---

## Submission Question

In your `README.md`, answer the following in a short paragraph (3–5 sentences):

> **"What was the hardest design decision you made while building this, and what trade-off did you accept?"**

There is no wrong answer. We ask this because it tells us more about how you think than the code itself.

---

## Submission Process

1. **Create a private GitHub repository** for your solution
2. **Add `allobankdev` as a collaborator** (Settings → Collaborators → Add people)
3. **Include a `Dockerfile`** in the root of your project (see `Dockerfile.template`)
4. **Submit via the form:** [Click Here](https://forms.gle/nZKQ2EjTCPfAKHog7)

   The form will ask for:
   - Your full name and contact details
   - Your private GitHub repository URL
   - Your GitHub username (for personalization verification)

> Do not open a Pull Request to this repository. Submissions are private.

---

## What We Look For

| Area | What it signals |
|---|---|
| Data modeling | How you think about domain entities and relationships |
| API design | Clarity, consistency, and REST conventions |
| Monetary handling | Awareness of precision issues in financial systems |
| Code structure | Separation of concerns, readability, maintainability |
| Testing | What you consider worth testing and why |
| Submission answer | Genuine engagement with the problem |

We review every submission before the interview. The interview will include questions directly about your code — be ready to walk through it and extend it live.

Good luck!

---

## Panduan Penggunaan (Setelah Clone)

### Quick Start

```bash
git clone <your-fork-url>
cd allo-backend-test
./mvnw spring-boot:run
```

Jalankan test:
```bash
./mvnw test
```

Aplikasi berjalan di port `8080` dan hanya mengambil data Frankfurter sekali saat startup.

### Setup

Pastikan Java 17+ terpasang. Lalu jalankan:

```bash
./mvnw clean package
```

### Run Aplikasi

```bash
./mvnw spring-boot:run
```

Atau jalankan file jar hasil build:

```bash
java -jar target/finance-aggregator-0.0.1-SNAPSHOT.jar
```

### Konfigurasi

Defaults live in `src/main/resources/application.yaml`:

```yaml
frankfurter:
  base-url: "https://api.frankfurter.app"
  github-username: "agilnurdiansah29"
  timeouts:
    connect-ms: 2000
    response-ms: 4000

finance:
  historical:
    start-date: "2024-01-01"
    end-date: "2024-01-05"
    from: "IDR"
    to: "USD"
```

### Tes API (cURL)

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```

### Ringkasan Endpoint

| Endpoint | Description |
| --- | --- |
| `GET /api/finance/data/latest_idr_rates` | Latest IDR rates with `USD_BuySpread_IDR` applied. |
| `GET /api/finance/data/historical_idr_usd` | Historical IDR→USD series for the configured date range (filtered). |
| `GET /api/finance/data/supported_currencies` | List of supported currencies from Frankfurter. |

### Contoh Response Sukses

`latest_idr_rates`
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": [
    {
      "resourceType": "latest_idr_rates",
      "data": {
        "amount": 1.0,
        "base": "IDR",
        "date": "2026-01-28",
        "rates": {
          "USD": 0.000060,
          "EUR": 0.000050
        },
        "USD_BuySpread_IDR": 16764.8333333334
      }
    }
  ],
  "timestamp": "2026-01-29T06:00:00"
}
```

`historical_idr_usd` (sudah difilter sesuai range)
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": [
    {
      "resourceType": "historical_idr_usd",
      "data": {
        "amount": 1.0,
        "base": "IDR",
        "start_date": "2024-01-01",
        "end_date": "2024-01-05",
        "rates": {
          "2024-01-02": { "USD": 0.000064 },
          "2024-01-03": { "USD": 0.000064 }
        }
      }
    }
  ],
  "timestamp": "2026-01-29T06:00:00"
}
```

`supported_currencies`
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": [
    {
      "resourceType": "supported_currencies",
      "data": {
        "USD": "United States Dollar",
        "IDR": "Indonesian Rupiah"
      }
    }
  ],
  "timestamp": "2026-01-29T06:00:00"
}
```

### Contoh Response Error

Invalid resource type (`404`):
```json
{
  "success": false,
  "message": "Request failed",
  "error": "Unknown resourceType: foo",
  "timestamp": "2026-01-29T06:00:00"
}
```

External API issue (`502`):
```json
{
  "success": false,
  "message": "Request failed",
  "error": "Frankfurter API error: 502",
  "timestamp": "2026-01-29T06:00:00"
}
```

### Struktur Project

- `config` — configuration properties and WebClient FactoryBean
- `controller` — REST endpoint `/api/finance/data/{resourceType}`
- `strategy` — three fetcher strategies for each resource type
- `service` — Frankfurter client, spread calculation, in‑memory store
- `dto` — response DTOs
- `model` — external API response models
- `runner` — ApplicationRunner that loads data at startup
- `exception` — custom exceptions and global error handler

### Catatan Historical Data

Frankfurter kadang mengembalikan tanggal di luar range (biasanya karena hari kerja terdekat).
Di sini response historical **difilter** supaya hanya tanggal dalam range
`finance.historical.range` yang dikembalikan.

### Catatan Personalisasi

- GitHub username: `agilnurdiansah29`
- Spread factor: `0.00589`

### Alasan Arsitektur

1. **Strategy Pattern:** Saya pisahkan tiap resource ke class strategi sendiri supaya controller tetap bersih dan mudah dikembangkan. Kalau ada resource baru, cukup tambah satu strategi baru tanpa mengubah controller.
2. **FactoryBean Client:** Pembuatan WebClient disatukan di `FactoryBean` agar konfigurasi base URL dan timeout konsisten, dan lifecycle client-nya jelas di Spring.
3. **ApplicationRunner:** Dipakai supaya proses loading berjalan setelah context siap, lebih aman dibanding `@PostConstruct` yang dieksekusi saat bean dibuat.
