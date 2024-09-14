CREATE TABLE IF NOT EXISTS partai (
    id CHAR(36) PRIMARY KEY,
    nama_partai VARCHAR(100) NOT NULL,
    nomor_urut INT NOT NULL
);

CREATE TABLE IF NOT EXISTS dapil (
    id CHAR(36) PRIMARY KEY,
    nama_dapil VARCHAR(100) NOT NULL,
    provinsi VARCHAR(100) NOT NULL,
    jumlah_kursi INT NOT NULL
);

CREATE TABLE IF NOT EXISTS caleg (
    id CHAR(36) PRIMARY KEY,
    dapil_id CHAR(36),
    partai_id CHAR(36),
    nomor_urut INT NOT NULL,
    nama VARCHAR(100) NOT NULL,
    jenis_kelamin VARCHAR(50) NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);
