CREATE DATABASE IF NOT EXISTS pemilu_db;
USE pemilu_db;

-- Tabel Dapil (Daerah Pemilihan)
CREATE TABLE dapil (
    id CHAR(36) PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    wilayah_dapil_list TEXT NOT NULL,
    jumlah_kursi INT NOT NULL
);

-- Tabel Partai (Partai Pemilu)
CREATE TABLE partai (
    id CHAR(36) PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL
);

-- Tabel Caleg (Calon Legislatif)
CREATE TABLE caleg (
    id CHAR(36) PRIMARY KEY,
    dapil_id CHAR(36) NOT NULL,
    partai_id CHAR(36) NOT NULL,
    nomor_urut INT NOT NULL,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin ENUM('LAKILAKI', 'PEREMPUAN') NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id) ON DELETE CASCADE,
    FOREIGN KEY (partai_id) REFERENCES partai(id) ON DELETE CASCADE
);
