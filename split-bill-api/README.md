
---

# Professional README.md

# Split Bill API

## Overview

Split Bill API is a Spring Boot REST API for managing shared group expenses and calculating optimized settlements between participants.

Built for the Allo Bank Backend Developer Take-Home Test.

---

# Features

* Create bill groups
* Add participants
* Add expenses
* Multiple split strategies
* Settlement optimization
* Service charge calculation
* Swagger documentation
* Docker support
* Unit testing

---

# Tech Stack

* Java 21
* Spring Boot 3
* Maven
* H2 Database
* Spring Data JPA
* Swagger/OpenAPI
* Docker

---

---
# Professional Repository Structure

```text
split-bill-api/
├── Dockerfile
├── README.md
├── pom.xml
├── .gitignore
├── .dockerignore
├── mvnw
├── mvnw.cmd
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── jokokusnandi
│   │   │           └── splitbill
│   │   │               ├── SplitBillApplication.java
│   │   │               │
│   │   │               ├── config
│   │   │               │   └── OpenApiConfig.java
│   │   │               │
│   │   │               ├── controller
│   │   │               │   ├── GroupController.java
│   │   │               │   ├── ExpenseController.java
│   │   │               │   └── SettlementController.java
│   │   │               │
│   │   │               ├── dto
│   │   │               │   ├── request
│   │   │               │   └── response
│   │   │               │
│   │   │               ├── entity
│   │   │               │   ├── BillGroup.java
│   │   │               │   ├── Participant.java
│   │   │               │   ├── Expense.java
│   │   │               │   ├── ExpenseSplit.java
│   │   │               │   └── BaseEntity.java
│   │   │               │
│   │   │               ├── enums
│   │   │               │   └── SplitStrategy.java
│   │   │               │
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── NotFoundException.java
│   │   │               │   └── BadRequestException.java
│   │   │               │
│   │   │               ├── mapper
│   │   │               │   └── GroupMapper.java
│   │   │               │
│   │   │               ├── repository
│   │   │               │   ├── GroupRepository.java
│   │   │               │   ├── ParticipantRepository.java
│   │   │               │   ├── ExpenseRepository.java
│   │   │               │   └── ExpenseSplitRepository.java
│   │   │               │
│   │   │               ├── service
│   │   │               │   ├── GroupService.java
│   │   │               │   ├── ExpenseService.java
│   │   │               │   ├── SettlementService.java
│   │   │               │   └── impl
│   │   │               │
│   │   │               ├── settlement
│   │   │               │   ├── SettlementCalculator.java
│   │   │               │   ├── BalanceSheet.java
│   │   │               │   ├── TransactionResult.java
│   │   │               │   └── SettlementOptimizer.java
│   │   │               │
│   │   │               └── util
│   │   │                   └── ServiceChargeUtil.java
│   │   │
│   │   └── resources
│   │       ├── application.yml
│   │       └── openapi.yml
│   │
│   └── test
│       └── java
│           └── com
│               └── jokokusnandi
│                   └── splitbill
│                       ├── service
│                       │   └── SettlementServiceTest.java
│                       └── settlement
│                           └── SettlementOptimizerTest.java
```

---

# ERD / Database Schema

```text
BillGroup
---------
id (PK)
name
created_at
updated_at

Participant
-----------
id (PK)
group_id (FK)
name
created_at
updated_at

Expense
-------
id (PK)
group_id (FK)
description
paid_by_participant_id (FK)
amount
split_strategy
created_at
updated_at

ExpenseSplit
------------
id (PK)
expense_id (FK)
participant_id (FK)
amount
percentage
created_at
updated_at
```

---

# Run Application

## Maven

```bash
mvn spring-boot:run
```
```bash
mvn clean compile spring-boot:run
```

---

---
# Cara Akses H2 Database Jika Aplikasi Berhasil Menyala

Jika langkah di atas berhasil mengoreksi kesalahan paket impor, log terminal akan memunculkan status Tomcat started on port 4110. Database dalam-memori H2 Anda otomatis aktif dan siap dibuka:
1. Buka browser internet Anda dan akses tautan: http://localhost:4110/h2-console
2. Isi formulir parameter koneksi persis seperti berikut:
- Driver Class: org.h2.Driver
- JDBC URL: jdbc:h2:mem:splitbill;IFEXISTS=FALSE
- User Name: sa
- Password: (Biarkan kosong)
3. Klik tombol Connect.
---


# Docker

```bash
docker build -t split-bill-api .

# Build repeatly
docker build --no-cache -t split-bill-api .

docker run -p 4110:4110 split-bill-api

# Running again
docker run -p 4110:4110 -e SERVER_PORT=4110 split-bill-api
```

---

# Swagger

```text
http://localhost:4110/swagger-ui.html
```

---

# Example cURL

## Create Group

```bash
curl --location 'http://localhost:4110/api/groups' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Bali Trip",
    "participants":["Joko","Budi","Sinta"]
}'
```

## Add Expense

```bash
curl --location 'http://localhost:4110/api/groups/1/expenses' \
--header 'Content-Type: application/json' \
--data '{
    "description":"Dinner",
    "amount":600,
    "paidBy":1,
    "splitStrategy":"EQUAL",
    "participantIds":[1,2,3]
}'
```

## Get Settlement

```bash
curl --location \
'http://localhost:4110/api/groups/1/settlement'
```

---

# Service Charge

GitHub Username:

```text
JokoKusnandi
```

Calculated:

```text
1296 % 10 = 6%
```

---

# Design Decision

One of the hardest design decisions was designing the settlement engine to support extensible split strategies while keeping the calculation logic clean and maintainable. I chose to separate settlement optimization into its own domain component instead of tightly coupling it with the service layer. The trade-off was additional abstraction and more classes, but it significantly improved scalability and testability of the business logic.

