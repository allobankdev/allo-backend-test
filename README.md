# Allo Bank Split Bill REST API

A production-grade **Spring Boot 3 REST API** built for the **Allo Bank Engineering Take-Home Challenge**. This solution helps groups of people manage shared expenses, record debt settlements, and compute optimized, fair pelunasan breakdown (minimizing total transaction count).

---

## Technical Features

- **Java 17 / 21 & Spring Boot 3.3.3**
- **Strict Monetary Handling**: All monetary amounts use `BigDecimal` with 2 decimal places (`RoundingMode.HALF_UP`) and explicit zero-sum conservation.
- **Dynamic Personalization Service Charge**: Automatically computes `service_charge_pct` and `service_charge_amount` based on the candidate's GitHub username ASCII sum.
- **Debt Simplification Algorithm**: Greedy optimization algorithm pairing maximum debtors with maximum creditors to minimize the total number of transactions required to settle all debts.
- **Multiple Split Strategies**: Supports `EQUAL` split, `EXACT` amount split, and `PERCENTAGE` split.
- **Direct Settlement Payment Recording**: Endpoint to record payments between participants to settle balances.
- **Multi-Stage Dockerfile**: Fast and lightweight container builds on port `4110`.
- **Interactive OpenAPI / Swagger UI**: Built-in interactive API documentation at `/swagger-ui.html`.

---

## Personalization Details

- **GitHub Username**: `resa-rm`
- **ASCII Calculation**:
  - `r` (114) + `e` (101) + `s` (115) + `a` (97) + `-` (45) + `r` (114) + `m` (109) = **695**
- **Calculated `service_charge_pct`**: `695 % 10` = **`5`** (**5%**)
- *Note*: This value is dynamically calculated in code via `PersonalizationService` and included in every settlement summary response.

---

## Submission Question Answer

> **"What was the hardest design decision you made while building this, and what trade-off did you accept?"**

> The hardest design decision was balancing fractional expense precision using `BigDecimal` with a deterministic, zero-sum debt simplification algorithm. When dividing non-divisible amounts (such as $100 split 3 ways), fractional rounding drift can cause subtle balance discrepancies across participants. I accepted the trade-off of deterministically allocating any fractional rounding remainder to the primary split participant, preserving strict zero-sum balance conservation across the group while enabling an efficient $O(N \log N)$ greedy debt minimization process.

---

## How to Build & Run

### Method 1: Running with Maven Wrapper (Local)

1. Make sure Java 17+ is installed.
2. Build and run unit tests:
   ```bash
   ./mvnw clean test
   ```
3. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The application will start on **`http://localhost:4110`**.

### Method 2: Running with Docker

1. Build the Docker image:
   ```bash
   docker build -t allo-split-bill .
   ```
2. Run the container:
   ```bash
   docker run -p 4110:4110 allo-split-bill
   ```
   The API is accessible at **`http://localhost:4110`**.

---

## Interactive Swagger UI

Once the application is running, open your browser and navigate to:
👉 **`http://localhost:4110/swagger-ui.html`** or **`http://localhost:4110/h2-console`**

---

## Example `curl` Commands

### 1. Create a Bill Group
```bash
curl -X POST http://localhost:4110/api/v1/groups \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Liburan Bali",
    "participants": ["Resa", "Budi", "Siti"]
  }'
```

### 2. Get Group Details
```bash
curl -X GET http://localhost:4110/api/v1/groups/1
```

### 3. List All Groups
```bash
curl -X GET http://localhost:4110/api/v1/groups
```

### 4. Add Expense (Equal Split Example)
```bash
curl -X POST http://localhost:4110/api/v1/groups/1/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Makan Siang Resto",
    "totalAmount": 300.00,
    "paidByParticipantId": 1,
    "splitType": "EQUAL"
  }'
```

### 5. Add Expense (Percentage Split Example)
```bash
curl -X POST http://localhost:4110/api/v1/groups/1/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Sewa Mobil",
    "totalAmount": 200.00,
    "paidByParticipantId": 2,
    "splitType": "PERCENTAGE",
    "splits": [
      {"participantId": 1, "percentage": 50.00},
      {"participantId": 2, "percentage": 25.00},
      {"participantId": 3, "percentage": 25.00}
    ]
  }'
```

### 6. List Expenses in Group
```bash
curl -X GET http://localhost:4110/api/v1/groups/1/expenses
```

### 7. Get Settlement Summary (Breakdown & Service Charge)
```bash
curl -X GET http://localhost:4110/api/v1/groups/1/settlement
```

### 8. Record a Direct Settlement Payment
```bash
curl -X POST http://localhost:4110/api/v1/groups/1/payments \
  -H "Content-Type: application/json" \
  -d '{
    "fromParticipantId": 3,
    "toParticipantId": 1,
    "amount": 100.00,
    "notes": "Pelunasan via Transfer Bank"
  }'
```

### 9. List Recorded Payments
```bash
curl -X GET http://localhost:4110/api/v1/groups/1/payments
```
