-- data.sql
INSERT INTO partai (id, nama_partai, nomor_urut) VALUES ('123e4567-e89b-12d3-a456-426614174000', 'Partai A', 1);
INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi) VALUES ('123e4567-e89b-12d3-a456-426614174001', 'Dapil 1', 'Provinsi A', 5);
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) 
VALUES ('123e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000', 1, 'Caleg A', 'LAKILAKI');
