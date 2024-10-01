CREATE TABLE partai (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nama_partai VARCHAR(50) NOT NULL,
    nomor_urut INT
);

CREATE TABLE dapil (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nama_dapil VARCHAR(50) NOT NULL,
    provinsi VARCHAR(50),
    jumlah_kursi INT
);

CREATE TABLE caleg (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nama VARCHAR(50) NOT NULL,
    nomor_urut INT NOT NULL,
    dapil_id UUID,
    partai_id UUID,
    jenis_kelamin VARCHAR(20),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);

CREATE TABLE wilayah_dapil (
    dapil_id UUID,
    wilayah VARCHAR(50),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id)
);
