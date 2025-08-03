# Read Me First
Ini adalah project skeleton untuk backend test menggunakan Spring Boot.
Kandidat diharapkan untuk menambahkan kode diatas skeleton tersebut.

Dalam rangka menyambut Pemilu 2024 mari kita coba lebih mengenal lebih dekat para Calon Legislatif dengan membuat miniatur API untuk Caleg :)

Model/Entity yang sudah disiapkan:
- `Caleg`: Calon Legislatif
- `Partai`: Partai Pemilu
- `Dapil`: Daerah Pemilihan

# TODO
- Siapkan database script `.sql` dan letakkan di folder `resources/db`, boleh memilih database bebas
- Buat REST API untuk menampilkan list `Caleg`
    - Tambahkan filter cari berdasarkan `Dapil` dan `Partai`
    - Tambahkan sorting berdasarkan Nomor urut
- Buat Merge Request ke project ini

# Penilaian
- Kode yang dibuat adalah production ready
- Kode yang rapi dan mudah dimengerti
- Nilai tambah jika ditambahkan unit test

---

# Pemilu API - Backend Test Allo Bank

API REST untuk mengelola data Calon Legislatif (Caleg) dalam rangka menyambut Pemilu 2024.

## 📋 Daftar Isi

- [Fitur](#-fitur)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Instalasi](#-instalasi)
- [Menjalankan Aplikasi](#-menjalankan-aplikasi)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Struktur Project](#-struktur-project)
- [Database Schema](#-database-schema)
- [Troubleshooting](#-troubleshooting)

## ✨ Fitur

- ✅ REST API untuk menampilkan daftar Caleg
- ✅ Filter pencarian berdasarkan Dapil dan Partai
- ✅ Sorting berdasarkan nomor urut
- ✅ Pagination support
- ✅ Dockerized deployment
- ✅ Production-ready configuration
- ✅ Unit tests

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **PostgreSQL 15**
- **Docker & Docker Compose**
- **Maven**
- **Lombok**
- **MapStruct** (optional)
- **JUnit 5** & **Mockito** untuk testing

## 📦 Prerequisites

Pastikan sudah terinstall:
- Java 17 atau lebih baru
- Maven 3.6+
- Docker Desktop
- Git

## 🚀 Instalasi

### 1. Clone Repository

```bash
git clone <repository-url>
cd allo-backend-test
```

### 2. Build Project

```bash
./mvnw clean package -DskipTests
```

Untuk Windows:
```powershell
./mvnw.cmd clean package -DskipTests
```

## 🏃‍♂️ Menjalankan Aplikasi

### Menggunakan Docker Compose

```bash
# Build dan jalankan semua services
docker-compose up --build

# Atau jalankan di background
docker-compose up -d --build
```

Aplikasi akan berjalan di: `http://localhost:8080/api`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Get All Caleg
Menampilkan daftar semua Caleg dengan support pagination dan filtering.

```http
GET /api/caleg
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| dapilId | UUID | No | Filter berdasarkan ID Dapil |
| partaiId | UUID | No | Filter berdasarkan ID Partai |
| page | Integer | No | Nomor halaman (default: 0) |
| size | Integer | No | Jumlah item per halaman (default: 20) |
| sort | String | No | Sorting field dan direction (default: nomorUrut,ASC) |

**Example Request:**
```bash
# Get all caleg
curl http://localhost:8080/api/caleg

# Filter by dapil
curl "http://localhost:8080/api/caleg?dapilId=650e8400-e29b-41d4-a716-446655440001"

# Filter by partai with pagination
curl "http://localhost:8080/api/caleg?partaiId=550e8400-e29b-41d4-a716-446655440001&page=0&size=5"

# Multiple filters with sorting
curl "http://localhost:8080/api/caleg?dapilId=650e8400-e29b-41d4-a716-446655440001&partaiId=550e8400-e29b-41d4-a716-446655440001&sort=nomorUrut,DESC"
```

**Example Response:**
```json
{
  "content": [
    {
      "id": "750e8400-e29b-41d4-a716-446655440001",
      "dapil": {
        "id": "650e8400-e29b-41d4-a716-446655440001",
        "namaDapil": "DKI Jakarta 1",
        "provinsi": "DKI Jakarta"
      },
      "partai": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "namaPartai": "Partai Demokrasi Indonesia Perjuangan",
        "nomorUrut": 1
      },
      "nomorUrut": 1,
      "nama": "Budi Santoso",
      "jenisKelamin": "LAKILAKI"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "ascending": true
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 8,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 8
}
```

#### 2. Get Caleg by ID
Menampilkan detail Caleg berdasarkan ID.

```http
GET /api/caleg/{id}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | UUID | Yes | ID Caleg |

**Example Request:**
```bash
curl http://localhost:8080/api/caleg/750e8400-e29b-41d4-a716-446655440001
```

**Example Response:**
```json
{
  "id": "750e8400-e29b-41d4-a716-446655440001",
  "dapil": {
    "id": "650e8400-e29b-41d4-a716-446655440001",
    "namaDapil": "DKI Jakarta 1",
    "provinsi": "DKI Jakarta"
  },
  "partai": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "namaPartai": "Partai Demokrasi Indonesia Perjuangan",
    "nomorUrut": 1
  },
  "nomorUrut": 1,
  "nama": "Budi Santoso",
  "jenisKelamin": "LAKILAKI"
}
```

## 🧪 Testing


### Run Specific Test Class
```bash
./mvnw test -Dtest=CalegControllerTest
```

Report akan tersedia di: `target/site/jacoco/index.html`

## 📁 Struktur Project

```
allo-backend-test/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/allobank/allobackendtest/
│   │   │       ├── controller/
│   │   │       │   └── CalegController.java
│   │   │       ├── dto/
│   │   │       │   ├── CalegDTO.java
│   │   │       │   ├── CalegFilterDTO.java
│   │   │       │   ├── DapilDTO.java
│   │   │       │   └── PartaiDTO.java
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── ResourceNotFoundException.java
│   │   │       ├── mapper/
│   │   │       │   └── CalegMapper.java
│   │   │       ├── model/
│   │   │       │   ├── Caleg.java
│   │   │       │   ├── Dapil.java
│   │   │       │   ├── JenisKelamin.java
│   │   │       │   └── Partai.java
│   │   │       ├── repository/
│   │   │       │   └── CalegRepository.java
│   │   │       ├── service/
│   │   │       │   ├── CalegService.java
│   │   │       │   └── impl/
│   │   │       │       └── CalegServiceImpl.java
│   │   │       └── AlloBackendTestApplication.java
│   │   └── resources/
│   │       ├── db/
│   │       │   ├── schema.sql
│   │       │   └── data.sql
│   │       ├── application.yml
│   │       └── application-docker.yml
│   └── test/
│       └── java/
│           └── com/allobank/allobackendtest/
│               ├── controller/
│               │   └── CalegControllerTest.java
│               └── service/
│                   └── CalegServiceTest.java
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🗄 Database Schema

### Tables

#### partai
| Column | Type | Constraints |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| nama_partai | VARCHAR(255) | NOT NULL |
| nomor_urut | INTEGER | NOT NULL, UNIQUE |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### dapil
| Column | Type | Constraints |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| nama_dapil | VARCHAR(255) | NOT NULL |
| provinsi | VARCHAR(255) | NOT NULL |
| wilayah_dapil_list | TEXT | NOT NULL (JSON array) |
| jumlah_kursi | INTEGER | NOT NULL |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### caleg
| Column | Type | Constraints |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| dapil_id | UUID | NOT NULL, FK to dapil(id) |
| partai_id | UUID | NOT NULL, FK to partai(id) |
| nomor_urut | INTEGER | NOT NULL |
| nama | VARCHAR(255) | NOT NULL |
| jenis_kelamin | VARCHAR(20) | NOT NULL, CHECK IN ('LAKILAKI', 'PEREMPUAN') |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

### Sample Data

Database akan otomatis di-populate dengan sample data saat aplikasi pertama kali dijalankan, termasuk:
- 5 Partai Politik
- 4 Dapil (DKI Jakarta dan Jawa Barat)
- 8 Caleg

## 🔧 Configuration

### Application Properties

**application.yml:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pemilu_db
    username: pemilu_user
    password: pemilu_pass
    
server:
  port: 8080
  servlet:
    context-path: /api
```

**application-docker.yml:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/pemilu_db
```

### Environment Variables

Untuk Docker deployment:
- `DB_USERNAME`: Database username (default: pemilu_user)
- `DB_PASSWORD`: Database password (default: pemilu_pass)
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (set to 'docker' for containerized deployment)

## ❗ Troubleshooting

### 1. Docker build error
```bash
# Clean Docker cache
docker system prune -a

# Rebuild tanpa cache
docker-compose build --no-cache
```

### 2. Database connection error
```bash
# Check if PostgreSQL is running
docker ps

# Check PostgreSQL logs
docker logs pemilu-postgres
```

### 3. Port already in use
```bash
# Stop existing services
docker-compose down

# Or change port in docker-compose.yml
```

### 4. Maven build error
```bash
# Clean and rebuild
./mvnw clean install -DskipTests

# If still error, delete .m2 cache
rm -rf ~/.m2/repository
```

## 📝 Notes

- Database schema dan initial data akan otomatis dibuat saat aplikasi pertama kali dijalankan
- Aplikasi menggunakan HikariCP untuk connection pooling
- Logging level dapat diatur melalui application.yml
- Untuk production deployment, pastikan untuk mengubah database credentials

## 🤝 Contributing

1. Fork repository
2. Buat feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

## 📄 License

This project is created for Allo Bank backend test purposes.

---
Made with ❤️ for Allo Bank Backend Test