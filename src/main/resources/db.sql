CREATE TABLE dapil (
    id UUID PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    jumlah_kursi INT NOT NULL
);

CREATE TABLE partai (
    id UUID PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL
);

CREATE TABLE caleg (
    id UUID PRIMARY KEY,
    dapil_id UUID REFERENCES dapil(id),
    partai_id UUID REFERENCES partai(id),
    nomor_urut INT NOT NULL,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(10) NOT NULL
);