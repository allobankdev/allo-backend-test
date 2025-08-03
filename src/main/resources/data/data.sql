
INSERT INTO partai (nama_partai, nomor_urut) VALUES
('Partai A', 1),
('Partai B', 2),
('Partai C', 3);

INSERT INTO dapil (nama_dapil, provinsi, wilayah_dapil_list, jumlah_kursi) VALUES
('Dapil 1', 'Provinsi Jawa Barat', '["Kab. Bogor", "Depok"]', 5),
('Dapil 2', 'Provinsi Jawa Tengah', '["Solo", "Jogyakarta"]', 4),
('Dapil 3', 'Provinsi Jawa Timur', '["Banyumas", "Malang"]', 6);

INSERT INTO caleg (nama, nomor_urut, dapil_id, partai_id, jenis_kelamin) VALUES('Komeng',1,1,1,'LAKI_LAKI'),
('Siti',2,1,2,'PEREMPUAN'),
('Budi',3,2,1,'LAKI_LAKI'),
('Dewi',4,2,3,'PEREMPUAN'),
('Joko',5,3,1,'LAKI_LAKI'),
('Rina',6,3,2,'PEREMPUAN'),
('Andi',7,1,3,'LAKI_LAKI'),
('Tina',8,2,2,'PEREMPUAN');
