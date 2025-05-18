-- Database: pemilu
-- DROP DATABASE IF EXISTS pemilu;

CREATE DATABASE pemilu
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Indonesian_Indonesia.1252'
    LC_CTYPE = 'Indonesian_Indonesia.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- #####################################################################

INSERT INTO dapil (id, nama_dapil, provinsi, wilayah_dapil_list, jumlah_kursi)
VALUES (
    gen_random_uuid(), 
    'Jakarta I', 
    'DKI Jakarta', 
    ARRAY['Jakarta Pusat', 'Jakarta Selatan'], 
    6
);


INSERT INTO partai (id, nama_partai, nomor_urut)
VALUES 
(gen_random_uuid(), 'Partai Maju', 1),
(gen_random_uuid(), 'Partai Sejahtera', 2);

-- Ambil UUID Dapil
SELECT id FROM dapil WHERE nama_dapil = 'Jakarta I';

-- Ambil UUID Partai
SELECT id FROM partai WHERE nama_partai = 'Partai Maju';
SELECT id FROM partai WHERE nama_partai = 'Partai Sejahtera';

-- UUID yang ada pada data di bawah pada field dapil_id dan partai_id dapat di sesuaikan dengan query select di atas
INSERT INTO caleg (id, dapil_id, partai_id, nomor_urut, nama, jenis_kelamin)
VALUES
(gen_random_uuid(), '10cd3d83-219f-42cd-846d-8994dc0e0e1c', 'bb27a33c-bf7c-44aa-b7c1-12d71d1d7658', 1, 'Andi Wijaya', 0),
(gen_random_uuid(), '10cd3d83-219f-42cd-846d-8994dc0e0e1c', 'bb27a33c-bf7c-44aa-b7c1-12d71d1d7658', 2, 'Siti Rahma', 1),
(gen_random_uuid(), '10cd3d83-219f-42cd-846d-8994dc0e0e1c', 'f4181007-3cd9-4958-8887-014a1e84bdb9', 1, 'Budi Santoso', 0);