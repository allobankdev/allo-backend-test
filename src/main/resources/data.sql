-- Masukkan data ke tabel partai
INSERT INTO partai (id, nama_partai, nomor_urut) VALUES
('b1e4c5d7-22a5-4b89-abe7-2b3a3e0b6c2b', 'PartaiA', 1),
('b2e5d6f8-33b6-5c90-bcf8-3c4b3f1d7d3c', 'PartaiB', 2);

-- Masukkan data ke tabel dapil
INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi) VALUES
('a1e5c5b7-88c5-4d99-bc88-9b77a8e275b7', 'Dapil1', 'ProvinsiA', 5),
('a5d4c2b6-1e5b-40d6-bad2-8c9c810ad2d5', 'Dapil2', 'ProvinsiB', 3);

-- Masukkan data ke tabel caleg
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) VALUES
('c1f6d7e8-44b7-6d92-cd89-4d5e6f7b8c9d', 'a1e5c5b7-88c5-4d99-bc88-9b77a8e275b7', 'b1e4c5d7-22a5-4b89-abe7-2b3a3e0b6c2b', 1, 'CalegA', 'LAKILAKI'),
('c2f7d8e9-55b8-7e93-de90-5e6f7g8c9d0e', 'a5d4c2b6-1e5b-40d6-bad2-8c9c810ad2d5', 'b2e5d6f8-33b6-5c90-bcf8-3c4b3f1d7d3c', 2, 'CalegB', 'PEREMPUAN'),
('c3f8e9f0-66c9-8f94-ef01-6f7g8h9d0e1f', 'a1e5c5b7-88c5-4d99-bc88-9b77a8e275b7', 'b2e5d6f8-33b6-5c90-bcf8-3c4b3f1d7d3c', 3, 'CalegC', 'LAKILAKI');
