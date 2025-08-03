DROP TABLE IF EXISTS caleg;
DROP TABLE IF EXISTS dapil;
DROP TABLE IF EXISTS partai;

CREATE TABLE partai (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nama_partai NVARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL
);

CREATE TABLE dapil (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nama_dapil NVARCHAR(255) NOT NULL,
    provinsi NVARCHAR(255) NOT NULL,
    wilayah_dapil_list NVARCHAR(MAX) NOT NULL,
    jumlah_kursi INT NOT NULL
);

CREATE TABLE caleg (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nama NVARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL,
    dapil_id BIGINT NOT NULL,
    partai_id BIGINT NOT NULL,
    jenis_kelamin NVARCHAR(10) CHECK (jenis_kelamin IN ('LAKI_LAKI', 'PEREMPUAN')),

    CONSTRAINT FK_caleg_dapil FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    CONSTRAINT FK_caleg_partai FOREIGN KEY (partai_id) REFERENCES partai(id)
);