-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS pemilu;

-- Use schema
SET search_path TO pemilu;

-- Create tables
CREATE TABLE IF NOT EXISTS partai (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INTEGER NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS dapil (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    wilayah_dapil_list TEXT NOT NULL, -- JSON array stored as text
    jumlah_kursi INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS caleg (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dapil_id UUID NOT NULL,
    partai_id UUID NOT NULL,
    nomor_urut INTEGER NOT NULL,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_caleg_dapil FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    CONSTRAINT fk_caleg_partai FOREIGN KEY (partai_id) REFERENCES partai(id),
    CONSTRAINT chk_jenis_kelamin CHECK (jenis_kelamin IN ('LAKILAKI', 'PEREMPUAN'))
    );

-- Create indexes (won't error if already exists)
CREATE INDEX IF NOT EXISTS idx_caleg_dapil
    ON pemilu.caleg (dapil_id);

CREATE INDEX IF NOT EXISTS idx_caleg_partai
    ON pemilu.caleg (partai_id);

CREATE INDEX IF NOT EXISTS idx_caleg_nomor_urut
    ON pemilu.caleg (nomor_urut);
