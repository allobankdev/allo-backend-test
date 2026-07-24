# Allo Bank Backend Test - Split Bill API

This is a REST API built with Spring Boot to help a group of people manage shared expenses and calculate the optimal way to settle up debts, minimizing the total number of transactions.

## Setup and Run

### Prerequisites
- Java 17 or higher
- Maven (or use the included wrapper, if provided)

### Running Locally
To run the application locally on port `4110`:
```bash
./mvnw spring-boot:run
```
*(If you are on Windows, you can use `mvnw.cmd spring-boot:run`)*

### Running with Docker
The project includes a multi-stage `Dockerfile`.
To build and run the Docker image:
```bash
docker build -t splitbill-api .
docker run -p 4110:4110 splitbill-api
```

## Personalization
- **Author**: Abdul Rahmat
- **Email**: abdulrahmat97dev@gmail.com
- **GitHub Username**: NekoSukuriputo
- **Service Charge Percentage**: `0%` (ASCII sum of 'nekosukuriputo' is 1560. `1560 % 10 = 0`).

## Submission Question

> **"What was the hardest design decision you made while building this, and what trade-off did you accept?"**

The hardest design decision was determining how to store balances and calculate settlements. A naive approach would record every expense as a direct 1-to-many debt, creating a tangled web of transactions (e.g., A owes B, B owes C, C owes A). To solve this, I chose to calculate the *net balance* for each participant across all expenses and then use a greedy algorithm to match the largest debtors with the largest creditors. The trade-off is that the final settlement transactions don't trace back to specific expenses (e.g., "Andi pays Citra for Dinner"). Instead, they represent the mathematically optimal way to settle all debts globally.

## API Documentation & Examples

### 1. Create a Bill Group
```bash
curl -X POST http://localhost:4110/api/groups \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Liburan ke Bali",
    "participants": ["Andi", "Budi", "Citra"]
  }'
```
**Response:**
```json
{
  "id": "uuid-here",
  "name": "Liburan ke Bali",
  "participants": ["Andi", "Budi", "Citra"]
}
```

### 2. Add an Expense
```bash
# Assuming the Group ID is 'uuid-here'
curl -X POST http://localhost:4110/api/groups/uuid-here/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Makan Malam",
    "paidBy": "Andi",
    "amount": 150000.00,
    "splitAmong": ["Andi", "Budi", "Citra"]
  }'
```

### 3. Get Settlement Summary
```bash
# Assuming the Group ID is 'uuid-here'
curl -X GET http://localhost:4110/api/groups/uuid-here/settlement
```
**Example Response:**
```json
{
  "groupId": "uuid-here",
  "totalExpenses": 150000.00,
  "serviceChargePct": 0,
  "serviceChargeAmount": 0.00,
  "transactions": [
    { "from": "Budi", "to": "Andi", "amount": 50000.00 },
    { "from": "Citra", "to": "Andi", "amount": 50000.00 }
  ]
}
```
