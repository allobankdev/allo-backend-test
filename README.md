# Allo Split Bill API

A REST API for managing shared expenses. Users have global `@username` identities, can join multiple groups, split expenses in several ways, view bills across groups, and record payments in an immutable ledger.

## Features

- Global users and group memberships.
- Equal, exact, and percentage expense splits, including subsets of group members.
- No tax, exclusive tax, or inclusive tax, with cent-level reconciliation.
- Deterministic settlement balances and suggested transfers.
- Append-only payment ledger with reversal entries.
- Personal bills dashboard grouped by currency.
- H2 file database, request validation, consistent JSON errors, OpenAPI/Swagger UI, and Docker.

## Requirements

- Java 21 or later.
- Docker is optional.
- Maven is not required separately; the Maven Wrapper downloads the project Maven version.

## Run locally

```powershell
# Windows PowerShell
.\mvnw.cmd spring-boot:run
```

```bash
# Linux/macOS
./mvnw spring-boot:run
```

The API listens at `http://localhost:4110`.

- Swagger UI: `http://localhost:4110/swagger-ui.html`
- OpenAPI JSON: `http://localhost:4110/v3/api-docs`

The local H2 database is stored in `data/` and persists between runs. Stop the application before removing that folder to reset the database. The application creates or upgrades the schema at startup.

## Build and test

```powershell
.\mvnw.cmd clean verify
```

```bash
./mvnw clean verify
```

## Run with Docker

```bash
docker build -t allo-split-bill .
docker run --rm -p 4110:4110 allo-split-bill
```

## Configuration

`src/main/resources/application.properties` is Spring Boot's application configuration file. It sets the server port, database connection, JSON naming style, and H2 console path. Database credentials and the JDBC URL can be overridden with environment variables; the `${VARIABLE:default}` syntax means “use this environment variable, or use the value after `:` if it is unset.” The `app.github-username` setting controls the username used to calculate the service charge.

| Environment variable | Default | Purpose |
| --- | --- | --- |
| `DATABASE_URL` | `jdbc:h2:file:./data/splitbill;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `DATABASE_USERNAME` | `sa` | Database username |
| `DATABASE_PASSWORD` | empty | Database password |
| `GITHUB_USERNAME` | `KIJESHOES` | Username used to calculate the service charge |

## API examples

Set the base URL in Bash:

```bash
BASE_URL=http://localhost:4110
```

Create a user. The username must start with `@`. Both `displayName` (shown by Swagger UI) and `display_name` are accepted.

```bash
curl -i -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"@ayu","displayName":"Ayu"}'
```

Get a user and their bills:

```bash
curl "$BASE_URL/api/users/@ayu"
curl "$BASE_URL/api/users/@ayu/bills"
```

Create a group using previously created users:

```bash
curl -i -X POST "$BASE_URL/api/groups" \
  -H "Content-Type: application/json" \
  -d '{"name":"Bandung trip","currency":"IDR","participants":["@ayu","@bima"]}'
```

Use the returned `group_id` in the following requests. `GET /api/groups/{group_id}` returns the group's members and their usernames, which are used to select people in expense and payment requests.

```bash
# Get group details
curl "$BASE_URL/api/groups/GROUP_ID"

# Record an expense and split it equally across every group member
curl -i -X POST "$BASE_URL/api/groups/GROUP_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{"description":"Dinner","amount":150000,"paid_by":"@ayu","split_type":"EQUAL"}'

# Record an expense split equally only between selected usernames
curl -i -X POST "$BASE_URL/api/groups/GROUP_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{"description":"Taxi","amount":60000,"paid_by":"@ayu","split_type":"EQUAL","split_between":["@ayu","@bima"]}'

# Assign exact portions using usernames
curl -i -X POST "$BASE_URL/api/groups/GROUP_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{"description":"Dinner","amount":150000,"paid_by":"@ayu","split_type":"EXACT","shares":[{"username":"@ayu","amount":100000},{"username":"@bima","amount":50000}]}'

