CREATE TABLE partai (
    id UUID PRIMARY KEY,
    nama_partai VARCHAR(255) UNIQUE NOT NULL,
    nomor_urut INT
);

CREATE TABLE dapil (
    id UUID PRIMARY KEY,
    nama_dapil VARCHAR(255) UNIQUE NOT NULL,
    provinsi VARCHAR(255),
    jumlah_kursi INT
);

CREATE TABLE dapil_wilayah (
    dapil_id UUID,
    wilayah VARCHAR(255),
    FOREIGN KEY (dapil_id) REFERENCES dapil(id)
);

CREATE TABLE caleg (
    id UUID PRIMARY KEY,
    nama VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL,
    jenis_kelamin ENUM ('LAKILAKI', 'PEREMPUAN'),
    alamat TEXT,
    dapil_id UUID,
    partai_id UUID,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);
