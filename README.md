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


# Solution Overview

This section describes my implementation for the Allo Bank Backend Developer Take-Home Test.

The application is built using Spring Boot and follows a clean, layered architecture
with a strong emphasis on extensibility, testability, and production readiness.

## Setup & Run Instructions

### Prerequisites
- Java 17+
- Maven 3+

### Clone Repository
```bash
git clone <your-fork-repo-url>
cd <project-folder>

### Run Application
./mvnw spring-boot:run

### Run Tests
./mvnw clean test

### API Usage Examples
# Latest IDR Rates
curl http://localhost:8080/api/finance/data/latest_idr_rates

# Historical IDR to USD
curl http://localhost:8080/api/finance/data/historical_idr_usd

# Supported Currencies
curl http://localhost:8080/api/finance/data/supported_currencies

### Architectural Rationale
#1. Why Strategy Pattern?
The Strategy Pattern is used to encapsulate each resource type
(latest IDR rates, historical IDR-USD data, supported currencies)
into independent components.

This avoids conditional logic and makes the system easily extensible
without modifying existing code.

# 2. Why use FactoryBean for API Client?
The FactoryBean is used to centralize the construction and configuration
of the external API client.

This allows external configuration via application.yml and avoids
duplicated setup logic across the application.

# 3. Why ApplicationRunner?
ApplicationRunner guarantees that all required data is fetched
once at application startup and stored in an immutable, thread-safe
in-memory store.

This ensures consistent responses and avoids unnecessary calls
to external APIs during runtime.