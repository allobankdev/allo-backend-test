-- Sequence untuk tabel dapil
CREATE SEQUENCE dapil_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Sequence untuk tabel partai
CREATE SEQUENCE partai_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Sequence untuk tabel caleg
CREATE SEQUENCE caleg_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabel dapil (Daerah Pemilihan)
CREATE TABLE dapil (
    dapil_id BIGINT DEFAULT nextval('dapil_id_seq') PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    jumlah_kursi INT NOT NULL
);

-- Tabel partai (Partai Politik)
CREATE TABLE partai (
    partai_id BIGINT DEFAULT nextval('partai_id_seq') PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL
);

-- Tabel caleg (Calon Legislatif)
CREATE TABLE caleg (
    caleg_id BIGINT DEFAULT nextval('caleg_id_seq') PRIMARY KEY,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(255) NOT NULL,
    nomor_urut INT NOT NULL,
    dapil_id BIGINT,
    partai_id BIGINT,
    FOREIGN KEY (dapil_id) REFERENCES dapil(dapil_id),
    FOREIGN KEY (partai_id) REFERENCES partai(partai_id)
);

nextval('caleg_id_seq'::regclass)
nextval('partai_id_seq'::regclass)
nextval('dapil_id_seq'::regclass)

-- Tabel wilayah_dapil (Wilayah Daerah Pemilihan)
CREATE TABLE wilayah_dapil (
    nama_wilayah VARCHAR(255) NOT NULL,
    dapil_id BIGINT,
    FOREIGN KEY (dapil_id) REFERENCES dapil(dapil_id)
);