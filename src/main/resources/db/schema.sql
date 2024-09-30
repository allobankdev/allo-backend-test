-- Tabel untuk Partai
CREATE TABLE partai (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT
);

-- Tabel untuk Dapil
CREATE TABLE dapil (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    jumlah_kursi INT NOT NULL
);

-- Tabel untuk menyimpan wilayah dapil dalam bentuk JSON (MySQL 5.7+)
CREATE TABLE dapil_wilayah_dapil_list (
    dapil_id BIGINT,
    wilayah VARCHAR(255),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id) ON DELETE CASCADE,
    PRIMARY KEY (dapil_id, wilayah)
);

-- Tabel untuk Caleg
CREATE TABLE caleg (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dapil_id BIGINT NOT NULL,
    partai_id BIGINT NOT NULL,
    nomor_urut INT,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin ENUM('Laki-laki', 'Perempuan') NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id) ON DELETE CASCADE,
    FOREIGN KEY (partai_id) REFERENCES partai(id) ON DELETE CASCADE
);
