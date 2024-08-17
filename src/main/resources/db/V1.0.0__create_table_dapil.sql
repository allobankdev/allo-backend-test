CREATE TABLE IF NOT EXISTS public.dapil
(
    id uuid NOT NULL,
    jumlah_kursi integer NOT NULL,
    nama_dapil character varying(255) COLLATE pg_catalog."default" NOT NULL,
    provinsi character varying(255) COLLATE pg_catalog."default" NOT NULL,
    wilayah_dapil character varying(255)[] COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT dapil_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;