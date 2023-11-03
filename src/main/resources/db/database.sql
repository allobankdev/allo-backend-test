DROP DATABASE pilpres;
CREATE DATABASE pilpres;

use pilpres;

CREATE TABLE caleg
(
	id INT(10) NOT NULL,
	nama VARCHAR(100) NOT NULL,
	nomor_urut VARCHAR(100) NOT NULL,
	dapil_id INT(10) NOT NULL,
	partai_id INT(10) NOT NULL,
	jenisKelamin VARCHAR(100) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY fk_caleg_dapil (dapil_id) REFERENCES dapil (id),
	FOREIGN KEY fk_caleg_partai (partai_id) REFERENCES partai (id)
) ENGINE innoDB;

SELECT * FROM caleg;
DESC caleg;

CREATE TABLE dapil
(
	id INT(10) NOT NULL,
	namaDapil VARCHAR(100) NOT NULL,
	provinsi VARCHAR(100) NOT NULL,
	wilayahDapilList VARCHAR(100) NOT NULL,
	jumlahKursi INT(100) NOT NULL,
	PRIMARY KEY (id)
) ENGINE InnoDB;

SELECT * FROM dapil;
DESC dapil;

CREATE TABLE partai
(
	id INT(10) NOT NULL,
	namaPartai VARCHAR(100) NOT NULL,
	nomorUrut VARCHAR(100) NOT NULL,
	PRIMARY KEY (id)
) ENGINE InnoDB

SELECT * FROM partai;
DESC partai;
