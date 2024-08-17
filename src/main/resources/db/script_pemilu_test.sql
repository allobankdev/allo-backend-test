CREATE DATABASE test;

create sequence test.calegsequence;
create sequence test.dapilsequence;
create sequence test.partaisequence;

CREATE TABLE test.caleg (
    Id number DEFAULT test.calegsequence.NEXTVAL,
    Dapil varchar2(255),
    Partai varchar2(255), 
    NomorUrut int,
    Nama varchar2(255),
    JenisKelamin varchar2(255)
);

Create table test.dapil (
  Id number DEFAULT test.dapilsequence.NEXTVAL,
  NamaDapil varchar2(255),
  Provinsi varchar2(255),
  WilayahDapil varchar2(255),
  JumlahKursi int
);

create table test.partai (
  Id number DEFAULT test.partaisequence.NEXTVAL,
  NamaPartai varchar2(255),
  NomorUrut int
);

INSERT INTO test.caleg
VALUES
(test.calegsequence.NEXTVAL, 'Tambun', 'Partai Kuning', 1, 'Pratama Agus', 'LAKILAKI'),
(test.calegsequence.NEXTVAL, 'Ps Minggu', 'Partai Biru', 2, 'Raka Ginting', 'LAKILAKI'),
(test.calegsequence.NEXTVAL, 'Mampang', 'Partai Hijo', 3, 'Sri Dewi', 'PEREMPUAN');

INSERT INTO test.dapil
VALUES
(test.dapilsequence.NEXTVAL, 'Tambun', 'Jawa Barat', 'Bekasi', '20'),
(test.dapilsequence.NEXTVAL, 'Ps Minggu', 'DKI Jakarta', 'Jakarta Selatan', '10'),
(test.dapilsequence.NEXTVAL, 'Mampang', 'DKI Jakarta', 'Jakarta Selatan', '7');

INSERT INTO test.partai
VALUES
(test.partaisequence.NEXTVAL, 'Partai Kuning', 1),
(test.partaisequence.NEXTVAL, 'Partai Biru', 2),
(test.partaisequence.NEXTVAL, 'Partai Hijo', 3);
