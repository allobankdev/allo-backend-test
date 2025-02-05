USE pemilu_db;

-- Insert data ke tabel Dapil
INSERT INTO dapil (id, nama_dapil, provinsi, wilayah_dapil_list, jumlah_kursi) VALUES
(UUID(), 'Dapil Jakarta 1', 'DKI Jakarta', 'Jakarta Pusat, Jakarta Selatan', 10),
(UUID(), 'Dapil Jawa Barat 2', 'Jawa Barat', 'Bekasi, Depok', 12),
(UUID(), 'Dapil Jawa Timur 3', 'Jawa Timur', 'Surabaya, Sidoarjo', 8);

-- Insert data ke tabel Partai
INSERT INTO partai (id, nama_partai, nomor_urut) VALUES
(UUID(), 'Partai Merdeka', 1),
(UUID(), 'Partai Sejahtera', 2),
(UUID(), 'Partai Pembangunan', 3);

-- Insert data ke tabel Caleg
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) VALUES
(UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil Jakarta 1'), (SELECT id FROM partai WHERE nama_partai = 'Partai Merdeka'), 1, 'Jojon', 'LAKILAKI'),
(UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil Jawa Barat 2'), (SELECT id FROM partai WHERE nama_partai = 'Partai Sejahtera'), 2, 'Jajan', 'PEREMPUAN'),
(UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil Jawa Timur 3'), (SELECT id FROM partai WHERE nama_partai = 'Partai Pembangunan'), 3, 'Jejen', 'LAKILAKI');
