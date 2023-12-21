CREATE DATABASE allo_backend;

CREATE TABLE Partai (
    id VARCHAR(100) NOT NULL ,
    namaPartai VARCHAR(100) NOT NULL,
    nomorUrut INT,
    PRIMARY KEY (id)
);
SELECT * FROM Partai;

CREATE TABLE Dapil (
    id VARCHAR(100) NOT NULL ,
    namaDapil VARCHAR(100) NOT NULL,
    provinsi VARCHAR(100) NOT NULL,
    wilayahDapilList TEXT, -- You might want to use a separate table for this if it's a list of related entities
    jumlahKursi INT,
    PRIMARY KEY (id)
);
SELECT * FROM Dapil;

CREATE TABLE Caleg
(
    id           VARCHAR(100),
    dapil_id     VARCHAR(100),
    partai_id    VARCHAR(100),
    nomorUrut    INT,
    nama         VARCHAR(255) NOT NULL,
    jenisKelamin VARCHAR(20)  NOT NULL, -- Assuming you store the enum as a string
    PRIMARY KEY (id),
    FOREIGN KEY (dapil_id) REFERENCES Dapil (id),
    FOREIGN KEY (partai_id) REFERENCES Partai (id)
);
SELECT * FROM Caleg;