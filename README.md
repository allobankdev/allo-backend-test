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



## DOKUMENTASI - HADIN DAVIDI SIANTURI 

STEP : 

-SETUP DATABASE POSTGRESQL on file resources/application.properties

-RUN SCHEMA resources/db/schema.sql

-RUN TEST

-RUN APP

### GET ALL CALEG
END POINT : GET /api/caleg

### GET CALEG BY DAPIL
END POINT : GET /api/caleg?dapil=Dapil 1

### GET CALEG BY PARTAI
END POINT : GET /api/caleg?partai=Partai A

### GET CALEG BY DAPIL & PARTAI
END POINT : GET /api/caleg?partai=Partai A&dapil=Dapil 1


## RESPONSE FORMAT : 
SUCCESS -> {data : [...], errors : null}
FAILED (Invalid dapil, Invalid partai, Caleg Not Found) -> ResponseStatusException


