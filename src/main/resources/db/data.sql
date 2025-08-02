-- Set schema
SET search_path TO pemilu;

-- Insert Partai (skip jika id sudah ada)
INSERT INTO partai (id, nama_partai, nomor_urut) VALUES
                                                     ('550e8400-e29b-41d4-a716-446655440001', 'Partai Demokrasi Indonesia Perjuangan', 1),
                                                     ('550e8400-e29b-41d4-a716-446655440002', 'Partai Gerakan Indonesia Raya', 2),
                                                     ('550e8400-e29b-41d4-a716-446655440003', 'Partai Golongan Karya', 3),
                                                     ('550e8400-e29b-41d4-a716-446655440004', 'Partai NasDem', 4),
                                                     ('550e8400-e29b-41d4-a716-446655440005', 'Partai Demokrat', 5)
    ON CONFLICT (id) DO NOTHING;

-- Insert Dapil (skip jika id sudah ada)
INSERT INTO dapil (id, nama_dapil, provinsi, wilayah_dapil_list, jumlah_kursi) VALUES
                                                                                   ('650e8400-e29b-41d4-a716-446655440001', 'DKI Jakarta 1', 'DKI Jakarta', '["Jakarta Pusat", "Jakarta Utara"]', 8),
                                                                                   ('650e8400-e29b-41d4-a716-446655440002', 'DKI Jakarta 2', 'DKI Jakarta', '["Jakarta Selatan", "Luar Negeri"]', 7),
                                                                                   ('650e8400-e29b-41d4-a716-446655440003', 'Jawa Barat 1', 'Jawa Barat', '["Bandung", "Cimahi"]', 10),
                                                                                   ('650e8400-e29b-41d4-a716-446655440004', 'Jawa Barat 2', 'Jawa Barat', '["Bogor", "Depok"]', 9)
    ON CONFLICT (id) DO NOTHING;

-- Insert Caleg (skip jika id sudah ada)
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) VALUES
                                                                                 -- DKI Jakarta 1 - PDIP
                                                                                 ('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 1, 'Budi Santoso', 'LAKILAKI'),
                                                                                 ('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 2, 'Siti Aminah', 'PEREMPUAN'),
                                                                                 -- DKI Jakarta 1 - Gerindra
                                                                                 ('750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 1, 'Ahmad Hidayat', 'LAKILAKI'),
                                                                                 ('750e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 2, 'Dewi Lestari', 'PEREMPUAN'),
                                                                                 -- DKI Jakarta 2 - Golkar
                                                                                 ('750e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 1, 'Rudi Hartono', 'LAKILAKI'),
                                                                                 ('750e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 2, 'Maya Sari', 'PEREMPUAN'),
                                                                                 -- Jawa Barat 1 - NasDem
                                                                                 ('750e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440004', 1, 'Eko Prasetyo', 'LAKILAKI'),
                                                                                 ('750e8400-e29b-41d4-a716-446655440008', '650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440004', 2, 'Rina Wijaya', 'PEREMPUAN')
    ON CONFLICT (id) DO NOTHING;
