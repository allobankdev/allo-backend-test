CREATE TABLE caleg (
    id UUID PRIMARY KEY,
    dapil_id UUID,
    partai_id UUID,
    nomor_urut INTEGER,
    nama VARCHAR(255),
    jenis_kelamin VARCHAR(20),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);

CREATE TABLE dapil (
    id UUID PRIMARY KEY,
    nama_dapil VARCHAR(255),
    provinsi VARCHAR(255),
    jumlah_kursi INTEGER
);

CREATE TABLE partai (
    id UUID PRIMARY KEY,
    nama_partai VARCHAR(255),
    nomor_urut INTEGER
);
