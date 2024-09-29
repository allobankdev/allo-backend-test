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

INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi)
VALUES
    ('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Dapil 1', 'Jawa Barat', 10),
    ('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Dapil 2', 'Jawa Timur', 8);

INSERT INTO partai (id, nama_partai, nomor_urut) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'Partai A', 1),
('550e8400-e29b-41d4-a716-446655440001', 'Partai B', 2),
('550e8400-e29b-41d4-a716-446655440002', 'Partai C', 3);

INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) VALUES
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 1, 'Caleg A1', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 2, 'Caleg A2', 'PEREMPUAN'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 3, 'Caleg B1', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 4, 'Caleg B2', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 5, 'Caleg C1', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440000', 6, 'Caleg C2', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440001', 7, 'Caleg A3', 'LAKILAKI'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440001', 8, 'Caleg B3', 'PEREMPUAN'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440001', 9, 'Caleg C3', 'PEREMPUAN'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440001', 10, 'Caleg C4', 'PEREMPUAN'),
(UUID(), 'd1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', '550e8400-e29b-41d4-a716-446655440001', 11, 'Caleg A4', 'PEREMPUAN'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 12, 'Caleg A5', 'PEREMPUAN'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 13, 'Caleg B4', 'PEREMPUAN'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 14, 'Caleg B5', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 15, 'Caleg C5', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 16, 'Caleg C6', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 17, 'Caleg C7', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 18, 'Caleg C8', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 19, 'Caleg A6', 'LAKILAKI'),
(UUID(), 'f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', '550e8400-e29b-41d4-a716-446655440002', 20, 'Caleg A7', 'LAKILAKI');

INSERT INTO dapil_wilayah_dapil_list (dapil_id, wilayah_dapil_list) VALUES
('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Wilayah Dapil A'),
('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Wilayah Dapil B'),
('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Wilayah Dapil C'),
('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Wilayah Dapil D'),
('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Wilayah Dapil E'),
('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Wilayah Dapil F'),
('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Wilayah Dapil G'),
('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Wilayah Dapil H'),
('d1e5f86e-58de-4e5b-aee6-0c8c29ecb9f7', 'Wilayah Dapil I'),
('f67d5a6d-3d21-4b6f-9e2d-02d5e7a2ef15', 'Wilayah Dapil J');