-- Tabel untuk Partai
CREATE TABLE partai (
    id CHAR(36) PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INTEGER NOT NULL
);

-- Tabel untuk Dapil
CREATE TABLE dapil (
    id CHAR(36) PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    jumlah_kursi INTEGER NOT NULL
);

-- Tabel untuk Caleg
CREATE TABLE caleg (
    id CHAR(36) PRIMARY KEY,
    dapil_id CHAR(36) NOT NULL,
    partai_id CHAR(36) NOT NULL,
    nomor_urut INTEGER NOT NULL,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(50) NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);