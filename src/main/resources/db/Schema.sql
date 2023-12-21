--CREATE TABLE
CREATE TABLE IF NOT EXISTS dapil(
    id VARCHAR2(300) AS UUID,
    nama_dapil VARCHAR2(300),
    provinsi VARCHAR2(300),
    wilayah_dapi_list VARCHAR2(300),
    jumlah_kursi Integer,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS partai(
    id VARCHAR2(300) as UUID,
    nama_partai VARCHAR2(300),
    nomor_urut Integer,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS caleg(
    id  VARCHAR2(300) AS UUID,
    dapil_ID UUID REFERENCES dapil(id),
    partai_ID UUID REFERENCES partai(id),
    nomor_urut Integer,
    nama VARCHAR2(300),
    jenis_kelamin VARCHAR2(300),
    PRIMARY KEY(id)
);

-- INSERT VALUE DATA

--DAPIL
INSERT INTO dapil(id, nama_dapil, provinsi, jumlah_kursi)
VALUE
('DAP2023122101', 'Dapil A','Bandung', 20),
('DAP2023122102', 'Dapil B','Jakarta', 20),
('DAP2023122103', 'Dapil C','Papua', 20);

--PARTAI
INSERT INTO partai(id, nama_partai, nomor_urut)
VALUE
('PAR2023122101', 'Partai A',1),
('PAR2023122102', 'Partai B',2),
('PAR2023122103', 'Partai C',3);

--CALEG
INSERT INTO caleg(id, dapil_ID, partai_ID, nomor_urut, nama, jenis_kelamin)
VALUE
('CAL2023122101','DAP2023122101','PAR2023122101',01,'CALEG AA','LAKI-LAKI'),
('CAL2023122102','DAP2023122102','PAR2023122102',02,'CALEG BB','PEREMPUAN'),
('CAL2023122103','DAP2023122103','PAR2023122103',03,'CALEG CC','LAKI-LAKI');