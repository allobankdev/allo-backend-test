-- Memasukkan data dummy ke tabel dapil
INSERT INTO dapil (nama_dapil, provinsi, jumlah_kursi) VALUES
('Dapil Jakarta Pusat', 'DKI Jakarta', 6),
('Dapil Jawa Barat 1', 'Jawa Barat', 8),
('Dapil Sumatera Utara 2', 'Sumatera Utara', 5),
('Dapil Bali', 'Bali', 4);

-- Memasukkan data dummy ke tabel partai
INSERT INTO partai (nama_partai, nomor_urut) VALUES
('Partai Demokrasi Indonesia Perjuangan', 1),
('Partai Golongan Karya', 2),
('Partai Gerakan Indonesia Raya', 3),
('Partai Amanat Nasional', 4);

-- Memasukkan data dummy ke tabel caleg
INSERT INTO caleg (jenis_kelamin, nama, nomor_urut, dapil_id, partai_id) VALUES
('LAKILAKI', 'Asep Hidayat', 1, 1, 1),
('LAKILAKI', 'Budi Santoso', 2, 2, 2),
('PEREMPUAN', 'Citra Dewi', 3, 3, 3),
('PEREMPUAN', 'Dian Pratama', 4, 4, 4);

-- Memasukkan data dummy ke tabel wilayah_dapil
INSERT INTO wilayah_dapil (dapil_id, wilayah) VALUES
(1, 'Jakarta Pusat 1'),
(2, 'Jakarta Pusat 2'),
(3, 'Bandung Utara'),
(4, 'Bandung Selatan')