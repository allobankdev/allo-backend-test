# ALLOBANK TEST

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


# NOTES
## JAVA
- Gunakan Java 17

## QUERY CREATE TABLE
- Query ini sudah automatis dijalankan sama sistem ketika aplikasi dijalankan
```
create table caleg (
    id binary(16) not null,
    jenis_kelamin enum ('LAKILAKI','PEREMPUAN'),
    nama varchar(255),
    nomor_urut integer,
    id_dapil binary(16) not null,
    id_partai binary(16) not null,
    primary key (id)
) engine=InnoDB

create table dapil (
    id binary(16) not null,
    jumlah_kursi integer not null,
    nama_dapil varchar(255),
    provinsi varchar(255),
    primary key (id)
) engine=InnoDB

create table dapil_wilayah_dapil_list (
    dapil_id binary(16) not null,
    wilayah_dapil_list varchar(255)
) engine=InnoDB

create table partai (
    id binary(16) not null,
    nama_partai varchar(255),
    nomor_urut integer,
    primary key (id)
) engine=InnoDB

alter table caleg 
    add constraint FKaujnksfcp45xgs6il9pwknp9h 
    foreign key (id_dapil) 
    references dapil (id)
    
alter table caleg 
    add constraint FKac8lve4bt6ydvsyg8b67unsm8 
    foreign key (id_partai) 
    references partai (id)
    
alter table dapil_wilayah_dapil_list 
    add constraint FK6ehl8rmwahgy7ei2acxhbnfkj 
    foreign key (dapil_id) 
    references dapil (id)
```

## QUERY INSERT DATA
```
INSERT INTO dapil (id, jumlah_kursi, nama_dapil, provinsi) VALUES
    (UUID_TO_BIN(UUID()), 5, 'Dapil 1', 'Provinsi A'),
    (UUID_TO_BIN(UUID()), 4, 'Dapil 2', 'Provinsi A'),
    (UUID_TO_BIN(UUID()), 6, 'Dapil 3', 'Provinsi B'),
    (UUID_TO_BIN(UUID()), 3, 'Dapil 4', 'Provinsi B');

INSERT INTO partai (id, nama_partai, nomor_urut) VALUES
    (UUID_TO_BIN(UUID()), 'Partai A', 1),
    (UUID_TO_BIN(UUID()), 'Partai B', 2),
    (UUID_TO_BIN(UUID()), 'Partai C', 3),
    (UUID_TO_BIN(UUID()), 'Partai D', 4);

INSERT INTO caleg (id, jenis_kelamin, nama, nomor_urut, id_dapil, id_partai) VALUES
    (UUID_TO_BIN(UUID()), 'LAKILAKI', 'Caleg 1', 1, (SELECT id FROM dapil LIMIT 1), (SELECT id FROM partai LIMIT 1)),
    (UUID_TO_BIN(UUID()), 'PEREMPUAN', 'Caleg 2', 2, (SELECT id FROM dapil LIMIT 1 OFFSET 1), (SELECT id FROM partai LIMIT 1 OFFSET 1)),
    (UUID_TO_BIN(UUID()), 'LAKILAKI', 'Caleg 3', 3, (SELECT id FROM dapil LIMIT 1 OFFSET 2), (SELECT id FROM partai LIMIT 1 OFFSET 2)),
    (UUID_TO_BIN(UUID()), 'PEREMPUAN', 'Caleg 4', 4, (SELECT id FROM dapil LIMIT 1 OFFSET 3), (SELECT id FROM partai LIMIT 1 OFFSET 3)),
    (UUID_TO_BIN(UUID()), 'LAKILAKI', 'Caleg 5', 5, (SELECT id FROM dapil LIMIT 1), (SELECT id FROM partai LIMIT 1)),
    (UUID_TO_BIN(UUID()), 'PEREMPUAN', 'Caleg 6', 6, (SELECT id FROM dapil LIMIT 1 OFFSET 1), (SELECT id FROM partai LIMIT 1 OFFSET 1)),
    (UUID_TO_BIN(UUID()), 'LAKILAKI', 'Caleg 7', 7, (SELECT id FROM dapil LIMIT 1 OFFSET 2), (SELECT id FROM partai LIMIT 1 OFFSET 2)),
    (UUID_TO_BIN(UUID()), 'PEREMPUAN', 'Caleg 8', 8, (SELECT id FROM dapil LIMIT 1 OFFSET 3), (SELECT id FROM partai LIMIT 1 OFFSET 3)),
    (UUID_TO_BIN(UUID()), 'LAKILAKI', 'Caleg 9', 9, (SELECT id FROM dapil LIMIT 1), (SELECT id FROM partai LIMIT 1)),
    (UUID_TO_BIN(UUID()), 'PEREMPUAN', 'Caleg 10', 10, (SELECT id FROM dapil LIMIT 1 OFFSET 1), (SELECT id FROM partai LIMIT 1 OFFSET 1));

INSERT INTO dapil_wilayah_dapil_list (dapil_id, wilayah_dapil_list) VALUES
    ((SELECT id FROM dapil LIMIT 1), 'Wilayah A1'),
    ((SELECT id FROM dapil LIMIT 1), 'Wilayah A2'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 1), 'Wilayah B1'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 1), 'Wilayah B2'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 2), 'Wilayah C1'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 2), 'Wilayah C2'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 3), 'Wilayah D1'),
    ((SELECT id FROM dapil LIMIT 1 OFFSET 3), 'Wilayah D2');
```

## URL API
- [ ] [GET All Data: ](localhost:1901/api/alloBank/caleg/list) localhost:1901/api/alloBank/caleg/list
- [ ] [GET Filtered Data 1: ](localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=nomorUrut) localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=nomorUrut
- [ ] [GET Filtered Data 2: ](localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=nama) localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=nama
- [ ] [Invalid Filtered Data: ](localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=x) localhost:1901/api/alloBank/caleg/list?dapil=nama_dapil&partai=nama_partai&sortBy=x
