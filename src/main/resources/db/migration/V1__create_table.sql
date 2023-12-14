CREATE DATABASE IF NOT EXISTS allo_bank_db_test;
USE allo_bank_db_test;

CREATE TABLE IF NOT EXISTS dapil (
    id VARCHAR(36) PRIMARY KEY,
    nama_dapil VARCHAR(101),
    provinsi VARCHAR(50),
    jumlah_kursi INTEGER
);

CREATE TABLE IF NOT EXISTS partai (
    id VARCHAR(36) PRIMARY KEY,
    nama_partai VARCHAR(101),
    nomor_urut INTEGER
);

CREATE TABLE IF NOT EXISTS caleg (
    id VARCHAR(36) PRIMARY KEY,
    dapil_id VARCHAR(36),
    partai_id VARCHAR(36),
    nomor_urut INTEGER,
    nama VARCHAR(101),
    jenis_kelamin VARCHAR(20),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);
