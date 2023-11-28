CREATE DATABASE pemilu_db;

CREATE TABLE IF NOT EXISTS partai (
    id              uuid             primary key,
    nama            varchar(50)      not null default '',
    nomor_urut      int              not null default 0,
    created_at      timestamp        not null default now(),
    updated_at      timestamp        not null,
    deleted_at      timestamp        null
);

CREATE TABLE IF NOT EXISTS dapil (
    id              uuid             primary key,
    nama            varchar(50)      not null default '',
    provinsi        varchar(50)      not null default '',
    jumlah_kursi    int              not null default 0,
    wilayah_dapil   json             not null default '[]',
    created_at      timestamp        not null default now(),
    updated_at      timestamp        not null,
    deleted_at      timestamp        null
);

CREATE TABLE IF NOT EXISTS caleg (
    id              uuid        primary key,
    dapil_id        uuid        not null,
    partai_id       uuid        not null,
    nomor_urut      int         not null default 0,
    nama            varchar(50) not null default '',
    jenis_kelamin   varchar(10) not null default '',
    created_at      timestamp   not null default now(),
    updated_at      timestamp   not null,
    deleted_at      timestamp   null,

    CONSTRAINT fk_dapil
        FOREIGN KEY(dapil_id)
            REFERENCES dapil(id),

    CONSTRAINT fk_partai
        FOREIGN KEY(partai_id)
            REFERENCES partai(id)
);