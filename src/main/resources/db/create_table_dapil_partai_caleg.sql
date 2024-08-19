USE pemilu;

CREATE TABLE dapil
(
    id VARCHAR(40) PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    wilayah_dapil_list TEXT,
    jumlah_kursi INT NOT NULL
);


CREATE TABLE partai
(
    id VARCHAR(40) PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL
);


CREATE TABLE caleg
(
    id VARCHAR(40) PRIMARY KEY,
    dapil_id VARCHAR(40),
    partai_id VARCHAR(40),
    nomor_urut INT NOT NULL,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(20) NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);

