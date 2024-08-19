USE pemilu;
-- Insert Data Dapil
INSERT INTO dapil
    (id, nama_dapil, provinsi, jumlah_kursi, wilayah_dapil_list)
VALUES
    ('123e4567-e89b-12d3-a456-426614174113', 'DKI JAKARTA I', 'DKI Jakarta', 6, "Jakarta Timur"),
    ('123e4567-e89b-12d3-a456-426614174114', 'DKI JAKARTA II', 'DKI Jakarta', 7, "Jakarta Pusat, Jakarta Selatan"),
    ('123e4567-e89b-12d3-a456-426614174115', 'DKI JAKARTA III', 'DKI Jakarta', 8, "Kep. Seribu, Jakarta Utara, Jakarta Barat");

-- Insert Data Partai
INSERT INTO partai
    (id, nama_partai, nomor_urut)
VALUES
    ('123e4567-e89b-12d3-a456-426614174222', 'Partai A', 1),
    ('123e4567-e89b-12d3-a456-426614174223', 'Partai B', 2),
    ('123e4567-e89b-12d3-a456-426614174224', 'Partai C', 3);


-- Insert Data Caleg
INSERT INTO caleg
    (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin)
VALUES
    ('123e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174113', '123e4567-e89b-12d3-a456-426614174222', 1, 'Nama AA', 'LAKILAKI'),
    ('123e4567-e89b-12d3-a456-426614174003', '123e4567-e89b-12d3-a456-426614174114', '123e4567-e89b-12d3-a456-426614174223', 2, 'Nama BB', 'PEREMPUAN'),
    ('123e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174115', '123e4567-e89b-12d3-a456-426614174224', 3, 'Nama CC', 'LAKILAKI');
