CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TYPE JenisKelamin AS ENUM ('LAKILAKI', 'PEREMPUAN');

CREATE TABLE personal.partai (
	id varchar DEFAULT personal.uuid_generate_v4() NOT NULL,
	nama_partai varchar NULL,
	nomor_urut int4 NULL,
	CONSTRAINT partai_pk PRIMARY KEY (id)
);

CREATE TABLE personal.dapil (
	id varchar DEFAULT personal.uuid_generate_v4() NOT NULL,
	nama_dapil varchar NULL,
	provinsi varchar NULL,
	wilayah_dapil_list _varchar NULL,
	jumlahkursi int4 NULL,
	CONSTRAINT dapil_pk PRIMARY KEY (id)
);

CREATE TABLE personal.caleg (
	id varchar DEFAULT personal.uuid_generate_v4() NOT NULL,
	dapil varchar NOT NULL,
	partai varchar NOT NULL,
	nomor_urut int4 NULL,
	nama varchar NULL,
	jenis_kelamin personal."jeniskelamin" NULL,
	CONSTRAINT caleg_pk PRIMARY KEY (id)
);

ALTER TABLE personal.caleg ADD CONSTRAINT caleg_dapil_fk FOREIGN KEY (dapil) REFERENCES personal.dapil(id);
ALTER TABLE personal.caleg ADD CONSTRAINT caleg_partai_fk FOREIGN KEY (partai) REFERENCES personal.partai(id);

INSERT INTO personal.partai
(nama_partai, nomor_urut)
VALUES('Golkar', 1),
('Demokrat', 2),
('PDIP', 3),
('PAN', 4);

INSERT INTO personal.dapil
(nama_dapil, provinsi, wilayah_dapil_list, jumlahkursi)
VALUES('DKI JAKARTA 1', 'DKI JAKARTA', '{"KOTA ADM. JAKARTA PUSAT"}', 12),
('DKI JAKARTA 2', 'DKI JAKARTA', '{"ADM. KEP. SERIBU","KOTA ADM. JAKARTA UTARA"}', 11),
('DKI JAKARTA 3', 'DKI JAKARTA', '{"ADM. KEP. SERIBU","KOTA ADM. JAKARTA UTARA"}', 10);

INSERT INTO personal.caleg
(dapil, partai, nomor_urut, nama, jenis_kelamin)
VALUES('e9e1fb24-d511-467a-99c3-a8b4bbd75f6b', '72d85684-cbb8-4e88-a764-4e1664a0199f', 1, 'Agus', 'LAKILAKI'),
('e9e1fb24-d511-467a-99c3-a8b4bbd75f6b', 'b669ea64-313a-482f-93ab-beda039a3dd6', 2, 'Amel', 'PEREMPUAN'),
('e9e1fb24-d511-467a-99c3-a8b4bbd75f6b', '72d85684-cbb8-4e88-a764-4e1664a0199f', 3, 'Dika', 'LAKILAKI');