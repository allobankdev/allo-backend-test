-- Menambahkan data ke tabel dapil
INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi)
VALUES (UUID(), 'Dapil 1 Jakarta', 'DKI Jakarta', 10),
       (UUID(), 'Dapil 2 Jawa Barat', 'Jawa Barat', 12);

-- Menambahkan data ke tabel partai
INSERT INTO partai (id, nama_partai, nomor_urut)
VALUES (UUID(), 'Partai A', 1),
       (UUID(), 'Partai B', 2),
       (UUID(), 'Partai C', 3);

-- Menambahkan data ke tabel caleg
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin)
VALUES (UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil 1 Jakarta'),
        (SELECT id FROM partai WHERE nama_partai = 'Partai A'), 1, 'Caleg A1', 'LAKILAKI'),
       (UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil 1 Jakarta'),
        (SELECT id FROM partai WHERE nama_partai = 'Partai A'), 2, 'Caleg A2', 'PEREMPUAN'),
       (UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil 1 Jakarta'),
        (SELECT id FROM partai WHERE nama_partai = 'Partai B'), 3, 'Caleg B1', 'LAKILAKI'),
       (UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil 2 Jawa Barat'),
        (SELECT id FROM partai WHERE nama_partai = 'Partai C'), 1, 'Caleg C1', 'PEREMPUAN'),
       (UUID(), (SELECT id FROM dapil WHERE nama_dapil = 'Dapil 2 Jawa Barat'),
        (SELECT id FROM partai WHERE nama_partai = 'Partai C'), 2, 'Caleg C2', 'LAKILAKI');
