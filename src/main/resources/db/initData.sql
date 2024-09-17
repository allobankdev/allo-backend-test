INSERT INTO partai (id, nama_partai, nomor_urut) VALUES ('bbc5cc6a-bec2-4d68-ad33-560f0a5edc92', 'Partai Buruh Sejahtera', 1);
INSERT INTO partai (id, nama_partai, nomor_urut) VALUES ('f4bd1292-9959-40f7-abbd-1ff56d20da50', 'Partai Banteng Kuning', 2);

INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi) VALUES ('0172caf6-c413-46cb-b9bd-daf9045d49b8', 'Bandung', 'Jawa Barat', 2);
INSERT INTO dapil (id, nama_dapil, provinsi, jumlah_kursi) VALUES ('a31c9255-a25e-461c-bac7-59c6f47ba3e7', 'Cimahi', 'Jawa Barat', 4);

INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) 
VALUES ('588c3f5f-b50f-4125-b900-fa45dad19589', '0172caf6-c413-46cb-b9bd-daf9045d49b8', 'bbc5cc6a-bec2-4d68-ad33-560f0a5edc92', 1, 'Asep Sobandi', 'LAKILAKI');
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin) 
VALUES ('209c25fb-0171-427d-bc55-6c5b0efcf3f3', 'a31c9255-a25e-461c-bac7-59c6f47ba3e7', 'f4bd1292-9959-40f7-abbd-1ff56d20da50', 1, 'Budi Somad', 'LAKILAKI');