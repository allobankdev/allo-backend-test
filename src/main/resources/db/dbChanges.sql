CREATE SCHEMA `pemilu` ;

CREATE TABLE partai
(
	id VARCHAR(36) NOT NULL,
	nama_partai VARCHAR(100) NOT NULL,
	nomor_urut INT NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (nama_partai)
);

CREATE TABLE dapil
(
	id VARCHAR(36) NOT NULL,
	nama_dapil VARCHAR(100) NOT NULL,
	provinsi VARCHAR(100) NOT NULL,
	wilayah_dapil_list VARCHAR(300) NOT NULL,
	jumlah_kursi INT NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (nama_dapil)
);

CREATE TABLE caleg
(
	id VARCHAR(36) NOT NULL,
	nama_dapil VARCHAR(100) NOT NULL,
	nama_partai VARCHAR(100) NOT NULL,
	nomor_urut INT NOT NULL,
	nama VARCHAR(100) NOT NULL,
	jenis_kelamin VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY fk_caleg_partai (nama_partai) REFERENCES partai(nama_partai),
	FOREIGN KEY fk_caleg_daerah (nama_dapil) REFERENCES dapil(nama_dapil)
);

INSERT INTO partai(id,nama_partai,nomor_urut) 
VALUES("216a83c9-5de0-4852-a6f8-bffc2dff7a4f","Gerindra",1);
INSERT INTO partai(id,nama_partai,nomor_urut) 
VALUES("7fe2d4b7-2981-409e-bdfe-21053429f990","Golkar",2);
INSERT INTO partai(id,nama_partai,nomor_urut) 
VALUES("ab5a1201-1ed9-4be6-b8ab-6a4402ad3c2f","PDI Perjuangan",3);

INSERT INTO dapil(id,nama_dapil,provinsi,wilayah_dapil_list,jumlah_kursi)
VALUES("4b7d3a65-f2d1-4cd0-8158-8e8f77f56830","DaerahA","Jawa Barat","Bekasi",2);
INSERT INTO dapil(id,nama_dapil,provinsi,wilayah_dapil_list,jumlah_kursi)
VALUES("c362aea5-4a0d-4155-bd86-939c0526c4a1","DaerahB","Kalimantan Barat","Pontianak",10);
INSERT INTO dapil(id,nama_dapil,provinsi,wilayah_dapil_list,jumlah_kursi)
VALUES("eced3590-1fcb-49cf-8b30-b02d2c2cff71","DaerahC","Sumatera Utara","Medan",15);

INSERT INTO caleg(id,nama_dapil,nama_partai,nomor_urut,nama,jenis_kelamin)
VALUES("f2055721-d496-48ad-a62b-f5bd3a17e9e9","DaerahA","Gerindra",1,"Syamsudin","LAKILAKI");
INSERT INTO caleg(id,nama_dapil,nama_partai,nomor_urut,nama,jenis_kelamin)
VALUES("ddc50b1f-4d27-4243-9580-7976ae1d873e","DaerahB","Gerindra",10,"Tina","PEREMPUAN");
INSERT INTO caleg(id,nama_dapil,nama_partai,nomor_urut,nama,jenis_kelamin)
VALUES("9ced8988-924d-4606-afe6-1d73415c34ea","DaerahA","Golkar",2,"Budi","LAKILAKI");
INSERT INTO caleg(id,nama_dapil,nama_partai,nomor_urut,nama,jenis_kelamin)
VALUES("e7dd3f82-ea92-4f10-9797-99d89e5fd033","DaerahA","PDI Perjuangan",3,"Dian","PEREMPUAN");
INSERT INTO caleg(id,nama_dapil,nama_partai,nomor_urut,nama,jenis_kelamin)
VALUES("1a7c5241-f6f1-42dc-a293-c8dcf0923ed9","DaerahC","PDI Perjuangan",1,"Arthur","LAKILAKI");