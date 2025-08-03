DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'jenis_kelamin_enum') THEN
        CREATE TYPE jenis_kelamin_enum AS ENUM ('LAKILAKI', 'PEREMPUAN');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS partai (
    id UUID PRIMARY KEY,
    nama_partai VARCHAR(255),
    nomor_urut INTEGER
);

CREATE TABLE IF NOT EXISTS dapil (
    id UUID PRIMARY KEY,
    nama_dapil VARCHAR(255),
    provinsi VARCHAR(255),
    wilayah_dapil_list TEXT[],
    jumlah_kursi INTEGER
);

CREATE TABLE IF NOT EXISTS caleg (
    id UUID PRIMARY KEY,
    dapil_id UUID REFERENCES dapil(id),
    partai_id UUID REFERENCES partai(id),
    nomor_urut INTEGER,
    nama VARCHAR(255),
    jenis_kelamin jenis_kelamin_enum
);
