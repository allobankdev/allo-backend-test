
DROP TABLE IF EXISTS caleg;
DROP TABLE IF EXISTS partai;
DROP TABLE IF EXISTS dapil;


CREATE TABLE IF NOT EXISTS partai (
                                      id UUID DEFAULT gen_random_uuid(),
                                      nama_partai VARCHAR(255) NOT NULL,
                                      nomor_urut INTEGER,
                                      PRIMARY KEY (id)
);

-- Tabel Dapil
CREATE TABLE IF NOT EXISTS dapil (
                                     id UUID DEFAULT gen_random_uuid(),
                                     nama_dapil VARCHAR(255) NOT NULL,
                                     provinsi VARCHAR(255),
                                     jumlah_kursi INTEGER,
                                     PRIMARY KEY (id)
);

-- Tabel Caleg
CREATE TABLE IF NOT EXISTS caleg (
                                     id UUID DEFAULT gen_random_uuid(),
                                     dapil_id UUID REFERENCES dapil(id),
                                     partai_id UUID REFERENCES partai(id),
                                     nomor_urut INTEGER,
                                     nama VARCHAR(255),
                                     jenis_kelamin VARCHAR(50),
                                     PRIMARY KEY (id)
);

-- Dummy Data untuk Partai
INSERT INTO partai (id, nama_partai, nomor_urut)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'Partai A', 1),
    ('00000000-0000-0000-0000-000000000002', 'Partai B', 2),
    ('00000000-0000-0000-0000-000000000003', 'Partai C', 3);

-- Dummy Data untuk Dapil
INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi)
VALUES
    ('00000000-0000-0000-0000-000000000004', 'Dapil 1', 'Provinsi X', 5),
    ('00000000-0000-0000-0000-000000000005', 'Dapil 2', 'Provinsi Y', 3),
    ('00000000-0000-0000-0000-000000000006', 'Dapil 3', 'Provinsi Z', 4);

-- Dummy Data untuk Caleg
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin)
VALUES
    ('00000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', '1', 'Caleg 1A', 'LAKILAKI'),
    ('00000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', '2', 'Caleg 1B', 'PEREMPUAN'),
    ('00000000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000002', '1', 'Caleg 2A', 'LAKILAKI'),
    ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000002', '3', 'Caleg 2B', 'PEREMPUAN'),
    ('00000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000003', '2', 'Caleg 3A', 'LAKILAKI'),
    ('00000000-0000-0000-0000-000000000012', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000003', '3', 'Caleg 3B', 'PEREMPUAN');
