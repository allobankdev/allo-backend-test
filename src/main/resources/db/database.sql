CREATE DATABASE allo_bank_test
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1



CREATE TABLE dapil(
    id UUID PRIMARY KEY,
    nama_dapil VARCHAR(100) NOT NULL,
    provinsi VARCHAR(100) NOT NULL,
    jumlah_kursi INT NOT NULL
);



CREATE TABLE wilayah_dapil(
    id UUID PRIMARY KEY,
    wilayah_dapil VARCHAR(100) NOT NULL,
    dapil_id UUID NOT NULL,
    CONSTRAINT fk_wilayah_dapil_dapil FOREIGN KEY(dapil_id)
        REFERENCES dapil(id) ON DELETE CASCADE
);



CREATE TABLE partai(
    id UUID PRIMARY KEY,
    nama_partai VARCHAR(100) NOT NULL,
    nomor_urut INT NOT NULL
);




CREATE TABLE caleg(
    id UUID PRIMARY KEY,
    nomor_urut INT NOT NULL,
    nama VARCHAR(100) NOT NULL,
    jenis_kelamin VARCHAR(20) NOT NULL,
    dapil_id UUID NOT NULL,
    partai_id UUID NOT NULL,
    CONSTRAINT fk_caleg_dapil FOREIGN KEY(dapil_id)
        REFERENCES dapil(id) ON DELETE CASCADE,
    CONSTRAINT fk_caleg_partai FOREIGN KEY(partai_id)
        REFERENCES partai(id) ON DELETE CASCADE
)