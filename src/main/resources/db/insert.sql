INSERT INTO public.dapil
(id, created_at, updated_at, jumlah_kursi, nama_dapil, provinsi, wilayah_dapil_list)
VALUES('93b77144-5e14-410e-9f9f-ee9d904fc54e'::uuid, '2024-05-16 19:17:42.997', '2024-05-16 19:17:42.997', 8, 'DKI JAKARTA 3', 'DKI JAKARTA', '{"Kepulauan Seribu","Jakarta Barat","Jakarta Utara"}');
INSERT INTO public.dapil
(id, created_at, updated_at, jumlah_kursi, nama_dapil, provinsi, wilayah_dapil_list)
VALUES('54df86d9-cafc-4e1e-bcf8-2dabfe6e1b92'::uuid, '2024-05-16 19:18:11.289', '2024-05-16 19:18:11.289', 7, 'DKI JAKARTA 2', 'DKI JAKARTA', '{"Jakarta Pusat","Jakarta Selatan"}');
INSERT INTO public.dapil
(id, created_at, updated_at, jumlah_kursi, nama_dapil, provinsi, wilayah_dapil_list)
VALUES('071bf960-edd3-4c84-98b2-65be2e409e33'::uuid, '2024-05-16 19:18:27.449', '2024-05-16 19:18:27.449', 6, 'DKI JAKARTA 1', 'DKI JAKARTA', '{"Jakarta Timur"}');


INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('37b4f3bb-52b2-461f-9d1d-8dd1baa66cea'::uuid, '2024-05-16 19:15:41.607', '2024-05-16 19:15:41.607', 'PDIP', 1);
INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('9ff2424e-6cba-419a-80ab-b543f8be3250'::uuid, '2024-05-16 19:15:57.551', '2024-05-16 19:15:57.551', 'Gerindra', 2);
INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('5e471b08-8e06-4d81-a1eb-9f900163b740'::uuid, '2024-05-16 19:16:41.380', '2024-05-16 19:16:41.380', 'Golkar', 3);
INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('29848586-bdc6-46a7-8dbb-83057e33e2de'::uuid, '2024-05-16 19:16:55.399', '2024-05-16 19:16:55.399', 'Demokrat', 4);
INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('bf919b9e-c87d-47c0-b555-ce0790efeb8c'::uuid, '2024-05-16 19:17:06.924', '2024-05-16 19:17:06.924', 'PSI', 5);
INSERT INTO public.partai
(id, created_at, updated_at, nama_partai, nomor_urut)
VALUES('8aeb1023-16cd-4beb-918e-7038a089dcdd'::uuid, '2024-05-16 19:17:22.445', '2024-05-16 19:17:22.445', 'Nasdem', 6);


INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('2adb1965-74ea-41f8-aa7e-f961b1f9e57a'::uuid, '2024-05-16 19:20:07.462', '2024-05-16 19:20:07.462', 'LAKILAKI', 'Dr. HABIBUROKHMAN, S.H., M.H.', 1, '071bf960-edd3-4c84-98b2-65be2e409e33'::uuid, '9ff2424e-6cba-419a-80ab-b543f8be3250'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('ca1bd7f9-94c7-4c1b-9c2e-d5bbb63884c5'::uuid, '2024-05-16 19:21:45.074', '2024-05-16 19:21:45.074', 'LAKILAKI', 'PUTRA NABABAN', 1, '071bf960-edd3-4c84-98b2-65be2e409e33'::uuid, '37b4f3bb-52b2-461f-9d1d-8dd1baa66cea'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('d1a30eb9-b1e5-47ad-9e84-10e2d0bd6d99'::uuid, '2024-05-16 19:22:26.482', '2024-05-16 19:22:26.482', 'LAKILAKI', 'ARIO BIMO NANDITO A, S.H.', 1, '071bf960-edd3-4c84-98b2-65be2e409e33'::uuid, '5e471b08-8e06-4d81-a1eb-9f900163b740'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('7340083e-5a64-42d1-9496-c8446c651d3d'::uuid, '2024-05-16 19:22:58.774', '2024-05-16 19:22:58.774', 'LAKILAKI', 'SONY KUSUMO', 4, '071bf960-edd3-4c84-98b2-65be2e409e33'::uuid, '37b4f3bb-52b2-461f-9d1d-8dd1baa66cea'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('607c49b6-5815-471b-8e1b-9c39d9111247'::uuid, '2024-05-16 19:23:55.980', '2024-05-16 19:23:55.980', 'PEREMPUAN', 'RAHAYU SARASWATI D. DJOJOHADIKUSUMO', 1, '93b77144-5e14-410e-9f9f-ee9d904fc54e'::uuid, '9ff2424e-6cba-419a-80ab-b543f8be3250'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('d32697a4-6769-4166-96ca-290473878ed5'::uuid, '2024-05-16 19:24:16.418', '2024-05-16 19:24:16.418', 'LAKILAKI', 'Drs. HOSEA SANJAYA', 2, '93b77144-5e14-410e-9f9f-ee9d904fc54e'::uuid, '9ff2424e-6cba-419a-80ab-b543f8be3250'::uuid);
INSERT INTO public.caleg
(id, created_at, updated_at, jenis_kelamin, nama, nomor_urut, dapil_id, partai_id)
VALUES('c5a08219-a602-4d68-a0ec-1aa060faee62'::uuid, '2024-05-16 19:24:47.925', '2024-05-16 19:24:47.925', 'LAKILAKI', 'AsscProf. Dr. DARMADI DURIANTO', 1, '93b77144-5e14-410e-9f9f-ee9d904fc54e'::uuid, '37b4f3bb-52b2-461f-9d1d-8dd1baa66cea'::uuid);