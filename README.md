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



# Allobank Backend Test

Aplikasi backend ini menyediakan API untuk mengelola data Caleg, Dapil, dan Partai. Aplikasi ini dibangun menggunakan Spring Boot dan memanfaatkan JPA untuk interaksi dengan database.

## Persiapan Sebelum Menjalankan Aplikasi

1. **Clone Repository**
   ```
   git clone
   cd allobank-backend-test
   ```

2. **Konfigurasi Database**
   Pastikan telah mengonfigurasi `application.properties` sesuai dengan pengaturan database. Contoh konfigurasi untuk MySQL:

   ```properties
    spring.application.name=caleg
    spring.datasource.url=jdbc:mysql://localhost:3306/pemilu
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    #spring.jpa.hibernate.ddl-auto=update
    server.port=8081
   ```

3. **Install Dependencies**
   Jalankan perintah berikut untuk mengunduh semua dependencies:
   ```
   mvn clean install
   ```

## 1. Membuat Tabel di Database

Sebelum menjalankan aplikasi, perlu membuat tabel di dalam database. Spring Boot dapat secara otomatis membuat tabel berdasarkan entitas yang ada, jika telah mengonfigurasi aktifkan `spring.jpa.hibernate.ddl-auto=update` di `application.properties`.

Namun, jika ingin membuat tabel secara manual, jalankan semua query di dalam folder resources/db


## 2. Menambahkan Data Dummy ke Tabel

Setelah tabel dibuat, dapat menambahkan data dummy untuk menguji aplikasi. Berikut adalah contoh SQL untuk memasukkan data dummy ke dalam tabel `Dapil`, `Partai`, dan `Caleg` atau terdapat pada direktory resources/db:

```sql
USE pemilu;
-- Insert Data Dapil
INSERT INTO dapil
    (id, nama_dapil, provinsi, jumlah_kursi, wilayah_dapil_list)
VALUES
    ('123e4567-e89b-12d3-a456-426614174113', 'DKI JAKARTA I', 'DKI Jakarta', 6, "Jakarta Timur"),
    ('123e4567-e89b-12d3-a456-426614174114', 'DKI JAKARTA II', 'DKI Jakarta', 7, "Jakarta Pusat, Jakarta Selatan"),
    ('123e4567-e89b-12d3-a456-426614174115', 'DKI JAKARTA III', 'DKI Jakarta', 8, "Kep. Seribu, Jakarta Utara, Jakarta Barat");

-- Insert Data Partai
INSERT INTO partai
    (id, nama_partai, nomor_urut)
VALUES
    ('123e4567-e89b-12d3-a456-426614174222', 'Partai A', 1),
    ('123e4567-e89b-12d3-a456-426614174223', 'Partai B', 2),
    ('123e4567-e89b-12d3-a456-426614174224', 'Partai C', 3);


-- Insert Data Caleg
INSERT INTO caleg
    (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin)
VALUES
    ('123e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174113', '123e4567-e89b-12d3-a456-426614174222', 1, 'Nama AA', 'LAKILAKI'),
    ('123e4567-e89b-12d3-a456-426614174003', '123e4567-e89b-12d3-a456-426614174114', '123e4567-e89b-12d3-a456-426614174223', 2, 'Nama BB', 'PEREMPUAN'),
    ('123e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174115', '123e4567-e89b-12d3-a456-426614174224', 3, 'Nama CC', 'LAKILAKI');

```

## 3. Menjalankan Aplikasi

Setelah tabel dan data dummy dibuat, dapat menjalankan aplikasi dengan perintah berikut:

```bash
mvn spring-boot:run
```

Aplikasi akan berjalan pada `localhost:8081`.

## 4. Contoh URL untuk Menggunakan API

Berikut adalah beberapa contoh URL untuk memanggil API.

### a. Mengambil Semua Data Caleg dengan Pagination
Endpoint ini akan mengembalikan semua data Caleg dengan pagination. bisa menentukan ukuran halaman dan urutan pengambilan data menggunakan parameter `size` dan `sort`.

**URL:**
```
GET http://localhost:8081/api/caleg/list?size=10&sort=nomorUrut,asc
```

**Contoh Respons:**
```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174004",
      "dapil": {
        "id": "123e4567-e89b-12d3-a456-426614174115",
        "namaDapil": "DKI JAKARTA III",
        "provinsi": "DKI Jakarta",
        "wilayahDapilList": [
          "Kep. Seribu",
          "Jakarta Utara",
          "Jakarta Barat"
        ],
        "jumlahKursi": 8
      },
      "partai": {
        "id": "123e4567-e89b-12d3-a456-426614174224",
        "namaPartai": "Partai C",
        "nomorUrut": 3
      },
      "nomorUrut": 3,
      "nama": "Nama CC",
      "jenisKelamin": "LAKILAKI"
    },
    {
      "id": "123e4567-e89b-12d3-a456-426614174003",
      "dapil": {
        "id": "123e4567-e89b-12d3-a456-426614174114",
        "namaDapil": "DKI JAKARTA II",
        "provinsi": "DKI Jakarta",
        "wilayahDapilList": [
          "Jakarta Pusat",
          "Jakarta Selatan"
        ],
        "jumlahKursi": 7
      },
      "partai": {
        "id": "123e4567-e89b-12d3-a456-426614174223",
        "namaPartai": "Partai B",
        "nomorUrut": 2
      },
      "nomorUrut": 2,
      "nama": "Nama BB",
      "jenisKelamin": "PEREMPUAN"
    },
    {
      "id": "123e4567-e89b-12d3-a456-426614174002",
      "dapil": {
        "id": "123e4567-e89b-12d3-a456-426614174113",
        "namaDapil": "DKI JAKARTA I",
        "provinsi": "DKI Jakarta",
        "wilayahDapilList": [
          "Jakarta Timur"
        ],
        "jumlahKursi": 6
      },
      "partai": {
        "id": "123e4567-e89b-12d3-a456-426614174222",
        "namaPartai": "Partai A",
        "nomorUrut": 1
      },
      "nomorUrut": 1,
      "nama": "Nama AA",
      "jenisKelamin": "LAKILAKI"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 3,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 3,
  "first": true,
  "size": 10,
  "number": 0,
  "empty": false
}
```

### b. Mengambil Data Caleg Berdasarkan Filter `dapil_id` dan `partai_id`
Endpoint ini akan mengembalikan data Caleg berdasarkan filter `dapil_id` dan `partai_id` dengan pagination dan sorting.

**URL:**
```
GET http://localhost:8081/api/caleg/list?dapil_id=123e4567-e89b-12d3-a456-426614174113&partai_id=123e4567-e89b-12d3-a456-426614174222&size=10&sort=nomorUrut,desc
```

**Contoh Respons:**
```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174002",
      "dapil": {
        "id": "123e4567-e89b-12d3-a456-426614174113",
        "namaDapil": "DKI JAKARTA I",
        "provinsi": "DKI Jakarta",
        "wilayahDapilList": [
          "Jakarta Timur"
        ],
        "jumlahKursi": 6
      },
      "partai": {
        "id": "123e4567-e89b-12d3-a456-426614174222",
        "namaPartai": "Partai A",
        "nomorUrut": 1
      },
      "nomorUrut": 1,
      "nama": "Nama AA",
      "jenisKelamin": "LAKILAKI"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "first": true,
  "size": 10,
  "number": 0,
  "empty": false
}
```

Dengan URL di atas, bisa memfilter data berdasarkan Dapil dan Partai serta melakukan pagination dan pengurutan data.

## 5. Testing

Untuk menjalankan unit test, gunakan perintah berikut:
```bash
mvn test
```