# List expenses and get one expense
curl "$BASE_URL/api/groups/GROUP_ID/expenses"
curl "$BASE_URL/api/groups/GROUP_ID/expenses/EXPENSE_ID"

# Get settlement
curl "$BASE_URL/api/groups/GROUP_ID/settlement"

# Record a payment between group members
curl -i -X POST "$BASE_URL/api/groups/GROUP_ID/payments" \
  -H "Content-Type: application/json" \
  -d '{"payer":"@bima","receiver":"@ayu","amount":50000}'

# List payments and reverse one payment
curl "$BASE_URL/api/groups/GROUP_ID/payments"
curl -i -X POST "$BASE_URL/api/groups/GROUP_ID/payments/PAYMENT_ID/reversal" \
  -H "Content-Type: application/json"
```

The full API contract is also available at `http://localhost:4110/v3/api-docs` when the application is running.

`split_between` is optional and applies only to `EQUAL` splits. Omit it to split across the whole group. Use `shares` with `EXACT` or `PERCENTAGE` when members owe different amounts. `paid_by`, `split_between`, each `shares[].username`, `payer`, and `receiver` accept group usernames.

## Money and settlement rules

- Monetary calculations use `BigDecimal` with two decimal places.
- Exclusive tax is added to the input amount; inclusive tax is extracted from it.
- Remainder cents are assigned deterministically so shares equal the expense total.
- Active payments reduce the outstanding debt between payer and receiver.
- Reversals add a compensating ledger entry and preserve the original payment.
- Service charge is reported separately and is not included in transfers because the challenge does not define a recipient.
- Settlement `transfers` include `from` and `to` participant IDs plus `from_username` and `to_username` handles, so clients can show who pays whom without a separate lookup. Personal bill group summaries also include `participant_username`.
- In `GET /api/groups/{group_id}/settlement`, a positive `balances[].balance` means the participant is owed money; a negative value means they owe money. Use `you_owe`, `you_are_owed`, and `status` for an explicit interpretation. For example, if Kije pays Rp10,000,000 and has a Rp2,500,000 share, Kije's net balance is `7,500,000`, `you_owe` is `0`, and `you_are_owed` is `7,500,000`; the suggested transfers go from the other participants to Kije.

## GitHub username and service charge

GitHub username: `KIJESHOES`.

```text
k + i + j + e + s + h + o + e + s
107 + 105 + 106 + 101 + 115 + 104 + 111 + 101 + 115 = 965
service_charge_pct = 965 % 10 = 5%
```

The application calculates this value at runtime from the lowercase username. `service_charge_amount` is 5% of `total_expenses` after tax.

## Design decision

The main design decision was to separate a user's global identity from their membership in a group. Expenses and payments refer to group membership IDs so that group boundaries are easy to validate; the global `@username` can be reused across groups. This adds a mapping step when building responses, while allowing a user's bills to be aggregated across groups without duplicating identity or changing transaction history.

## Limitations

he API models the main split-bill lifecycle:

1. Create a group with its members and currency.
2. Record an expense by specifying who paid, which members share it, and how to split it (`EQUAL`, `EXACT`, or `PERCENTAGE`). Optional tax is calculated as part of the expense.
3. Validate that the payer and everyone receiving a share belong to the group. Calculate each member's final share, reconcile rounding to the cent, and persist the expense and its shares together in one database transaction.
4. Calculate each member's net balance from the group's expenses, shares, and recorded payments. Return suggested transfers between debtors and creditors; these are settlement instructions, not money moved by the API.
5. Record a payment when members settle outside the system. The API checks that it goes from a current debtor to a current creditor and does not exceed the outstanding amount. A correction is recorded as a reversal entry so the original payment remains in the ledger.

This is the take-home scope: it implements bill calculation and settlement tracking, but not the surrounding production workflow. Requests identify group members directly, and the API does not authenticate the caller or prove that the caller may act for the named member. A production version should derive the acting identity from a session or token and authorize actions against group membership and roles. It would also need idempotency for retried writes, rate limiting, and an external audit/event system for durable change history and notifications. Payment records here represent reported settlements; integrating a payment provider and reconciling its transaction status is outside this project's scope.
