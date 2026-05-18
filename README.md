Youtube: https://www.youtube.com/@learnwithkenedy
email: kenedinovriansyah@gmail.com

# Split Bill API

A Spring Boot REST API for managing shared expenses and calculating settlements among a group of people.

## Build & Run

**Prerequisites:** Java 17+, Docker (optional)

### Local

```bash
GITHUB_USERNAME=kydevx ./mvnw spring-boot:run
```

### Docker

```bash
docker build -t split-bill-api .
docker run -p 4110:4110 -e GITHUB_USERNAME=kydevx split-bill-api
```

## Example Curl Commands

### 1. Create a group

```bash
curl -X POST http://localhost:4110/api/groups \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Weekend Trip",
    "participants": ["Alice", "Bob", "Charlie"]
  }'
```

Response includes the `id` needed for subsequent requests.

### 2. Add an expense

```bash
curl -X POST http://localhost:4110/api/groups/{groupId}/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "paidBy": "Alice",
    "amount": 60.00,
    "description": "Pizza dinner",
    "splitAmong": ["Alice", "Bob", "Charlie"]
  }'
```

### 3. Get settlement summary

```bash
curl http://localhost:4110/api/groups/{groupId}/settlement
```

Sample response:

```json
{
  "groupId": "...",
  "groupName": "Weekend Trip",
  "totalExpenses": 60.0,
  "debts": [
    { "from": "Bob", "to": "Alice", "amount": 20.0 },
    { "from": "Charlie", "to": "Alice", "amount": 20.0 }
  ],
  "serviceChargePct": 7,
  "serviceChargeAmount": 4.2
}
```

## Personalization

- **GitHub Username:** kydevx
- **Service Charge Calculation:**
  - Unicode sum: 107+121+100+101+118+120 = 667
  - `service_charge_pct = 667 % 10 = 7` (7%)
- The `service_charge_amount` is 7% of the total group expenses, computed in code at runtime.

## Submission Question

**"What was the hardest design decision you made while building this, and what trade-off did you accept?"**

The hardest decision was choosing between an in-memory store and a database. I chose in-memory (ConcurrentHashMap) to keep the setup zero-config and focus the evaluation on the API design and settlement algorithm rather than infrastructure wiring. The trade-off is that all data is lost on restart, but the problem's requirements didn't specify persistence, and the settlement logic is the core deliverable. If this were production, I'd swap in a database repository — the service layer is already fully decoupled from storage through the repository interface, so the migration would be straightforward.
